package org.example.pages.HomePages;

import org.example.HibernateUtil;
import org.example.entity.Account;
import org.example.pages.HomePages.HistoryPage;
import org.example.pages.HomePages.TransactionPage;
import org.example.pages.LoginPages.AuthPage;
import org.example.pages.LoginPages.RegistrationPage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel accountLabel;
    private JButton editButton;
    private JButton createAccountButton;
    private String profilePassport = RegistrationPage.userPassport;
    private DefaultListModel<String> accountListModel;
    private JList<String> accountList;

    private TransactionPage transactionPage;
    private HistoryPage historyPage;

    public ProfilePage() {
        setLayout(new BorderLayout());

        // Create the menu bar
        menuBar = new JMenuBar();

        // Create the file menu
        fileMenu = new JMenu("Menu");

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

        fileMenu.add(historyMenuItem);
        // Add the transaction menu item to the file menu
        fileMenu.add(transactionMenuItem);

        // Add the file menu to the menu bar
        menuBar.add(fileMenu);

        // Create the menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.add(menuBar);

        // Add the menu panel to the panel
        add(menuPanel, BorderLayout.NORTH);

        // Create the profile info panel
        JPanel profileInfoPanel = new JPanel(new GridLayout(2, 1));
        String name = getUserNameByEmail(AuthPage.userEmail);
        String surname = getUserSurameByEmail(AuthPage.userEmail);
        nameLabel = new JLabel(name);
        emailLabel = new JLabel(surname);
        profileInfoPanel.add(nameLabel);
        profileInfoPanel.add(emailLabel);

        // Create the accounts panel
        JPanel accountsPanel = new JPanel(new BorderLayout());
        accountListModel = new DefaultListModel<>();
        accountList = new JList<>(accountListModel);

        // Create a scroll pane for the account list
        JScrollPane accountScrollPane = new JScrollPane(accountList);
        accountsPanel.add(accountScrollPane, BorderLayout.CENTER);

        // Create the buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        // Create and add create account button
        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle create account button click here
                createAccountDialog();
            }
        });
        buttonsPanel.add(createAccountButton);

        // Create and add edit profile button
        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle edit profile button click here
                JOptionPane.showMessageDialog(null, "Edit Profile button clicked!");
            }
        });
        buttonsPanel.add(editProfileButton);

        // Create a panel to hold profile info and accounts
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(profileInfoPanel, BorderLayout.NORTH);
        contentPanel.add(accountsPanel, BorderLayout.CENTER);

        // Create a panel to hold the content panel and buttons panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add the main panel to the panel
        add(mainPanel, BorderLayout.CENTER);

        // Load and display user accounts
        loadUserAccounts();
    }




    // Load and display user accounts
    private void loadUserAccounts() {
        profilePassport = getUserPassportByEmail(AuthPage.userEmail);

        // Get user accounts from the database based on the passport
        List<Account> accounts = getUserAccountsByPassport(profilePassport);

        accountListModel.clear(); // Clear the existing account list

        // Add the account details to the account list model
        for (Account account : accounts) {
            String accountInfo = "Account Number: " + account.getAccountNumber() +
                    ", Bank: " + account.getBankName() +
                    ", Type: " + account.getAType() +
                    ", Amount: " + account.getAmount() +
                    ", Currency: " + account.getACurrencyId();
            accountListModel.addElement(accountInfo);
        }
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

    public String getUserSurameByEmail(String email) {
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
