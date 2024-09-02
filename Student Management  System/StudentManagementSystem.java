import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class StudentManagementSystem {
    private JFrame frame;
    private StudentDAO studentDAO;
    private JTextArea textArea;

    public StudentManagementSystem() {
        try {
            studentDAO = new StudentDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database.");
            System.exit(1);
        }

        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField idField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Course:"));
        panel.add(courseField);
        panel.add(new JLabel("ID (for update/delete):"));
        panel.add(idField);

        textArea = new JTextArea();
        textArea.setEditable(false);

        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton viewButton = new JButton("View All Students");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Student student = new Student(0,
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            emailField.getText(),
                            courseField.getText());
                    studentDAO.addStudent(student);
                    JOptionPane.showMessageDialog(frame, "Student added.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error adding student.");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    Student student = studentDAO.getStudent(id);
                    if (student != null) {
                        student.setName(nameField.getText());
                        student.setAge(Integer.parseInt(ageField.getText()));
                        student.setEmail(emailField.getText());
                        student.setCourse(courseField.getText());
                        studentDAO.updateStudent(student);
                        JOptionPane.showMessageDialog(frame, "Student updated.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Student not found.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error updating student.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    studentDAO.deleteStudent(id);
                    JOptionPane.showMessageDialog(frame, "Student deleted.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error deleting student.");
                }
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Student> students = studentDAO.getAllStudents();
                    textArea.setText("");
                    for (Student student : students) {
                        textArea.append(student.toString() + "\n");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error retrieving students.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystem());
    }
}
