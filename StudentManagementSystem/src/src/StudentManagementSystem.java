package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentManagementSystem extends JFrame {
    private StudentDAO studentDAO;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField addNameField, addEmailField, addAgeField, addGradeField;
    private JTextField updateIdField, updateNameField, updateEmailField, updateAgeField, updateGradeField;
    private JTextField deleteIdField;
    private JTextField searchField;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;

    public StudentManagementSystem() {
        studentDAO = new StudentDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel viewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Age", "Grade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAllStudents());
        viewPanel.add(refreshButton, BorderLayout.SOUTH);
        tabbedPane.addTab("View All", viewPanel);

        JPanel addPanel = createStyledForm(new String[]{"Name:", "Email:", "Age:", "Grade:"},
                new JTextField[]{addNameField = new JTextField(), addEmailField = new JTextField(),
                        addAgeField = new JTextField(), addGradeField = new JTextField()}, "Add Student",
                e -> addStudent(), true);
        tabbedPane.addTab("Add Student", addPanel);

        JPanel updatePanel = createStyledForm(new String[]{"Student ID:", "New Name:", "New Email:", "New Age:", "New Grade:"},
                new JTextField[]{updateIdField = new JTextField(), updateNameField = new JTextField(),
                        updateEmailField = new JTextField(), updateAgeField = new JTextField(),
                        updateGradeField = new JTextField()}, "Update Student",
                e -> updateStudent(), true);
        tabbedPane.addTab("Update Student", updatePanel);

        JPanel deletePanel = createStyledForm(new String[]{"Student ID:"},
                new JTextField[]{deleteIdField = new JTextField(15)}, "Delete Student",
                e -> deleteStudent(), false);
        tabbedPane.addTab("Delete Student", deletePanel);

        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel searchInputPanel = new JPanel(new FlowLayout());
        searchInputPanel.add(new JLabel("Enter ID or Name:"));
        searchField = new JTextField(20);
        searchInputPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudent());
        searchInputPanel.add(searchButton);
        searchPanel.add(searchInputPanel, BorderLayout.NORTH);

        searchTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Age", "Grade"}, 0);
        searchTable = new JTable(searchTableModel);
        JScrollPane searchScrollPane = new JScrollPane(searchTable);
        searchPanel.add(searchScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Search Student", searchPanel);

        add(tabbedPane);
        loadAllStudents();
    }

    private JPanel createStyledForm(String[] labels, JTextField[] fields, String buttonText, ActionListener action, boolean centerButton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx++;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            fields[i].setPreferredSize(new Dimension(200, 25));
            panel.add(fields[i], gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        }

        JButton button = new JButton(buttonText);
        button.addActionListener(action);
        gbc.gridy++;
        gbc.gridx = centerButton ? 0 : 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(button, gbc);
        return panel;
    }

    private void loadAllStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getAge(),
                    student.getGrade()
            });
        }
    }

       private void addStudent() {
            String name = addNameField.getText().trim();
            String email = addEmailField.getText().trim();
            int age;
            String grade = addGradeField.getText().trim();
            try {
                age = Integer.parseInt(addAgeField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid age. Please enter a valid number.");
                return;
            }

            Student student = new Student(name, email, age, grade);
            boolean success = studentDAO.insertStudent(student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student added successfully.");
                addNameField.setText("");
                addEmailField.setText("");
                addAgeField.setText("");
                addGradeField.setText("");
                loadAllStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student. Check logs for details.");
            }
        }

        private void updateStudent() {
            int id;
            try {
                id = Integer.parseInt(updateIdField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID. Please enter a valid number.");
                return;
            }
            String name = updateNameField.getText().trim();
            String email = updateEmailField.getText().trim();
            int age;
            try {
                age = Integer.parseInt(updateAgeField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid age. Please enter a valid number.");
                return;
            }
            String grade = updateGradeField.getText().trim();

            Student student = new Student(id, name, email, age, grade);
            boolean success = studentDAO.updateStudent(student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                updateIdField.setText("");
                updateNameField.setText("");
                updateEmailField.setText("");
                updateAgeField.setText("");
                updateGradeField.setText("");
                loadAllStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student. Check logs for details.");
            }
        }

        private void deleteStudent() {
            int id;
            try {
                id = Integer.parseInt(deleteIdField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID. Please enter a valid number.");
                return;
            }
            boolean success = studentDAO.deleteStudent(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                deleteIdField.setText("");
                loadAllStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student. Check logs for details.");
            }
        }

        private void searchStudent() {
            String keyword = searchField.getText().trim();
            List<Student> students = studentDAO.getStudentByIdOrName(keyword);
            searchTableModel.setRowCount(0);
            for (Student student : students) {
                searchTableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getAge(),
                    student.getGrade()
                });
            }
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No matching student found.");
            }
        }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystem().setVisible(true));
    }
}
