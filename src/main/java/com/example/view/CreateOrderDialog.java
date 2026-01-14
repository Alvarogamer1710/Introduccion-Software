package com.example.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.example.Article;

public class CreateOrderDialog extends JDialog {

    private JTextField orderIdField = new JTextField(10);
    private JPanel articlesPanel = new JPanel();
    private JButton addArticleButton = new JButton("Add Article");
    private JButton saveButton = new JButton("Save Order");
    private JButton cancelButton = new JButton("Cancel");

    private List<ArticleInputPanel> articlePanels = new ArrayList<>();

    public CreateOrderDialog(JFrame parent) {
        super(parent, "Create New Order", true);
        setSize(500, 600);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel para ID del pedido
        JPanel idPanel = new JPanel();
        idPanel.add(new JLabel("Order ID:"));
        idPanel.add(orderIdField);
        mainPanel.add(idPanel, BorderLayout.NORTH);

        // Panel para artículos
        articlesPanel.setLayout(new GridLayout(0, 1, 5, 5));
        JScrollPane articlesScroll = new JScrollPane(articlesPanel);
        mainPanel.add(articlesScroll, BorderLayout.CENTER);

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addArticleButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    public String getOrderId() {
        return orderIdField.getText().trim();
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        for (ArticleInputPanel panel : articlePanels) {
            if (panel.isValid()) {
                articles.add(panel.getArticle());
            }
        }
        return articles;
    }

    public JButton getAddArticleButton() {
        return addArticleButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void addArticle() {
        ArticleInputPanel articlePanel = new ArticleInputPanel(articlePanels.size() + 1);
        articlePanels.add(articlePanel);
        articlesPanel.add(articlePanel);
        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    public void removeArticle(ArticleInputPanel panel) {
        articlePanels.remove(panel);
        articlesPanel.remove(panel);
        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    public void clearForm() {
        orderIdField.setText("");
        articlePanels.clear();
        articlesPanel.removeAll();
    }

    // =========== Clase interna para panel de artículo ===========

    public class ArticleInputPanel extends JPanel {
        private JTextField nameField = new JTextField(10);
        private JTextField quantityField = new JTextField(5);
        private JTextField priceField = new JTextField(5);
        private JTextField discountField = new JTextField(5);
        private JButton removeButton = new JButton("Remove");

        public ArticleInputPanel(int articleNumber) {
            setLayout(new GridLayout(2, 5, 5, 5));
            setBorder(new EmptyBorder(5, 5, 5, 5));

            // Primera fila de labels
            add(new JLabel("Name:"));
            add(new JLabel("Quantity:"));
            add(new JLabel("Price (€):"));
            add(new JLabel("Discount (%):"));
            add(new JLabel(""));

            // Segunda fila de campos
            add(nameField);
            add(quantityField);
            add(priceField);
            add(discountField);
            add(removeButton);

            removeButton.addActionListener(e -> removeArticle(this));
        }

        public Article getArticle() {
            try {
                String name = nameField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                double discount = Double.parseDouble(discountField.getText().trim());

                return new Article(name, quantity, price, discount);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public boolean isValid() {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) return false;

                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity <= 0) return false;

                double price = Double.parseDouble(priceField.getText().trim());
                if (price <= 0) return false;

                double discount = Double.parseDouble(discountField.getText().trim());
                if (discount < 0 || discount > 100) return false;

                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
