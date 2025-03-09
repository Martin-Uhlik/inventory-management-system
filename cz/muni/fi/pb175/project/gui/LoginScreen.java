package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Users;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class representing login screen.
 * On successful user authentication the application is opened.
 * Error message otherwise.
 *
 * @author Martin Uhlik
 */
public class LoginScreen{
    private final JFrame frame;
    private JButton loginButton;
    private JComboBox<Users> userNameField;
    private JPanel loginPanel;
    private JLabel nameLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;

    /**
     * Object constructor.
     *
     * @param title title of window frame
     */
    public LoginScreen(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(loginPanel);

        frame.setSize(400, 250);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        userNameField.setModel(new DefaultComboBoxModel<>(Users.values()));

        LoginScreen self = this;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Users currentUser = (Users) userNameField.getSelectedItem();
                if (checkPassword(currentUser)) {
                    passwordField.setText("");
                    frame.dispose();
                    new MainMenu(title, self, currentUser);
                } else {
                    JOptionPane.showMessageDialog(frame, "Nesprávné přihlašovací údaje!");
                }
            }
        });

        showLogin();
    }

    /**
     * Shows this login window.
     */
    public void showLogin() {
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Checks if given password is correct.
     *
     * @param user selected user
     * @return true if given password is correct, false otherwise
     */
    private boolean checkPassword(Users user) {
        //TODO passwords should not be stored as plain text
        String pass = "";
        switch (user) {
            case MANAGER:
                pass = "manager";
                break;
            case PROGRAMMER:
                pass = "programmer";
                break;
            case OPERATOR:
                pass = "operator";
                break;
        }
        String cmpPass = Arrays.asList(passwordField.getPassword()).stream().
                map(String::valueOf)
                .collect(Collectors.joining());
        return pass.equals(cmpPass);
    }
}
