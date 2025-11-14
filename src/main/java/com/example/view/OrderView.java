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

    sb.append("Order ID: ").append(order.getOrderId()).append("\n\n");

    sb.append("Articles:\n");
    order.getArticles().forEach(a -> {
        sb.append("- Name: ").append(a.getName()).append("\n")
          .append("  Quantity: ").append(a.getQuantity()).append("\n")
          .append("  Unit Price: ").append(a.getPrice()).append("\n")
          .append("  Discount: ").append(a.getDiscount()).append("%\n\n");
    });

    resultArea.setText(sb.toString());
}


    // Muestra un mensaje (por ejemplo: "Order not found.")
    public void displayMessage(String message) {
        resultArea.setText(message);
    }
}
