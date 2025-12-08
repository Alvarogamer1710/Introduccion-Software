package com.example.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Order;
import com.example.view.OrderView;

public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    // Atributos
    private OrderView view;
    private List<Order> orders;

    public OrderController(OrderView view, List<Order> orders) {
        // Inicializar atributos
        this.view = view;
        this.orders = orders;

        // Añadir listener al botón de búsqueda
        this.view.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrder();
            }
        });
    }

    private void searchOrder() {
        String id = view.getSearchId(); // obtener texto introducido en campo de búsqueda
        log.info("Buscando pedido con ID: {}", id);

        // Buscar pedido
        Order found = null;
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(id)) {
                found = o;
                break;
            }
        }

        // Mostrar resultado
        if (found != null) {
            log.info("Pedido encontrado: {}", found);
            try {
                // Intentar obtener el tipo de cambio (se hace en displayOrder)
                view.displayOrder(found);
            } catch (Exception e) {
                log.warn("Error al mostrar el pedido, pero continuamos mostrando información básica", e);
                view.displayMessage("Se encontró el pedido pero hubo un error al obtener el tipo de cambio.\n"
                        + "Mostrando información con tipo de cambio por defecto.\n\n" + found);
            }
        } else {
            log.warn("Pedido no encontrado con ID: {}", id);
            view.displayMessage("Order not found.");
        }
    }
}
