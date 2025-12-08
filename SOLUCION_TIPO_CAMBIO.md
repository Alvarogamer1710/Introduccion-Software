# Solución: Obtención de Tipo de Cambio EUR/USD desde Servicio Externo

## 📋 Descripción General

Se ha implementado un sistema para obtener dinámicamente el tipo de cambio EUR/USD desde un servicio externo (API), permitiendo mostrar los precios de los pedidos en ambas monedas de manera realista y actualizada.

## 🏗️ Arquitectura de la Solución

### 1. **ExchangeRateService** (Nuevo)
Clase responsable de la comunicación con la API externa.

**Características principales:**
- ✅ **Sin autenticación requerida**: Utiliza ExchangeRate-API que permite hasta 1500 requests/mes sin clave
- ✅ **Caché local**: Almacena el tipo de cambio durante 1 hora para evitar llamadas excesivas
- ✅ **Manejo de errores**: Incluye timeouts, reintentos con valores por defecto, y logging detallado
- ✅ **Thread-safe**: Utiliza `HttpClient` nativo de Java (desde Java 11)

**API Utilizada:**
```
URL: https://api.exchangerate-api.com/v4/latest/EUR
Método: GET
Respuesta JSON:
{
  "rates": {
    "USD": 1.0847,
    "GBP": 0.8573,
    ...
  }
}
```

**Métodos principales:**
- `getExchangeRate()`: Obtiene el tipo EUR/USD con caché
- `convertEURtoUSD(double eurAmount)`: Convierte EUR a USD
- `convertUSDtoEUR(double usdAmount)`: Convierte USD a EUR

### 2. **Order** (Modificado)
Se agregaron métodos para trabajar con múltiples monedas:

```java
// Métodos nuevos
public double getGrossTotalUSD()      // Total bruto en USD
public double getDiscountedTotalUSD() // Total con descuentos en USD
public double getExchangeRate()       // Tipo de cambio actual
```

### 3. **OrderView** (Mejorado)
Se rediseñó el método `displayOrder()` para mostrar:
- ✅ Tipo de cambio actual
- ✅ Todos los precios en EUR y USD
- ✅ Formato visual mejorado con separadores
- ✅ Detalles de cada artículo con ambas monedas

**Ejemplo de salida:**
```
═══════════════════════════════════════════
       DETALLES DEL PEDIDO: ORD001
═══════════════════════════════════════════

Tipo de cambio EUR/USD: 1 EUR = 1.0847 USD

ARTÍCULOS:
───────────────────────────────────────────
• Laptop
  Cantidad: 1 | Precio unitario: €999.99
  Descuento: 10.00%
  Total bruto: €999.99 | $1084.66
  Total con descuento: €899.99 | $976.19

═══════════════════════════════════════════
TOTAL BRUTO:       €999.99 | $1084.66
TOTAL CON DESCTO:  €899.99 | $976.19
═══════════════════════════════════════════
```

### 4. **OrderController** (Mejorado)
Se agregó manejo de excepciones para garantizar robustez:
- Si falla la API, se muestra el pedido con un tipo de cambio por defecto (1.10)
- Logging detallado de errores
- La aplicación no se crashea si no hay conexión

### 5. **pom.xml** (Actualizado)
Se agregó dependencia:
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

(Nota: JSON library ya estaba incluida, se agregó GSON como alternativa futura)

## 🔄 Flujo de Ejecución

```
Usuario busca pedido
         ↓
OrderController → searchOrder()
         ↓
Pedido encontrado
         ↓
OrderView → displayOrder(order)
         ↓
Order → getExchangeRate()
         ↓
ExchangeRateService → getExchangeRate()
         ├─ ¿Caché válido? → SÍ → Devolver caché
         └─ NO → Llamar API
              ├─ Éxito → Guardar en caché + Devolver
              └─ Error → Devolver valor por defecto (1.10)
         ↓
Mostrar precios en EUR y USD
```

## 🛡️ Características de Resiliencia

| Escenario | Comportamiento |
|-----------|-----------------|
| API disponible | Obtiene tipo de cambio real, lo cachea durante 1 hora |
| API caída | Usa tipo de cambio en caché si existe, sino 1.10 (aproximado) |
| Timeout de conexión | 5 segundos máximo, luego usa valor por defecto |
| Sin conexión a internet | Usa caché antiguo o valor por defecto |
| Primera llamada sin internet | Tipo de cambio 1.10 (realista) |

## 📊 Ventajas de la Solución

✅ **Realista**: Tipo de cambio dinámico, no es constante hardcodeada
✅ **Eficiente**: Caché de 1 hora evita llamadas innecesarias
✅ **Confiable**: Manejo robusto de errores y excepciones
✅ **Simple**: Sin autenticación, sin API keys
✅ **Escalable**: Fácil de cambiar de API o agregar más monedas
✅ **Transparente**: El usuario ve el tipo de cambio usado
✅ **Educativa**: Demuestra consumo de APIs externas en Java

## 🔧 Posibles Mejoras Futuras

1. **Pool de APIs**: Si una falla, intentar con otra (ej: Fixer.io, Open Exchange Rates)
2. **Base de datos**: Guardar tipos de cambio históricos
3. **Configuración**: Permitir cambiar monedas base y destino
4. **UI mejorada**: Mostrar variación del tipo de cambio
5. **Notificaciones**: Alertar si tipo de cambio sube/baja mucho
6. **Persistencia**: Guardar tipo de cambio en disco para arrange sin internet

## 📚 Recursos Utilizados

- **ExchangeRate-API**: https://www.exchangerate-api.com/
- **HttpClient Java**: https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html
- **SLF4J Logging**: Logging existente en el proyecto

## ✅ Verificación de Funcionamiento

Para probar:

1. Compilar con Maven:
   ```bash
   mvn clean compile
   ```

2. Ejecutar la aplicación:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.Main"
   ```

3. Buscar un pedido - se mostrará en EUR y USD con tipo de cambio actual

4. Ver logs para verificar llamadas a API:
   ```
   INFO  [ExchangeRateService] Obteniendo tipo de cambio EUR/USD desde API externa...
   INFO  [ExchangeRateService] Tipo de cambio obtenido exitosamente: 1 EUR = 1.0847 USD
   ```
