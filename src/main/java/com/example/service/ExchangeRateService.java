package com.example.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para obtener el tipo de cambio EUR/USD desde una API externa.
 * Implementa caché local para evitar llamadas excesivas a la API.
 */
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);
    
    // API endpoint - ExchangeRate-API (sin requiere autenticación para uso limitado)
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/EUR";
    
    // Caché del tipo de cambio
    private double cachedExchangeRate = -1;
    private long lastUpdateTime = 0;
    
    // Tiempo de expiración del caché (1 hora = 3600000 ms)
    private static final long CACHE_VALIDITY_MS = TimeUnit.HOURS.toMillis(1);
    
    // Timeout para la conexión
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    
    // Valor por defecto en caso de error
    private static final double DEFAULT_EXCHANGE_RATE = 1.10;

    /**
     * Obtiene el tipo de cambio EUR/USD.
     * Utiliza caché local para evitar llamadas excesivas.
     * 
     * @return Tipo de cambio EUR/USD (1 EUR = X USD)
     */
    public double getExchangeRate() {
        // Verificar si el caché sigue siendo válido
        if (isCacheValid()) {
            log.debug("Usando tipo de cambio en caché: {}", cachedExchangeRate);
            return cachedExchangeRate;
        }

        // Obtener nuevo tipo de cambio desde la API
        return fetchExchangeRateFromAPI();
    }

    /**
     * Verifica si el caché sigue siendo válido.
     */
    private boolean isCacheValid() {
        if (cachedExchangeRate <= 0) {
            return false;
        }
        long elapsedTime = System.currentTimeMillis() - lastUpdateTime;
        return elapsedTime < CACHE_VALIDITY_MS;
    }

    /**
     * Obtiene el tipo de cambio desde la API externa.
     */
    private double fetchExchangeRateFromAPI() {
        try {
            log.info("Obteniendo tipo de cambio EUR/USD desde API externa...");
            
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(REQUEST_TIMEOUT)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                double usdRate = jsonResponse.getJSONObject("rates").getDouble("USD");
                
                // Guardar en caché
                cachedExchangeRate = usdRate;
                lastUpdateTime = System.currentTimeMillis();
                
                log.info("Tipo de cambio obtenido exitosamente: 1 EUR = {} USD", usdRate);
                return usdRate;
            } else {
                log.warn("Error en la respuesta de la API. Código de estado: {}", response.statusCode());
                return getDefaultRate();
            }

        } catch (IOException e) {
            log.error("Error de conexión al obtener el tipo de cambio", e);
            return getDefaultRate();
        } catch (InterruptedException e) {
            log.error("Solicitud interrumpida al obtener el tipo de cambio", e);
            Thread.currentThread().interrupt();
            return getDefaultRate();
        } catch (Exception e) {
            log.error("Error inesperado al obtener el tipo de cambio", e);
            return getDefaultRate();
        }
    }

    /**
     * Devuelve un valor por defecto si la API no está disponible.
     * Este valor debería mostrarse al usuario como aproximado.
     */
    private double getDefaultRate() {
        log.warn("Usando tipo de cambio por defecto: {} (aproximado)", DEFAULT_EXCHANGE_RATE);
        
        // Si tenemos un valor en caché aunque sea antiguo, usarlo
        if (cachedExchangeRate > 0) {
            return cachedExchangeRate;
        }
        
        return DEFAULT_EXCHANGE_RATE;
    }

    /**
     * Convierte un valor en EUR a USD.
     */
    public double convertEURtoUSD(double eurAmount) {
        return eurAmount * getExchangeRate();
    }

    /**
     * Convierte un valor en USD a EUR.
     */
    public double convertUSDtoEUR(double usdAmount) {
        return usdAmount / getExchangeRate();
    }
}
