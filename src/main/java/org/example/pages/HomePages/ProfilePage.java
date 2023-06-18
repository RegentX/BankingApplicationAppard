package org.example.pages.HomePages;

import org.example.HibernateUtil;
import org.example.entity.Account;
import org.example.entity.User;
import org.example.pages.HomePages.HistoryPage;
import org.example.pages.HomePages.TransactionPage;
import org.example.pages.LoginPages.AuthPage;
import org.example.pages.LoginPages.RegistrationPage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilePage extends JPanel {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem transactionMenuItem;
    private JMenuItem historyMenuItem;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel surnameLabel;
    private JLabel accountLabel;
    private JLabel totalAmountLabel;

    private JButton editButton;
    private JButton createAccountButton;
    private JButton deleteButton;
    private String profilePassport = RegistrationPage.userPassport;
    private DefaultListModel<String> accountListModel;
    private JList<String> accountList;
    private JPanel accountPanel;

    private TransactionPage transactionPage;
    private HistoryPage historyPage;

    public ProfilePage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create the menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLUE);
        menuBar.setForeground(Color.WHITE);

        // Create the file menu
        fileMenu = new JMenu("Menu");
        fileMenu.setForeground(Color.WHITE);

        // Create the transaction menu item
        transactionMenuItem = new JMenuItem("Transactions");
        transactionMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle transaction action here
                transactionPage = new TransactionPage();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ProfilePage.this);
                frame.setContentPane(transactionPage);
                frame.revalidate();
            }
        });
        transactionMenuItem.setForeground(Color.WHITE);
        transactionMenuItem.setBackground(Color.BLUE);

        historyMenuItem = new JMenuItem("History");
        historyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle history action here
                historyPage = new HistoryPage();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ProfilePage.this);
                frame.setContentPane(historyPage);
                frame.revalidate();
            }
        });
        historyMenuItem.setForeground(Color.WHITE);
        historyMenuItem.setBackground(Color.BLUE);

        fileMenu.add(historyMenuItem);
        // Add the transaction menu item to the file menu
        fileMenu.add(transactionMenuItem);

        // Add the file menu to the menu bar
        menuBar.add(fileMenu);

        // Create the menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.BLUE);
        menuPanel.add(menuBar);

        // Add the menu panel to the panel
        add(menuPanel, BorderLayout.NORTH);

        // Create the profile info panel
        profilePassport = getUserPassportByEmail(AuthPage.userEmail);
        JPanel profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBackground(Color.WHITE);

        String name = getUserNameByEmail(AuthPage.userEmail);
        String surname = getUserSurnameByEmail(AuthPage.userEmail);
        String totalAmount = sumAccountAmountsByPassport(profilePassport).toString();

        nameLabel = new JLabel("Name: " + name);
        surnameLabel = new JLabel("Surname: " + surname);
        totalAmountLabel = new JLabel("Total Amount: " + totalAmount);

        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        surnameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 20));

        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        surnameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        profileInfoPanel.add(Box.createVerticalGlue()); // Add vertical glue for centering
        profileInfoPanel.add(nameLabel);
        profileInfoPanel.add(surnameLabel);
        profileInfoPanel.add(totalAmountLabel);
        profileInfoPanel.add(Box.createVerticalGlue());

        profileInfoPanel.add(nameLabel);
        profileInfoPanel.add(surnameLabel);
        profileInfoPanel.add(totalAmountLabel);

        accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add some padding

        // Create a scroll pane for the account list
        JScrollPane accountScrollPane = new JScrollPane(accountPanel);
        accountScrollPane.setBorder(null);
        accountScrollPane.setBackground(Color.WHITE);

        // Create the buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        // Create and add create account button
        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle create account button click here
                createAccountDialog();
            }
        });
        createAccountButton.setBackground(Color.BLUE);
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(createAccountButton);

        // Create and add edit profile button
        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle edit profile button click here
                JOptionPane.showMessageDialog(null, "Edit Profile button clicked!");
                editProfileDialog();
            }
        });
        editProfileButton.setBackground(Color.BLUE);
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBorderPainted(false);
        editProfileButton.setFocusPainted(false);
        editProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(editProfileButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle delete account button click here
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account and all associated accounts?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Delete user and associated accounts
                    redirectToAuthPage();
                    deleteUserAndAccounts(profilePassport);
                    // Redirect to authentication page

                }
            }
        });
        deleteAccountButton.setBackground(Color.RED);
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.setBorderPainted(false);
        deleteAccountButton.setFocusPainted(false);
        deleteAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(deleteAccountButton);

        // Create a panel to hold profile info and accounts
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(profileInfoPanel, BorderLayout.NORTH);
        contentPanel.add(accountScrollPane, BorderLayout.CENTER);

        // Create a panel to hold the content panel and buttons panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add the main panel to the panel
        add(mainPanel, BorderLayout.CENTER);

        // Load and display user accounts
        loadUserAccounts();
    }

    private void editProfileDialog() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ProfilePage.this);
        JDialog dialog = new JDialog(frame, "Edit Profile", true);
        dialog.setLayout(new FlowLayout());

        // Retrieve the current user data
        String currentName = nameLabel.getText();
        String currentSurname = surnameLabel.getText();

        // Create and add name label and text field
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField(currentName, 10);
        dialog.add(nameLabel);
        dialog.add(nameTextField);

        // Create and add email label and text field
        JLabel emailLabel = new JLabel("Email:");
        JTextField surnameTextField = new JTextField(currentSurname, 10);
        dialog.add(emailLabel);
        dialog.add(surnameTextField);

        // Create and add save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle save button click here
                String newName = nameTextField.getText();
                String newSurname = surnameTextField.getText();

                // Update the user data in the User table
                updateUserProfile(newName, newSurname);

                // Update the displayed name and email labels
                nameLabel.setText(newName);
                emailLabel.setText(newSurname);

                // Close the dialog
                dialog.dispose();
            }
        });
        dialog.add(saveButton);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void deleteUserAndAccounts(String passport) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            String deleteAccountsHql = "DELETE FROM Account WHERE passport = :passport";
            Query<?> deleteAccountsQuery = session.createQuery(deleteAccountsHql);
            deleteAccountsQuery.setParameter("passport", passport);
            deleteAccountsQuery.executeUpdate();
            // Delete user
            String deleteUserHql = "DELETE FROM User WHERE passport = :passport";
            Query<?> deleteUserQuery = session.createQuery(deleteUserHql);
            deleteUserQuery.setParameter("passport", passport);
            deleteUserQuery.executeUpdate();

            // Delete accounts associated with the passport


            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void redirectToAuthPage() {
        AuthPage authPage = new AuthPage();

        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(ProfilePage.this);
        currentFrame.dispose(); // Close the current window

        // Create a new window for the authentication page
        JFrame newFrame = new JFrame("Authentication Page");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Update the close operation

        // Create a panel to hold the authPage component
        JPanel panel = new JPanel();
        panel.add(authPage);

        newFrame.getContentPane().add(panel); // Add the panel to the new frame's content pane
        newFrame.pack();
        newFrame.setLocationRelativeTo(null); // Center the new window on the screen
        newFrame.setVisible(true);
    }




    private void updateUserProfile(String name, String surname) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Get the user by email
            String hql = "FROM User u WHERE u.email = :email";
            Query<User> query = session.createQuery(hql);
            query.setParameter("email", AuthPage.userEmail);
            User user = query.uniqueResult();

            // Update the user data
            user.setFirstName(name);
            user.setLastName(surname);

            // Save the updated user to the database
            session.update(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Load and display user accounts
    // Load and display user accounts
    private void loadUserAccounts() {
        profilePassport = getUserPassportByEmail(AuthPage.userEmail);
        List<Account> accounts = getUserAccountsByPassport(profilePassport);

        accountPanel.removeAll(); // Clear the account panel

        for (Account account : accounts) {
            JPanel accountRectangle = createAccountRectangle(account);
            accountPanel.add(accountRectangle);
        }

        revalidate(); // Revalidate the panel to reflect the changes
    }

    // Create an account rectangle with account info and delete button
    private JPanel createAccountRectangle(Account account) {
        JPanel rectangle = new JPanel(new BorderLayout());
        rectangle.setBackground(Color.WHITE);
        rectangle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add a border

        // Create a label to display account info
        JLabel accountInfoLabel = new JLabel(getAccountInfo(account));
        accountInfoLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add some padding
        accountInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font style and size
        rectangle.add(accountInfoLabel, BorderLayout.CENTER);

        // Create the delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle delete button click here
                deleteAccount(account);
                loadUserAccounts(); // Reload the accounts after deletion
            }
        });
        rectangle.add(deleteButton, BorderLayout.EAST);

        return rectangle;
    }

    public Long sumAccountAmountsByPassport(String passport) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Long result = null;
        try  {
            session.beginTransaction();

            String hql = "SELECT SUM(a.amount) FROM Account a WHERE a.passport = :passport";
            Query<Long> query = session.createQuery(hql);
            query.setParameter("passport", passport);
            result = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().commit();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result != null ? result : 0L;
    }


    private void deleteAccount(Account account) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Delete the account from the database
            session.delete(account);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Get the account info string for display
    private String getAccountInfo(Account account) {
        return "Account Number: " + account.getAccountNumber() +
                ", Bank: " + account.getBankName() +
                ", Type: " + account.getAType() +
                ", Amount: " + account.getAmount() +
                ", Currency: " + account.getACurrencyId();
    }

    // Get user accounts based on passport
    private List<Account> getUserAccountsByPassport(String passport) {
        List<Account> accounts = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql = "FROM Account a WHERE a.passport = :passport";
            Query<Account> query = session.createQuery(hql);
            query.setParameter("passport", passport);

            accounts = query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return accounts;
    }


    private void createAccountDialog() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ProfilePage.this);
        JDialog dialog = new JDialog(frame, "Create Account", true);
        dialog.setLayout(new FlowLayout());

        JLabel bankLabel = new JLabel("Bank:");
        JComboBox<String> bankComboBox = new JComboBox<>(new String[]{"vtb", "alpha", "sber"});
        dialog.add(bankLabel);
        dialog.add(bankComboBox);

        JLabel typeLabel = new JLabel("Account Type:");
        JTextField typeTextField = new JTextField(10);
        dialog.add(typeLabel);
        dialog.add(typeTextField);

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountTextField = new JTextField(10);
        dialog.add(amountLabel);
        dialog.add(amountTextField);

        JLabel currencyLabel = new JLabel("Currency:");
        JComboBox<String> currencyComboBox = new JComboBox<>(new String[]{"usd", "rub", "GBP"});
        dialog.add(currencyLabel);
        dialog.add(currencyComboBox);

        Map<String, Integer> currencyIdMap = new HashMap<>();
        currencyIdMap.put("usd", 1);
        currencyIdMap.put("rub", 2);
        currencyIdMap.put("GBP", 3);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle create button click here
                String bank = (String) bankComboBox.getSelectedItem();
                String accountType = typeTextField.getText();
                int amount = Integer.parseInt(amountTextField.getText());
                String currency = (String) currencyComboBox.getSelectedItem();
                Long currencyId = Long.valueOf(currencyIdMap.get(currency));

                // Save the account data to the database
                saveAccountData(bank, accountType, amount, currencyId);

                // Close the dialog
                dialog.dispose();
            }
        });
        dialog.add(createButton);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public String getUserPassportByEmail(String email) {
        String passport = null;

        // Получение сессии Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Создание запроса Hibernate для получения паспорта пользователя по email
            String hql = "SELECT u.passport FROM User u WHERE u.email = :email";
            Query<String> query = session.createQuery(hql);
            query.setParameter("email", email);

            // Выполнение запроса и получение результата
            passport = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return passport;
    }

    public String getUserNameByEmail(String email) {
        String name = null;


        // Получение сессии Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Создание запроса Hibernate для получения паспорта пользователя по email
            String hql = "SELECT u.firstName FROM User u WHERE u.email = :email";
            Query<String> query = session.createQuery(hql);
            query.setParameter("email", email);

            // Выполнение запроса и получение результата
            name = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return name;
    }

    public String getUserSurnameByEmail(String email) {
        String surname = null;


        // Получение сессии Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Создание запроса Hibernate для получения паспорта пользователя по email
            String hql = "SELECT u.lastName FROM User u WHERE u.email = :email";
            Query<String> query = session.createQuery(hql);
            query.setParameter("email", email);

            // Выполнение запроса и получение результата
            surname = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return surname;
    }
    private void saveAccountData(String bank, String accountType, int amount, Long currency) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        profilePassport = getUserPassportByEmail(AuthPage.userEmail);
        String accountNumber = Long.toString(Instant.now().toEpochMilli());

        try {
            transaction = session.beginTransaction();

            // Create a new Account object
            Account account = new Account();
            account.setAccountNumber(accountNumber);
            account.setBankName(bank);
            account.setAType(accountType);
            account.setAmount(Long.valueOf(amount));
            account.setACurrencyId(currency);
            account.setPassport(profilePassport);

            // Save the account to the database
            session.save(account);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Profile Page");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);

                ProfilePage profilePage = new ProfilePage();
                frame.setContentPane(profilePage);

                frame.setVisible(true);
            }
        });
    }
}
