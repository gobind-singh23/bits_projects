package f2NBA;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseConnection extends JFrame{
    // JDBC URL, username, and password
    public static final String JDBC_URL_NBA = "jdbc:mysql://172.17.66.164:3306/NBA";
    public static final String JDBC_URL_formula1_final = "jdbc:mysql://172.17.66.164:3306/formula1_final";

    public static final String USERNAME = "winga7";
    public static final String PASSWORD = "ashok";    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    
    
    public DatabaseConnection() {
        setBounds(100, 100, 300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(10, 10, 80, 25);
        panel.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(100, 10, 160, 25);
        panel.add(usernameField);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(10, 40, 80, 25);
        panel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 40, 160, 25);
        panel.add(passwordField);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(100, 70, 80, 25);
        panel.add(signUpButton);

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://172.17.66.164:3306/myDatabase", "winga7", "ashok");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            statement.setString(1, username);
            statement.setString(2, password); // Consider hashing the password before storing it
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "User created successfully!");
                dispose(); // Close the sign-up form
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create user.");
            }
        } catch (SQLException e) {
        	if (e.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                // Display error message in GUI
                JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.");
            } else {
                // Display generic error message in GUI
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            };
        }
    }
    
//    public static void main(String[] args) {
//        Connection connection = null;
//        try {
//            // Establish connection to the database
//            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//            System.out.println("Connected to the database");
//
//            // Execute SQL queries
//            // Example: Select data from a table
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM Actions");
//            while (resultSet.next()) {
//                // Process the result set
//                String column1 = resultSet.getString("PlayerId");
//                int column2 = resultSet.getInt("GameId");
//                // Do something with the data
//                System.out.println("Column1: " + column1 + ", Column2: " + column2);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Close the connection
//            if (connection != null) {
//                try {
//                    connection.close();
//                    System.out.println("Disconnected from the database");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
