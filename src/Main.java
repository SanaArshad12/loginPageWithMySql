import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

 class LoginForm implements ActionListener {
    private JFrame frame;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton submitButton;

    public LoginForm() {
        frame = new JFrame("Login Frame");
        userNameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        userField = new JTextField(10);
        passwordField = new JPasswordField(10);
        submitButton = new JButton("Submit");

        submitButton.addActionListener(this);

        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 150, 10));
        frame.add(userNameLabel);
        frame.add(userField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(submitButton);

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String username = userField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter the username and password.");
                return;
            }

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "password");
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE stuname = ?")) {

                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    // Check password using a secure method, e.g., bcrypt
                    if (password.equals(storedPassword)) {
                        JOptionPane.showMessageDialog(null, "Login successful");
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect password");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User not found");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
