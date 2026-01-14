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
import com.example.Order;

public class EditOrderDialog extends JDialog {

    private JPanel articlesPanel = new JPanel();
    private JButton saveButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");

    private List<ArticleEditPanel> articlePanels = new ArrayList<>();

    public EditOrderDialog(JFrame parent, Order order) {
        super(parent, "Edit Order: " + order.getOrderId(), true);
        setSize(500, 600);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título informativo
        JLabel titleLabel = new JLabel("Edit article quantities and discounts:");
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel para artículos
        articlesPanel.setLayout(new GridLayout(0, 1, 5, 5));
        JScrollPane articlesScroll = new JScrollPane(articlesPanel);
        mainPanel.add(articlesScroll, BorderLayout.CENTER);

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Cargar artículos del pedido
        loadArticles(order.getArticles());
    }

    /**
     * Carga los artículos del pedido en paneles editables
     */
    private void loadArticles(List<Article> articles) {
        for (Article article : articles) {
            ArticleEditPanel panel = new ArticleEditPanel(article);
            articlePanels.add(panel);
            articlesPanel.add(panel);
        }
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        for (ArticleEditPanel panel : articlePanels) {
            Article article = panel.getArticle();
            if (article != null && panel.isValid()) {
                articles.add(article);
            }
        }
        return articles;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    // =========== Clase interna para panel de edición de artículo ===========

    public class ArticleEditPanel extends JPanel {
        private Article originalArticle;
        private JLabel nameLabel;
        private JLabel priceLabel;
        private JTextField quantityField = new JTextField(5);
        private JTextField discountField = new JTextField(5);

        public ArticleEditPanel(Article article) {
            this.originalArticle = article;

            setLayout(new GridLayout(3, 2, 5, 5));
            setBorder(new EmptyBorder(5, 5, 5, 5));

            // Primera fila: nombre y precio (solo lectura)
            add(new JLabel("Name:"));
            nameLabel = new JLabel(article.getName());
            add(nameLabel);

            // Segunda fila: precio unitario (solo lectura)
            add(new JLabel("Unit Price:"));
            priceLabel = new JLabel(String.format("€%.2f", article.getPrice()));
            add(priceLabel);

            // Tercera fila: cantidad y descuento (editables)
            add(new JLabel("Quantity:"));
            quantityField.setText(String.valueOf(article.getQuantity()));
            add(quantityField);

            add(new JLabel("Discount (%):"));
            discountField.setText(String.valueOf(article.getDiscount()));
            add(discountField);
        }

        public Article getArticle() {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double discount = Double.parseDouble(discountField.getText().trim());

                // Crear nuevo artículo con los valores actualizados
                Article updated = new Article(
                        originalArticle.getName(),
                        quantity,
                        originalArticle.getPrice(),
                        discount
                );
                return updated;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public boolean isValid() {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity <= 0) return false;

                double discount = Double.parseDouble(discountField.getText().trim());
                if (discount < 0 || discount > 100) return false;

                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
