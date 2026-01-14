# Guía de Compilación y Ejecución - Aplicación de Gestión de Pedidos v1.2

## Requisitos Previos

- **Java**: JDK 17 o superior
- **Maven**: 3.6 o superior (opcional - se puede usar el wrapper)
- **Sistema Operativo**: Windows, Linux o macOS

## Estructura del Proyecto

```
demo/
├── pom.xml                                 # Configuración Maven
├── README.md                               # Documentación original
├── EVOLUTIVO_PEDIDOS.md                    # Documentación de los cambios
├── GUIA_COMPILACION.md                     # Este archivo
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Main.java                   # Clase principal de ejecución
│   │   │   ├── Order.java                  # Modelo de Pedido
│   │   │   ├── Article.java                # Modelo de Artículo
│   │   │   ├── Calculator.java             # Cálculos de totales
│   │   │   ├── Searcher.java               # Búsqueda de artículos
│   │   │   ├── controller/
│   │   │   │   └── OrderController.java    # Controlador (REFACTORIZADO)
│   │   │   ├── service/
│   │   │   │   └── ExchangeRateService.java # Servicio de tipos de cambio
│   │   │   ├── util/
│   │   │   │   └── JsonPersistenceUtil.java # NUEVO - Persistencia en JSON
│   │   │   └── view/
│   │   │       ├── OrderView.java          # Vista principal (REFACTORIZADA)
│   │   │       ├── CreateOrderDialog.java  # NUEVO - Formulario de creación
│   │   │       └── EditOrderDialog.java    # NUEVO - Formulario de edición
│   │   └── resources/
│   │       ├── orders.json                 # Base de datos (JSON)
│   │       ├── logback.xml                 # Configuración de logs
│   │       └── plantUML/
│   │           ├── casosDeUso.puml         # Diagrama de casos de uso (ACTUALIZADO)
│   │           ├── example.puml            # Diagrama de ejemplo
│   │           └── secuencia.puml          # Diagrama de secuencia
│   └── test/
│       └── java/com/example/               # Tests unitarios
├── target/                                 # Directorio de compilación
└── logs/                                   # Directorio de logs
```

## Método 1: Compilación con Maven (Recomendado)

### Paso 1: Instalar Maven (si no está instalado)
```bash
# En Windows (usando Chocolatey)
choco install maven

# O descargarlo manualmente desde https://maven.apache.org/download.cgi
```

### Paso 2: Compilar el Proyecto
```bash
# Navegar al directorio del proyecto
cd "c:\Users\Usuario\Introducción software\demo"

# Limpiar y compilar
mvn clean compile

# O compilar directamente
mvn compile
```

### Paso 3: Ejecutar la Aplicación
```bash
# Opción A: Usar el plugin exec-maven
mvn exec:java -Dexec.mainClass="com.example.Main"

# Opción B: Empaquetar y ejecutar
mvn package
java -jar target/demo-1.2.jar

# Opción C: Ejecutar desde target/classes
mvn compile
java -cp target/classes com.example.Main
```

## Método 2: Compilación Manual sin Maven

### Paso 1: Descargar Dependencias
Descargar los siguientes JARs y guardarlos en una carpeta `lib/`:
- `json-20240303.jar` (desde Maven Central)
- `gson-2.10.1.jar`
- `slf4j-api-2.0.15.jar`
- `logback-classic-1.5.19.jar`
- `logback-core-1.5.19.jar`

### Paso 2: Compilar Manualmente
```bash
# Navegar al proyecto
cd "c:\Users\Usuario\Introducción software\demo"

# Compilar todos los archivos Java
javac -d target/classes ^
  -cp "lib/*;target/classes" ^
  src/main/java/com/example/*.java ^
  src/main/java/com/example/controller/*.java ^
  src/main/java/com/example/service/*.java ^
  src/main/java/com/example/util/*.java ^
  src/main/java/com/example/view/*.java

# Copiar recursos
xcopy src\main\resources target\classes /Y /S
```

### Paso 3: Ejecutar
```bash
java -cp "target/classes;lib/*" com.example.Main
```

## Método 3: IDE (VS Code, IntelliJ IDEA, Eclipse)

### VS Code
1. Instalar extensión "Extension Pack for Java" de Microsoft
2. Abrir la carpeta del proyecto
3. VS Code detectará automáticamente la configuración Maven
4. Hacer clic en "Run" en la clase Main.java

### IntelliJ IDEA
1. File → Open → Seleccionar la carpeta del proyecto
2. IntelliJ detectará automáticamente pom.xml
3. Hacer clic derecho en Main.java → Run 'Main.main()'

### Eclipse
1. File → Open Projects from File System
2. Seleccionar la carpeta del proyecto
3. Hacer clic derecho → Run As → Java Application

## Verificación de Compilación Exitosa

Después de compilar, debería ver:
- El directorio `target/classes/` con los archivos `.class` compilados
- Sin mensajes de error en la salida
- Logs en la consola indicando que la aplicación se inició correctamente

## Solución de Problemas

### Error: "mvn: El término 'mvn' no se reconoce"
**Solución**: Maven no está en PATH. Instálalo o usa la ruta completa.

### Error: "No such file or directory: orders.json"
**Solución**: Es normal. La aplicación buscará el archivo en `src/main/resources/orders.json`. Asegúrate de estar ejecutando desde el directorio raíz del proyecto.

### Error: "Port already in use" (si hay servidor web)
**Solución**: La aplicación no usa puerto web. Si ves este error, verifica que no hay otra aplicación usando ese puerto.

### Error de Compilación: "symbol not found"
**Solución**: 
1. Verifica que todas las dependencias están en `pom.xml`
2. Ejecuta `mvn clean compile` para descargar las dependencias nuevamente
3. Verifica que los imports son correctos en los archivos Java

### La GUI no aparece en Linux/macOS
**Solución**: Asegúrate de que tienes un servidor X11 corriendo. En algunos sistemas puede requerir `DISPLAY=:0 java ...`

## Dependencias Utilizadas

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.15</version>
</dependency>

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.19</version>
</dependency>

<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20240303</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.8.1</version>
    <scope>test</scope>
</dependency>
```

## Ejecutar Tests

```bash
# Con Maven
mvn test

# Ejecutar un test específico
mvn test -Dtest=CalculatorTest

# Con cobertura
mvn test jacoco:report
```

## Generar JAR Ejecutable

```bash
# Crear un JAR con todas las dependencias
mvn assembly:single

# O crear un JAR con manifiesto que apunta a librerías externas
mvn jar:jar
```

## Limpiar Build

```bash
# Eliminar directorio target y regenerar
mvn clean

# Limpiar e instalar nuevamente
mvn clean install
```

## Información de la Versión

- **Versión del Proyecto**: 1.2
- **Java Target Version**: 17
- **Maven Version**: 3.6+
- **Fecha de Compilación**: Enero 2026

---

Para más información, consulta:
- [Documentación Maven](https://maven.apache.org/guides/)
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Documentación de las Nuevas Funcionalidades](EVOLUTIVO_PEDIDOS.md)
