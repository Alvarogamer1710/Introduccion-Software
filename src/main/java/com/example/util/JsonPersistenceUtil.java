package com.example.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Article;
import com.example.Order;

public class JsonPersistenceUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonPersistenceUtil.class);

    /**
     * Guarda una lista de pedidos en un archivo JSON.
     *
     * @param orders Lista de pedidos a guardar
     * @param filePath Ruta del archivo JSON
     * @return true si se guardó correctamente, false en caso de error
     */
    public static boolean saveOrdersToFile(List<Order> orders, String filePath) {
        try {
            // Convertir lista de pedidos a JSONArray
            JSONArray ordersArray = new JSONArray();

            for (Order order : orders) {
                JSONObject orderObj = new JSONObject();
                orderObj.put("id", order.getOrderId());

                // Convertir artículos
                JSONArray articlesArray = new JSONArray();
                for (Article article : order.getArticles()) {
                    JSONObject articleObj = new JSONObject();
                    articleObj.put("name", article.getName());
                    articleObj.put("quantity", article.getQuantity());
                    articleObj.put("unitPrice", article.getPrice());
                    articleObj.put("discount", article.getDiscount());
                    articlesArray.put(articleObj);
                }

                orderObj.put("articles", articlesArray);
                ordersArray.put(orderObj);
            }

            // Escribir en archivo
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Crear directorios si no existen

            try (FileWriter fileWriter = new FileWriter(file)) {
                // Indentar JSON para mejor legibilidad
                fileWriter.write(ordersArray.toString(2));
                fileWriter.flush();
                log.info("Pedidos guardados correctamente en: {}", filePath);
                return true;
            }

        } catch (IOException e) {
            log.error("Error al guardar pedidos en archivo JSON", e);
            return false;
        }
    }

    /**
     * Guarda los pedidos en el archivo orders.json en el sistema de archivos.
     * Nota: Intenta guardar en el directorio de recursos, pero si no es posible,
     * lo hará en la carpeta del proyecto raíz.
     *
     * @param orders Lista de pedidos a guardar
     * @return true si se guardó correctamente
     */
    public static boolean saveOrders(List<Order> orders) {
        // Intentar guardar en el classpath del proyecto
        String resourcePath = "src/main/resources/orders.json";

        if (saveOrdersToFile(orders, resourcePath)) {
            return true;
        }

        // Si no funciona, intentar en la raíz del proyecto
        String projectPath = "orders.json";
        return saveOrdersToFile(orders, projectPath);
    }
}
