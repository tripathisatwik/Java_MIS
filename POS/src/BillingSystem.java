//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.*;
//import java.awt.print.*;
//
//public class BillingSystem {
//
//    private JFrame frame;
//    private JTable table;
//    private DefaultTableModel model;
//    private JTextField searchField, nameField, priceField, qtyField;
//    private JButton addButton, createBillButton, resetButton, searchButton, printButton;
//    private Connection conn;
//
//    public BillingSystem() {
//        initializeUI();
//        connectDatabase();
//        addActionListeners();
//    }
//
//    private void initializeUI() {
//        frame = new JFrame("Billing System");
//        frame.setSize(900, 600);
//        frame.setLayout(new BorderLayout());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JLabel lblSearch = new JLabel("Search ID:");
//        searchField = new JTextField(10);
//        searchButton = new JButton("Search");
//        topPanel.add(lblSearch);
//        topPanel.add(searchField);
//        topPanel.add(searchButton);
//        frame.add(topPanel, BorderLayout.NORTH);
//
//        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
//        
//        model = new DefaultTableModel(new String[]{"S.N.", "Item", "Price", "Qty", "Tax (13%)", "Subtotal"}, 0);
//        table = new JTable(model);
//        JScrollPane pane = new JScrollPane(table);
//        centerPanel.add(pane);
//        
//        JPanel productPanel = new JPanel(new GridLayout(4, 2, 5, 5));
//        productPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
//        productPanel.add(new JLabel("Name:"));
//        nameField = new JTextField();
//        productPanel.add(nameField);
//        productPanel.add(new JLabel("Price:"));
//        priceField = new JTextField();
//        productPanel.add(priceField);
//        productPanel.add(new JLabel("Quantity:"));
//        qtyField = new JTextField();
//        productPanel.add(qtyField);
//        
//        addButton = new JButton("Add");
//        productPanel.add(new JLabel());
//        productPanel.add(addButton);
//        
//        centerPanel.add(productPanel);
//        
//        frame.add(centerPanel, BorderLayout.CENTER);
//        
//        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        createBillButton = new JButton("Create BILL");
//        createBillButton.setFont(new Font("Arial", Font.BOLD, 14));
//        createBillButton.setBackground(new Color(50, 150, 250));
//        createBillButton.setForeground(Color.WHITE);
//        resetButton = new JButton("Reset");
//        bottomPanel.add(createBillButton);
//        bottomPanel.add(resetButton);
//        frame.add(bottomPanel, BorderLayout.SOUTH);
//        
//        frame.setVisible(true);
//    }
//
//    private void connectDatabase() {
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billing_db", "satwik", "satwik");
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(frame, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void addActionListeners() {
//        searchButton.addActionListener(e -> searchProduct());
//        addButton.addActionListener(e -> addProduct());
//        createBillButton.addActionListener(e -> createBill());
//        resetButton.addActionListener(e -> resetFields());
//    }
//
//    private void searchProduct() {
//        String id = searchField.getText().trim();
//        if (id.isEmpty()) {
//            JOptionPane.showMessageDialog(frame, "Please enter a product ID.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE id = ?")) {
//            stmt.setInt(1, Integer.parseInt(id));
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                nameField.setText(rs.getString("name"));
//                priceField.setText(String.format("%.2f", rs.getDouble("price")));
//            } else {
//                JOptionPane.showMessageDialog(frame, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (SQLException | NumberFormatException ex) {
//            JOptionPane.showMessageDialog(frame, "Error searching product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void addProduct() {
//        try {
//            double price = Double.parseDouble(priceField.getText());
//            int qty = Integer.parseInt(qtyField.getText());
//            double tax = price * qty * 0.13;
//            double subtotal = (price * qty) + tax;
//
//            model.addRow(new Object[]{
//                model.getRowCount() + 1,
//                nameField.getText(),
//                price,
//                qty,
//                tax,
//                subtotal
//            });
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(frame, "Invalid price or quantity.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void createBill() {
//        if (model.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(frame, "No items to create bill.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        JFrame billFrame = new JFrame("Bill Details");
//        billFrame.setSize(600, 400);
//        billFrame.setLayout(new BorderLayout());
//        
//        String[] columnNames = {"Item", "Price", "Qty", "Tax (13%)", "Subtotal"};
//        DefaultTableModel billModel = new DefaultTableModel(columnNames, 0);
//        JTable billTable = new JTable(billModel);
//        
//        for (int i = 0; i < model.getRowCount(); i++) {
//            billModel.addRow(new Object[]{
//                model.getValueAt(i, 1),
//                model.getValueAt(i, 2),
//                model.getValueAt(i, 3),
//                model.getValueAt(i, 4),
//                model.getValueAt(i, 5)
//            });
//        }
//        
//        billFrame.add(new JScrollPane(billTable), BorderLayout.CENTER);
//        
//        JButton printBillButton = new JButton("Print Bill");
//        printBillButton.addActionListener(e -> {
//            try {
//                billTable.print();
//            } catch (PrinterException ex) {
//                JOptionPane.showMessageDialog(billFrame, "Print failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//        
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(printBillButton);
//        billFrame.add(buttonPanel, BorderLayout.SOUTH);
//        
//        billFrame.setVisible(true);
//    }
//
//    private void resetFields() {
//        model.setRowCount(0);
//        searchField.setText("");
//        nameField.setText("");
//        priceField.setText("");
//        qtyField.setText("");
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(BillingSystem::new);
//    }
//}