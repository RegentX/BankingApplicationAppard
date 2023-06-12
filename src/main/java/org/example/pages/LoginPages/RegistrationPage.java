package org.example.pages.LoginPages;

import org.example.HibernateUtil;
import org.example.entity.User;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


public class RegistrationPage extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField passportField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    public static String userPassport;
    public String userFirstName;
    public String userLastName;
    public RegistrationPage() {
        setTitle("Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450); // Увеличили высоту окна
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Используем GridBagLayout для управления компонентами

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10); // Добавляем отступы между компонентами

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel passportLabel = new JLabel("Pssport:");
        passportField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Retry password:");
        confirmPasswordField = new JPasswordField();

        JButton continueButton = new JButton("Continue");

        // Установка оранжевого цвета для текста кнопки
        continueButton.setForeground(Color.ORANGE);

        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String passport = passportField.getText();
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

                if (validateForm(firstName, lastName, passport, email, password, confirmPassword)) {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.getTransaction().begin();

                    OffsetDateTime regDate = OffsetDateTime.now();
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPassport(passport);
                    user.setRegistrationDate(regDate);
                    user.setUPassword(password);

                    session.save(user);
                    session.getTransaction().commit(); //save transaction

                    session.close();
                    //HibernateUtil.close();

                    userPassport = passport;
                    userFirstName = firstName;
                    userLastName = lastName;
                    // Выполнение действий по сохранению данных регистрации
                    JOptionPane.showMessageDialog(null, "Registration succeed!");
                    // Redirect the user to AuthPage
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(RegistrationPage.this);
                    AuthPage authPage = new AuthPage();
                    authPage.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Check if fulfilled data is correct!");
                }

            }
        });

        // Размещение компонентов с помощью GridBagLayout
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(firstNameLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(firstNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(lastNameLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(lastNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        add(passportLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        add(passportField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        add(emailLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        add(confirmPasswordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        add(confirmPasswordField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        add(continueButton, constraints);
    }

    private boolean validateForm(String firstName, String lastName, String passport, String email, String password, String confirmPassword) {
        // Проверка правильности заполнения формы регистрации
        // В данном примере просто возвращаем true, чтобы всегда считать форму заполненной правильно
        return true;
    }

}
