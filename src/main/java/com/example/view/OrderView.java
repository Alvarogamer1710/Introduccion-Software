package com.example.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.example.Order;
import com.example.Article;

public class OrderView extends JFrame {

    private JTextField searchField = new JTextField(10);
    private JButton searchButton = new JButton("Search");
    private JTextArea resultArea = new JTextArea(10, 40);

    public OrderView() {
        setTitle("Order Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Añadir componentes
        add(new JLabel("Order ID:"));
        add(searchField);
        add(searchButton);
        add(new JScrollPane(resultArea));

        resultArea.setEditable(false); // no permitir escribir en el área de resultados

        pack();
        setVisible(true);
    }

    // Devuelve el texto introducido por el usuario
    public String getSearchId() {
        return searchField.getText();
    }

    // Devuelve el botón de búsqueda (por si el controlador lo necesita)
    public JButton getSearchButton() {
        return searchButton;
    }

    // Permite al controlador registrar un listener en el botón
    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    // Muestra los detalles del pedido encontrado
    public void displayOrder(Order order) {
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
    }


    // Muestra un mensaje (por ejemplo: "Order not found.")
    public void displayMessage(String message) {
        resultArea.setText(message);
    }
}
