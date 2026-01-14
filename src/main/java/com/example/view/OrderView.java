package com.example.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.example.Article;
import com.example.Order;

public class OrderView extends JFrame {

    // Componentes para búsqueda
    private JTextField searchField = new JTextField(10);
    private JButton searchButton = new JButton("Search");

    // Componentes para lista de IDs
    private JTextArea orderListArea = new JTextArea(10, 40);

    // Componentes para resultado de búsqueda
    private JTextArea resultArea = new JTextArea(10, 40);

    // Botones de acciones
    private JButton deleteButton = new JButton("Delete Order");
    private JButton editButton = new JButton("Edit Order");
    private JButton createButton = new JButton("Create New Order");
    private JButton backButton = new JButton("Back to List");

    // Panel para mostrar componentes dinámicamente
    private JPanel mainPanel;

    // Estado actual
    private String currentOrderId = null;

    public OrderView() {
        setTitle("Order Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Crear panel superior para búsqueda y creación
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Order ID:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(createButton);

        // Crear panel para lista de IDs
        orderListArea.setEditable(false);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Available Orders:"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(orderListArea), BorderLayout.CENTER);

        // Crear panel para resultado de búsqueda
        resultArea.setEditable(false);
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Crear panel para botones de acciones
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(deleteButton);
        actionPanel.add(editButton);
        actionPanel.add(backButton);
        resultPanel.add(actionPanel, BorderLayout.SOUTH);

        // Agregar componentes al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        // Inicialmente mostrar solo la lista y ocultar panel de resultado
        resultPanel.setVisible(false);
        backButton.setVisible(false);
        deleteButton.setVisible(false);
        editButton.setVisible(false);

        setVisible(true);
    }

    // ========== MÉTODOS PARA BÚSQUEDA ==========

    public String getSearchId() {
        return searchField.getText();
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    // ========== MÉTODOS PARA CREACIÓN ==========

    public JButton getCreateButton() {
        return createButton;
    }

    public void addCreateListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    // ========== MÉTODOS PARA ELIMINACIÓN ==========

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void addDeleteListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }

    // ========== MÉTODOS PARA EDICIÓN ==========

    public JButton getEditButton() {
        return editButton;
    }

    public void addEditListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    // ========== MÉTODOS PARA NAVEGACIÓN ==========

    public JButton getBackButton() {
        return backButton;
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // ========== MÉTODOS PARA MOSTRAR DATOS ==========

    /**
     * Muestra la lista de IDs de pedidos disponibles
     */
    public void displayOrderList(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════\n");
        sb.append("         LISTA DE PEDIDOS DISPONIBLES\n");
        sb.append("═══════════════════════════════════════════\n\n");

        if (orders.isEmpty()) {
            sb.append("No hay pedidos disponibles.\n");
        } else {
            for (Order order : orders) {
                sb.append(String.format("• Order ID: %s\n", order.getOrderId()));
            }
        }

        sb.append("\n═══════════════════════════════════════════\n");
        sb.append("(Introduce el ID en el campo de búsqueda)\n");

        orderListArea.setText(sb.toString());
        clearSearchField();
    }

    /**
     * Muestra los detalles de un pedido encontrado
     */
    public void displayOrder(Order order) {
        this.currentOrderId = order.getOrderId();

        StringBuilder sb = new StringBuilder();

        // Encabezado
        sb.append("═══════════════════════════════════════════\n");
        sb.append(String.format("       DETALLES DEL PEDIDO: %s\n", order.getOrderId()));
        sb.append("═══════════════════════════════════════════\n\n");

        // Tipo de cambio
        double exchangeRate = order.getExchangeRate();
        sb.append(String.format("Tipo de cambio EUR/USD: 1 EUR = %.4f USD\n\n", exchangeRate));

        // Encabezado de artículos
        sb.append("ARTÍCULOS:\n");
        sb.append("───────────────────────────────────────────\n");

        // Detalles de cada artículo
        for (Article article : order.getArticles()) {
            sb.append(String.format("• %s\n", article.getName()));
            sb.append(String.format("  Cantidad: %d | Precio unitario: €%.2f\n",
                    article.getQuantity(), article.getPrice()));
            sb.append(String.format("  Descuento: %.2f%%\n", article.getDiscount()));
            sb.append(String.format("  Total bruto: €%.2f | $%.2f\n",
                    article.getGrossAmount(),
                    exchangeRate * article.getGrossAmount()));
            sb.append(String.format("  Total con descuento: €%.2f | $%.2f\n\n",
                    article.getDiscountedAmount(),
                    exchangeRate * article.getDiscountedAmount()));
        }

        // Totales
        sb.append("═══════════════════════════════════════════\n");
        sb.append(String.format("TOTAL BRUTO:       €%.2f | $%.2f\n",
                order.getGrossTotal(),
                order.getGrossTotalUSD()));
        sb.append(String.format("TOTAL CON DESCTO:  €%.2f | $%.2f\n",
                order.getDiscountedTotal(),
                order.getDiscountedTotalUSD()));
        sb.append("═══════════════════════════════════════════\n");

        resultArea.setText(sb.toString());
        showDetailView();
    }

    /**
     * Muestra un mensaje genérico
     */
    public void displayMessage(String message) {
        resultArea.setText(message);
        showDetailView();
    }

    /**
     * Cambia la vista para mostrar detalles del pedido
     */
    private void showDetailView() {
        JPanel centerPanel = (JPanel) mainPanel.getComponent(1);
        JPanel southPanel = (JPanel) mainPanel.getComponent(2);

        centerPanel.setVisible(false);
        southPanel.setVisible(true);
        backButton.setVisible(true);
        deleteButton.setVisible(true);
        editButton.setVisible(true);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Cambia la vista para mostrar la lista de pedidos
     */
    public void showListView() {
        JPanel centerPanel = (JPanel) mainPanel.getComponent(1);
        JPanel southPanel = (JPanel) mainPanel.getComponent(2);

        centerPanel.setVisible(true);
        southPanel.setVisible(false);
        backButton.setVisible(false);
        deleteButton.setVisible(false);
        editButton.setVisible(false);

        this.currentOrderId = null;
        clearSearchField();

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Limpia el campo de búsqueda
     */
    public void clearSearchField() {
        searchField.setText("");
    }

    /**
     * Muestra un diálogo simple con mensaje
     */
    public void showAlert(String title, String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, title,
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un diálogo de error
     */
    public void showError(String title, String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, title,
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
