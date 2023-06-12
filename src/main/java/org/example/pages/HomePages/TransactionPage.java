package org.example.pages.HomePages;

import jakarta.persistence.TypedQuery;
import org.example.HibernateUtil;
import org.example.entity.*;
import org.example.pages.LoginPages.AuthPage;
import org.example.pages.LoginPages.RegistrationPage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;
import java.util.List;

public class TransactionPage extends JPanel {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem historyMenuItem;
    private JMenuItem profileMenuItem;
    private JTabbedPane tabbedPane;

    private String transactionPassport = RegistrationPage.userPassport;

    public TransactionPage() {
        setLayout(new BorderLayout());

        // Create the menu bar
        menuBar = new JMenuBar();

        // Create the file menu
        fileMenu = new JMenu("Menu");

        // Create the history menu item
        historyMenuItem = new JMenuItem("History");
        historyMenuItem.addActionListener(e -> {
            // Handle history action here
            HistoryPage historyPage = new HistoryPage();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(TransactionPage.this);
            frame.setContentPane(historyPage);
            frame.revalidate();
        });

        profileMenuItem = new JMenuItem("Profile");
        profileMenuItem.addActionListener(e -> {
            // Handle profile action here
            ProfilePage profilePage = new ProfilePage();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(TransactionPage.this);
            frame.setContentPane(profilePage);
            frame.revalidate();
        });

        // Add the history menu item to the file menu
        fileMenu.add(historyMenuItem);
        fileMenu.add(profileMenuItem);

        // Add the file menu to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar for the panel
        add(menuBar, BorderLayout.NORTH);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Create the "My Accounts" tab
        JPanel myAccountsPanel = new JPanel();
        myAccountsPanel.setLayout(new FlowLayout());
        transactionPassport = getUserPassportByEmail(AuthPage.userEmail);
        List<Account> myAccounts = getUserAccountsByPassport(transactionPassport);
        for (Account account : myAccounts) {
            JPanel accountPanel = createAccountPanel2(account);
            myAccountsPanel.add(accountPanel);
        }
        tabbedPane.addTab("My Accounts", myAccountsPanel);

        // Create the "Other People" tab
        JPanel otherPeoplePanel = new JPanel();
        otherPeoplePanel.setLayout(new FlowLayout());
        List<Account> otherPeopleAccounts = getOtherPeopleAccounts(transactionPassport);
        for (Account account : otherPeopleAccounts) {
            JPanel accountPanel = createAccountPanel(account);
            otherPeoplePanel.add(accountPanel);
        }
        tabbedPane.addTab("Other People", otherPeoplePanel);

        // Add the tabbed pane to the panel
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAccountPanel(Account account) {
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());
        accountPanel.setPreferredSize(new Dimension(150, 100));
        accountPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Get account owner's initials
        User accountOwner = getUserByPassport(account.getPassport());
        String ownerInitials = getOwnerInitials(accountOwner);
        String ownerAccountNumber = getOwnerAccountNumber(account);

        // Create label for owner's initials
        JLabel ownerInitialsLabel = new JLabel(ownerInitials);
        ownerInitialsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        ownerInitialsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountPanel.add(ownerInitialsLabel, BorderLayout.CENTER);

        JLabel ownerAccountNumberLabel = new JLabel(ownerAccountNumber);
        ownerAccountNumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        ownerAccountNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountPanel.add(ownerAccountNumberLabel, BorderLayout.CENTER);

        // Create label for amount of money
        JLabel amountLabel = new JLabel("Amount: $" + account.getAmount());
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountPanel.add(amountLabel, BorderLayout.SOUTH);

        // Create transfer button
        JButton transferButton = new JButton("Transfer Money");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle transfer money action here
                String transferToAccountNumber = account.getAccountNumber();
                showTransferWindow(transferToAccountNumber);
            }
        });
        accountPanel.add(transferButton, BorderLayout.NORTH);

        return accountPanel;
    }

    private void showTransferWindow(String transferToAccountNumber) {
        JFrame transferFrame = new JFrame("Transfer Money");
        transferFrame.setSize(300, 200);
        transferFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        transferFrame.setLocationRelativeTo(null);

        JPanel transferPanel = new JPanel();
        transferPanel.setLayout(new GridLayout(3, 2));

        // Create label and text field for account owner's initials
//        JLabel ownerInitialsLabel = new JLabel("Account Owner Initials:");
//        JTextField ownerInitialsField = new JTextField();
//        ownerInitialsField.setEditable(false);
//        transferPanel.add(ownerInitialsLabel);
//        transferPanel.add(ownerInitialsField);

        transactionPassport = getUserPassportByEmail(AuthPage.userEmail);
        List<Account> myAccounts = getUserAccountsByPassport(transactionPassport);
        JLabel senderAccountLabel = new JLabel("Account:");
        JComboBox<String> senderAccountComboBox = new JComboBox<>();
        for (Account account : myAccounts) {
            senderAccountComboBox.addItem(account.getAccountNumber());
        }
        transferPanel.add(senderAccountLabel);
        transferPanel.add(senderAccountComboBox);

        // Create label and text field for transfer amount
        JLabel transferAmountLabel = new JLabel("Transfer Amount:");
        JTextField transferAmountField = new JTextField();
        transferPanel.add(transferAmountLabel);
        transferPanel.add(transferAmountField);

        // Create transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle transfer action here
                //String ownerInitials = ownerInitialsField.getText();
                String transferAmountText = transferAmountField.getText();
                String tSenderAccount = (String) senderAccountComboBox.getSelectedItem();
                String senderBank = getBankNameByAccount(tSenderAccount);
                String getterBank = getBankNameByAccount(transferToAccountNumber);
                OffsetDateTime transactionOutDate = OffsetDateTime.now();
                if (!tSenderAccount.isEmpty() && !transferAmountText.isEmpty()) {
                    try {
                        long transferAmount = Long.parseLong(transferAmountText);
                        if (transferAmount > 0) {
                            // Add transaction to transfers_out table
                            addTransactionToTransfersOut(tSenderAccount, transferToAccountNumber, transferAmount, transactionOutDate, senderBank, getterBank);
                            addTransactionToTransfersIn(tSenderAccount, transferToAccountNumber, transferAmount, transactionOutDate, senderBank, getterBank);
                            // Update account balances
                            updateAccountBalances(transferToAccountNumber, tSenderAccount, transferAmount);

                            // Close the transfer window
                            transferFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(transferFrame, "Invalid transfer amount. Please enter a positive number.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(transferFrame, "Invalid transfer amount. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(transferFrame, "Please fill in all fields.");
                }
            }
        });
        transferPanel.add(new JLabel());
        transferPanel.add(transferButton);

        transferFrame.setContentPane(transferPanel);
        transferFrame.setVisible(true);
    }

    private JPanel createAccountPanel2(Account account) {
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());
        accountPanel.setPreferredSize(new Dimension(150, 100));
        accountPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Get account owner's initials
        User accountOwner = getUserByPassport(account.getPassport());
        String ownerInitials = getOwnerInitials(accountOwner);
        String ownerAccountNumber = getOwnerAccountNumber(account);

        // Create label for owner's initials
        JLabel ownerInitialsLabel = new JLabel(ownerInitials);
        ownerInitialsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        ownerInitialsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountPanel.add(ownerInitialsLabel, BorderLayout.CENTER);

        JLabel ownerAccountNumberLabel = new JLabel(ownerAccountNumber);
        ownerAccountNumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        ownerAccountNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountPanel.add(ownerAccountNumberLabel, BorderLayout.CENTER);

        // Create label for amount of money
        JLabel amountLabel = new JLabel("Amount: $" + account.getAmount());
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountPanel.add(amountLabel, BorderLayout.SOUTH);

        // Create transfer button
        JButton transferButton = new JButton("Transfer Money");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle transfer money action here
                String transferToAccountNumber = account.getAccountNumber();
                showTransferWindow2(transferToAccountNumber);
            }
        });
        accountPanel.add(transferButton, BorderLayout.NORTH);

        return accountPanel;
    }

    private void showTransferWindow2(String transferToAccountNumber) {
        JFrame transferFrame = new JFrame("Transfer Money");
        transferFrame.setSize(300, 200);
        transferFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        transferFrame.setLocationRelativeTo(null);

        JPanel transferPanel = new JPanel();
        transferPanel.setLayout(new GridLayout(3, 2));

        // Create label and text field for account owner's initials
//        JLabel ownerInitialsLabel = new JLabel("Account Owner Initials:");
//        JTextField ownerInitialsField = new JTextField();
//        ownerInitialsField.setEditable(false);
//        transferPanel.add(ownerInitialsLabel);
//        transferPanel.add(ownerInitialsField);

        transactionPassport = getUserPassportByEmail(AuthPage.userEmail);
        List<Account> myAccounts = getUserAccountsByPassport(transactionPassport);
        JLabel senderAccountLabel = new JLabel("Account:");
        JComboBox<String> senderAccountComboBox = new JComboBox<>();
        for (Account account : myAccounts) {
            senderAccountComboBox.addItem(account.getAccountNumber());
        }
        transferPanel.add(senderAccountLabel);
        transferPanel.add(senderAccountComboBox);

        // Create label and text field for transfer amount
        JLabel transferAmountLabel = new JLabel("Transfer Amount:");
        JTextField transferAmountField = new JTextField();
        transferPanel.add(transferAmountLabel);
        transferPanel.add(transferAmountField);

        // Create transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle transfer action here
                //String ownerInitials = ownerInitialsField.getText();
                String transferAmountText = transferAmountField.getText();
                String tSenderAccount = (String) senderAccountComboBox.getSelectedItem();
                String senderBank = getBankNameByAccount(tSenderAccount);
                String getterBank = getBankNameByAccount(transferToAccountNumber);
                OffsetDateTime transactionOutDate = OffsetDateTime.now();
                if (!tSenderAccount.isEmpty() && !transferAmountText.isEmpty()) {
                    try {
                        long transferAmount = Long.parseLong(transferAmountText);
                        if (transferAmount > 0) {
                            // Add transaction to transfers_out table
                            addTransactionToTransfersBetween(tSenderAccount, transferToAccountNumber, transferAmount, transactionOutDate, senderBank, getterBank);

                            // Update account balances
                            updateAccountBalances(transferToAccountNumber, tSenderAccount, transferAmount);

                            // Close the transfer window
                            transferFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(transferFrame, "Invalid transfer amount. Please enter a positive number.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(transferFrame, "Invalid transfer amount. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(transferFrame, "Please fill in all fields.");
                }
            }
        });
        transferPanel.add(new JLabel());
        transferPanel.add(transferButton);

        transferFrame.setContentPane(transferPanel);
        transferFrame.setVisible(true);
    }

    public String getBankNameByAccountNumber( String accountNumber) {

        return null;  // Account number not found
    }

    private void addTransactionToTransfersBetween(String senderAccount, String getterAccount, long transferAmount, OffsetDateTime transactionDate, String senderBank, String getterBank) {
        // Obtain Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Get the user's passport by initials

            if (getterAccount != null) {
                // Create a new transaction
                TransfersBetween transaction = new TransfersBetween();
                transaction.setGetterAccount(getterAccount);
                transaction.setSetterNumber(senderAccount);
                transaction.setDate(transactionDate);
                transaction.setAmount(transferAmount);

                // Save the transaction to transfers_out table
                session.save(transaction);
                session.getTransaction().commit();
            } else {
                JOptionPane.showMessageDialog(null, "Account owner not found.");
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    private void addTransactionToTransfersOut(String senderAccount, String getterAccount, long transferAmount, OffsetDateTime transactionDate, String senderBank, String getterBank) {
        // Obtain Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Get the user's passport by initials

            if (getterAccount != null) {
                // Create a new transaction
                TransfersOut transaction = new TransfersOut();
                transaction.setAccountNumber(senderAccount);
                transaction.setGetterAcount(getterAccount);
                transaction.setDate(transactionDate);
                transaction.setAmount(transferAmount);

                // Save the transaction to transfers_out table
                session.save(transaction);
                session.getTransaction().commit();
            } else {
                JOptionPane.showMessageDialog(null, "Account owner not found.");
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void addTransactionToTransfersIn(String senderAccount, String getterAccount, long transferAmount, OffsetDateTime transactionDate, String senderBank, String getterBank) {
        // Obtain Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Get the user's passport by initials

            if (getterAccount != null) {
                // Create a new transaction
                TransfersIn transaction = new TransfersIn();
                transaction.setAccountNumber(getterAccount);
                transaction.setSenderAccount(senderAccount);
                transaction.setDate(transactionDate);
                transaction.setAmount(transferAmount);

                // Save the transaction to transfers_out table
                session.save(transaction);
                session.getTransaction().commit();
            } else {
                JOptionPane.showMessageDialog(null, "Account owner not found.");
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private String getBankNameByAccount(String accountNumber) {
        String bankName = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            String hql = "SELECT a.bankName FROM Account a WHERE a.accountNumber = :accountNumber";
            TypedQuery<String> query = session.createQuery(hql, String.class);
            query.setParameter("accountNumber", accountNumber);

            bankName = query.getSingleResult();
            System.out.println(bankName);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bankName;
    }


    private void updateAccountBalances(String getterAccount, String senderAccount, double transferAmount) {
        // Obtain Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Update the account balances
            String updateTransferToAccount = "UPDATE Account SET amount = amount + :transferAmount WHERE accountNumber = :accountNumber";
            String updateTransferFromAccount = "UPDATE Account SET amount = amount - :transferAmount WHERE accountNumber = :accountNumber";

            Query<?> queryTransferToAccount = session.createQuery(updateTransferToAccount);
            queryTransferToAccount.setParameter("transferAmount", transferAmount);
            queryTransferToAccount.setParameter("accountNumber", getterAccount);
            queryTransferToAccount.executeUpdate();

            Query<?> queryTransferFromAccount = session.createQuery(updateTransferFromAccount);
            queryTransferFromAccount.setParameter("transferAmount", transferAmount);
            queryTransferFromAccount.setParameter("accountNumber", senderAccount);
            queryTransferFromAccount.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private String getUserPassportByInitials(String initials) {
        String passport = null;

        // Obtain Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Create Hibernate query to get user's passport by initials
            String hql = "SELECT u.passport FROM User u WHERE CONCAT(SUBSTRING(u.firstName, 1, 1), SUBSTRING(u.lastName, 1, 1)) = :initials";
            Query<String> query = session.createQuery(hql);
            query.setParameter("initials", initials);

            // Execute query and get the result
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


    public String getUserPassportByEmail(String email) {
        String passport = null;

        // Obtain Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            // Create Hibernate query to get user's passport by email
            String hql = "SELECT u.passport FROM User u WHERE u.email = :email";
            Query<String> query = session.createQuery(hql);
            query.setParameter("email", email);

            // Execute query and get the result
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

    private List<Account> getOtherPeopleAccounts(String passport) {
        List<Account> accounts = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql = "FROM Account a WHERE a.passport != :passport";
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

    private User getUserByPassport(String passport) {
        User user = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql = "FROM User u WHERE u.passport = :passport";
            Query<User> query = session.createQuery(hql);
            query.setParameter("passport", passport);

            user = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return user;
    }

    private String getOwnerInitials(User user) {
        if (user == null) {
            return "";
        }

        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            return "";
        }

        String initials = firstName.substring(0, 1) + lastName.substring(0, 1);
        return initials.toUpperCase();
    }

    private String getOwnerAccountNumber(Account account) {
        if (account == null) {
            return "";
        }

        String tAccount = account.getAccountNumber();

        return tAccount;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Transaction Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            TransactionPage transactionPage = new TransactionPage();
            frame.setContentPane(transactionPage);

            frame.setVisible(true);
        });
    }
}
