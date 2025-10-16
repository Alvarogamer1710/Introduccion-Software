package com.example;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Iniciando carga de pedidos...");

        try {
            // Leer el archivo JSON desde resources
            InputStream inputStream = Main.class.getResourceAsStream("/orders.json");
            if (inputStream == null) {
                log.error("No se encontró el archivo orders.json en resources");
                return;
            }

            // Convertir el contenido del InputStream a String
            String jsonText;
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                jsonText = scanner.useDelimiter("\\A").next();
            }

            // Parsear el JSON
            JSONArray ordersArray = new JSONArray(jsonText);
            List<Order> orders = new ArrayList<>();

            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject orderObj = ordersArray.getJSONObject(i);

                Order order = new Order();
                order.setOrderId(orderObj.getString("id")); // el JSON usa "id", tu clase usa "orderId"

                JSONArray articlesArray = orderObj.getJSONArray("articles");
                List<Article> articles = new ArrayList<>();

                for (int j = 0; j < articlesArray.length(); j++) {
                    JSONObject articleObj = articlesArray.getJSONObject(j);

                    Article article = new Article();
                    article.setName(articleObj.getString("name"));
                    article.setQuantity(articleObj.getInt("quantity"));
                    article.setPrice(articleObj.getDouble("unitPrice")); // tu clase usa "price"
                    article.setDiscount(articleObj.getDouble("discount"));

                    articles.add(article);
                }

                order.setArticles(articles);
                orders.add(order);

                log.debug("Loaded order: {}", order.getOrderId());
            }

            log.info("Se cargaron {} pedidos correctamente.", orders.size());

        } catch (Exception e) {
            log.error("Error al leer o parsear el archivo orders.json", e);
        }
    }
}
