import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class POSLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Connection connection;

    public POSLogin() {
        setTitle("POS System Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);

        loginButton.addActionListener(e -> authenticateUser());
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "password");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = SHA2(?, 256)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new POSMain().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new POSLogin().setVisible(true));
    }
}

class POSMain extends JFrame {
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField nameField, priceField, stockField;
    private JComboBox<String> categoryBox;
    private JButton addButton, updateButton, deleteButton, checkoutButton;
    private Connection connection;

    public POSMain() {
        setTitle("POS System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Category", "Price", "Stock"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        loadProducts();
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Category:"));
        categoryBox = new JComboBox<>(new String[]{"Electronics", "Groceries", "Clothing"});
        inputPanel.add(categoryBox);
        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        inputPanel.add(stockField);

        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        checkoutButton = new JButton("Checkout");

        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(checkoutButton);

        add(inputPanel, BorderLayout.SOUTH);
        connectToDatabase();

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        checkoutButton.addActionListener(e -> checkout());
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "satwik", "satwik");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void loadProducts() {a
        productTableModel.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");
            while (rs.next()) {
                productTableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getInt("stock_quantity")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Loading Products", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (int) productTableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String category = (String) categoryBox.getSelectedItem();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        
        try {
            String query = "UPDATE products SET name = ?, category = ?, price = ?, stock_quantity = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, id);
            ps.executeUpdate();
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Updating Product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkout() {
        JOptionPane.showMessageDialog(this, "Checkout feature not yet implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addProduct() {
        String name = nameField.getText();
        String category = (String) categoryBox.getSelectedItem();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        
        try {
            String query = "INSERT INTO products (name, category, price, stock_quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.executeUpdate();
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Adding Product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (int) productTableModel.getValueAt(selectedRow, 0);
        
        try {
            String query = "DELETE FROM products WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Deleting Product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
