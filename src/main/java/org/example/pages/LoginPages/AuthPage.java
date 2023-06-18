package org.example.pages.LoginPages;

import org.example.HibernateUtil;
import org.example.entity.User;
import org.example.pages.HomePages.ProfilePage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    public static String userEmail;
    private ProfilePage profilePage;

    public AuthPage() {
        setTitle("APPARD BANK");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        getContentPane().setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.BLUE);
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLUE);
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Sign in");
        JButton registerButton = new JButton("Sign up");

        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLUE);

        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(Color.BLUE);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (authenticate(email, password)) {
                    userEmail = email;
                    JOptionPane.showMessageDialog(null, "Authorization succeeded!");
                    profilePage = new ProfilePage();
                    setContentPane(profilePage);
                    revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong email or password!");
                }
            }
        });

        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Registration");
                RegistrationPage registrationPage = new RegistrationPage();
                registrationPage.setVisible(true);
                dispose();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(emailLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(passwordField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        add(loginButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        add(registerButton, constraints);
    }

    private boolean authenticate(String email, String password) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<User> query = session.createQuery("FROM User WHERE email = :email AND uPassword = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            User user = query.uniqueResult();
            return (user != null);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
