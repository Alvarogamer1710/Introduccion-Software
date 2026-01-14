package com.example.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Article;
import com.example.Order;
import com.example.util.JsonPersistenceUtil;
import com.example.view.OrderView;
import com.example.view.CreateOrderDialog;
import com.example.view.EditOrderDialog;

public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    // Atributos
    private OrderView view;
    private List<Order> orders;

    public OrderController(OrderView view, List<Order> orders) {
        // Inicializar atributos
        this.view = view;
        this.orders = orders;

        // Mostrar lista inicial de pedidos
        view.displayOrderList(orders);

        // Añadir listener al botón de búsqueda
        this.view.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrder();
            }
        });

        // Añadir listener al botón de creación
        this.view.addCreateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewOrder();
            }
        });

        // Añadir listener al botón de eliminación
        this.view.addDeleteListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOrder();
            }
        });

        // Añadir listener al botón de edición
        this.view.addEditListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editOrder();
            }
        });

        // Añadir listener al botón de volver
        this.view.addBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToList();
            }
        });
    }

    /**
     * Busca un pedido por ID y lo muestra
     */
    private void searchOrder() {
        String id = view.getSearchId();
        if (id.isEmpty()) {
            view.displayMessage("Please enter an Order ID to search.");
            return;
        }

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
                view.displayOrder(found);
            } catch (Exception e) {
                log.warn("Error al mostrar el pedido", e);
                view.displayMessage("Se encontró el pedido pero hubo un error al obtener el tipo de cambio.\n"
                        + "Mostrando información con tipo de cambio por defecto.\n\n" + found);
            }
        } else {
            log.warn("Pedido no encontrado con ID: {}", id);
            view.displayMessage("Order with ID '" + id + "' not found.");
        }
    }

    /**
     * Abre el diálogo para crear un nuevo pedido
     */
    private void createNewOrder() {
        log.info("Abriendo diálogo de creación de pedido");

        CreateOrderDialog dialog = new CreateOrderDialog(view);

        // Listener para agregar artículos
        dialog.getAddArticleButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.addArticle();
            }
        });

        // Listener para guardar
        dialog.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveNewOrder(dialog);
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Guarda un nuevo pedido creado desde el diálogo
     */
    private void saveNewOrder(CreateOrderDialog dialog) {
        String orderId = dialog.getOrderId();
        List<Article> articles = dialog.getArticles();

        // Validaciones
        if (orderId.isEmpty()) {
            view.showError("Error", "Please enter an Order ID.");
            return;
        }

        if (articles.isEmpty()) {
            view.showError("Error", "Please add at least one article.");
            return;
        }

        // Validar que el ID sea único
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                view.showError("Error", "Order ID '" + orderId + "' already exists.");
                return;
            }
        }

        // Crear nuevo pedido
        Order newOrder = new Order();
        newOrder.setOrderId(orderId);
        newOrder.setArticles(articles);

        // Agregar a la lista
        orders.add(newOrder);

        // Guardar en archivo JSON
        if (JsonPersistenceUtil.saveOrders(orders)) {
            log.info("Pedido creado y guardado correctamente: {}", orderId);
            view.showAlert("Success", "Order '" + orderId + "' created successfully!");

            // Actualizar lista y cerrar diálogo
            view.displayOrderList(orders);
            dialog.dispose();
        } else {
            log.error("Error al guardar el pedido en JSON");
            view.showError("Error", "Failed to save order. Please check the file permissions.");
            // Eliminar de la lista si falló el guardado
            orders.remove(newOrder);
        }
    }

    /**
     * Elimina un pedido
     */
    private void deleteOrder() {
        String orderId = view.getCurrentOrderId();
        if (orderId == null || orderId.isEmpty()) {
            view.showError("Error", "No order selected.");
            return;
        }

        // Confirmación
        int result = javax.swing.JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete order '" + orderId + "'?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (result != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        log.info("Eliminando pedido con ID: {}", orderId);

        // Buscar y eliminar
        Order toDelete = null;
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                toDelete = o;
                break;
            }
        }

        if (toDelete != null) {
            orders.remove(toDelete);

            // Guardar en JSON
            if (JsonPersistenceUtil.saveOrders(orders)) {
                log.info("Pedido eliminado correctamente: {}", orderId);
                view.showAlert("Success", "Order '" + orderId + "' deleted successfully!");

                // Volver a la lista
                backToList();
            } else {
                log.error("Error al guardar cambios después de eliminar");
                view.showError("Error", "Failed to delete order. Please check the file permissions.");
                // Restaurar el pedido si falló el guardado
                orders.add(toDelete);
            }
        }
    }

    /**
     * Abre el diálogo para editar un pedido
     */
    private void editOrder() {
        String orderId = view.getCurrentOrderId();
        if (orderId == null || orderId.isEmpty()) {
            view.showError("Error", "No order selected.");
            return;
        }

        log.info("Abriendo diálogo de edición para pedido: {}", orderId);

        // Buscar el pedido
        Order orderToEdit = null;
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                orderToEdit = o;
                break;
            }
        }

        if (orderToEdit == null) {
            view.showError("Error", "Order not found.");
            return;
        }

        // Hacer la variable final para usar en listener
        final Order orderForEditing = orderToEdit;
        EditOrderDialog dialog = new EditOrderDialog(view, orderToEdit);

        // Listener para guardar cambios
        dialog.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEditedOrder(dialog, orderForEditing);
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Guarda los cambios en un pedido editado
     */
    private void saveEditedOrder(EditOrderDialog dialog, Order order) {
        List<Article> updatedArticles = dialog.getArticles();

        if (updatedArticles.isEmpty()) {
            view.showError("Error", "Order must have at least one article.");
            return;
        }

        // Actualizar el pedido
        order.setArticles(updatedArticles);

        // Guardar en JSON
        if (JsonPersistenceUtil.saveOrders(orders)) {
            log.info("Pedido editado y guardado correctamente: {}", order.getOrderId());
            view.showAlert("Success", "Order updated successfully!");

            // Refrescar la vista del pedido
            view.displayOrder(order);
            dialog.dispose();
        } else {
            log.error("Error al guardar cambios del pedido");
            view.showError("Error", "Failed to save changes. Please check the file permissions.");
        }
    }

    /**
     * Vuelve a la pantalla de lista de pedidos
     */
    private void backToList() {
        log.info("Volviendo a la lista de pedidos");
        view.showListView();
        view.displayOrderList(orders);
    }
}
