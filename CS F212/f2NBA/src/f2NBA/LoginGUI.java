package f2NBA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton deleteAccButton;
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginGUI window = new LoginGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
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

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 70, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });    
        
        
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(100, 100, 160, 25);
        panel.add(signUpButton);

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSignUpForm();
            }
        });
        
        deleteAccButton = new JButton("Delete Account");
        deleteAccButton.setBounds(100, 130, 160, 25);
        panel.add(deleteAccButton);

        deleteAccButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAcc();
            }
        });
        
        
//        JButton signUpButton = new JButton("Sign Up");
//        signUpButton.setBounds(100, 100, 80, 25);
//        panel.add(signUpButton);
//
//        signUpButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                openSignUpForm();
//            }
//        });
//        
        
        
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://172.17.66.164:3306/myDatabase", "winga7", "ashok");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Login successful
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                LoginPage.createMainWindow();
            } else {
                // Login failed
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void openSignUpForm() {
        DatabaseConnection signUpForm = new DatabaseConnection();
        signUpForm.setVisible(true);
    }
    private void deleteAcc() {
        DeleteAccForm DeleteAccForm = new DeleteAccForm();
        DeleteAccForm.setVisible(true);
    }
    
}
