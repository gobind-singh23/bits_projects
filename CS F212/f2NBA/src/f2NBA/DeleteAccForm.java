package f2NBA;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DeleteAccForm extends JFrame{
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton deleteAccButton;

    public DeleteAccForm() {
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
        
        deleteAccButton = new JButton("Delete This Account");
        deleteAccButton.setBounds(100, 100, 80, 25);
        panel.add(deleteAccButton);

        deleteAccButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAcc();
            }
        });
    }
     
        private void deleteAcc() {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://172.17.66.164:3306/myDatabase", "winga7", "ashok");
                PreparedStatement statement = connection.prepareStatement("DELETE FROM users where username = ? and password = ?");
                statement.setString(1, username);
                statement.setString(2, password); // Consider hashing the password before storing it
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    dispose(); // Close the sign-up form
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
