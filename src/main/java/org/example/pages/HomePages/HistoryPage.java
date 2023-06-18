package org.example.pages.HomePages;

import org.example.HibernateUtil;
import org.example.entity.*;
import org.example.pages.HomePages.ProfilePage;
import org.example.pages.HomePages.TransactionPage;
import org.example.pages.LoginPages.AuthPage;
import org.example.pages.LoginPages.RegistrationPage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;
import java.util.List;

public class HistoryPage extends JPanel {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem profileMenuItem;
    private JMenuItem transactionMenuItem;
    private JTabbedPane tabbedPane;
    private JComboBox<String> accountComboBox;
    private JPanel incomesPanel;
    private JPanel sentPanel;
    private JPanel transfersPanel;
    private String historyPassport = RegistrationPage.userPassport;

    private ProfilePage profilePage;
    private TransactionPage transactionPage;

    public HistoryPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Set background color

        // Create the menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE); // Set background color

        // Create the file menu
        fileMenu = new JMenu("Menu");
        fileMenu.setForeground(Color.BLUE); // Set text color

        // Create the profile menu item
        profileMenuItem = new JMenuItem("Profile");
        profileMenuItem.setForeground(Color.BLUE); // Set text color
        profileMenuItem.addActionListener(e -> {
            profilePage = new ProfilePage();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(HistoryPage.this);
            frame.setContentPane(profilePage);
            frame.revalidate();
        });

        transactionMenuItem = new JMenuItem("Transaction");
        transactionMenuItem.setForeground(Color.BLUE); // Set text color
        transactionMenuItem.addActionListener(e -> {
            transactionPage = new TransactionPage();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(HistoryPage.this);
            frame.setContentPane(transactionPage);
            frame.revalidate();
        });

        // Add the profile menu item to the file menu
        fileMenu.add(transactionMenuItem);
        fileMenu.add(profileMenuItem);

        // Add the file menu to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar for the panel
        add(menuBar, BorderLayout.NORTH);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE); // Set background color
        tabbedPane.setForeground(Color.BLUE); // Set text color

        // Create the panel for incomes from others
        incomesPanel = new JPanel(new BorderLayout());
        incomesPanel.setBackground(Color.WHITE); // Set background color

        historyPassport = getUserPassportByEmail(AuthPage.userEmail);
        List<Account> myAccounts = getUserAccountsByPassport(historyPassport);

        // Create the account dropdown outside the tabbed pane
        JLabel accountLabel = new JLabel("Account:");
        accountLabel.setForeground(Color.BLUE); // Set text color
        accountComboBox = new JComboBox<>();
        accountComboBox.setBackground(Color.WHITE); // Set background color
        accountComboBox.setForeground(Color.BLUE); // Set text color
        // Add the accounts to the combo box
        for (Account account : myAccounts) {
            accountComboBox.addItem(account.getAccountNumber());
        }
        // Add action listener to the combo box
        accountComboBox.addActionListener(e -> {
            String selectedAccount = (String) accountComboBox.getSelectedItem();
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case 0: // Incomes from Others
                    List<TransfersIn> res1 = getUserTransfersInByAccount(selectedAccount);
                    displayTransfersIn(res1);
                    break;
                case 1: // Money Sent to Others
                    List<TransfersOut> res2 = getUserTransfersOutByAccount(selectedAccount);
                    displayTransfersOut(res2);
                    break;
                case 2: // Transfers between Accounts
                    List<TransfersBetween> res3 = getUserTransfersBetweenByAccount(selectedAccount);
                    displayTransfersBetween(res3);
                    break;
            }
        });

        JPanel accountPanel = new JPanel();
        accountPanel.setBackground(Color.WHITE); // Set background color
        accountPanel.add(accountLabel);
        accountPanel.add(accountComboBox);

        // Add the account panel to the panel
        add(accountPanel, BorderLayout.SOUTH);

        // Create the panel for incomes from others
        incomesPanel = new JPanel();
        incomesPanel.setBackground(Color.WHITE); // Set background color
        JScrollPane incomesScrollPane = new JScrollPane(incomesPanel); // Create a scroll pane for incomesPanel
        incomesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scrolling
        tabbedPane.addTab("Incomes from Others", incomesScrollPane);

        // Create the panel for money sent to others
        sentPanel = new JPanel();
        sentPanel.setBackground(Color.WHITE); // Set background color
        JScrollPane sentScrollPane = new JScrollPane(sentPanel); // Create a scroll pane for sentPanel
        sentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scrolling
        tabbedPane.addTab("Money Sent to Others", sentScrollPane);

        // Create the panel for transfers between accounts
        transfersPanel = new JPanel();
        transfersPanel.setBackground(Color.WHITE); // Set background color
        JScrollPane transfersScrollPane = new JScrollPane(transfersPanel); // Create a scroll pane for transfersPanel
        transfersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scrolling
        tabbedPane.addTab("Transfers between Accounts", transfersScrollPane);

        // Add the tabbed pane to the center of the panel
        add(tabbedPane, BorderLayout.CENTER);
    }



    public void clearPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    private void displayTransfersIn(List<TransfersIn> transfersIn) {
        clearPanel(incomesPanel);

        for (TransfersIn transfer : transfersIn) {
            displayTransaction(incomesPanel, "TransfersIn", transfer.getTransferInId(), transfer.getDate(), transfer.getAmount());
        }
        revalidate();
    }

    private void displayTransfersOut(List<TransfersOut> transfersOut) {
        System.out.println("Number of transfersOut: " + transfersOut.size()); // Debug statement

        clearPanel(sentPanel);
        for (TransfersOut transfer : transfersOut) {
            displayTransaction(sentPanel, "TransfersOut", transfer.getTransferOutId(), transfer.getDate(), transfer.getAmount());
        }
        revalidate();
    }


    private void displayTransfersBetween(List<TransfersBetween> transfersBetween) {
        clearPanel(transfersPanel);
        for (TransfersBetween transfer : transfersBetween) {
            displayTransaction(transfersPanel, "TransfersBetween", transfer.getTransferBetweenId(), transfer.getDate(), transfer.getAmount());
        }
        revalidate();
    }

    private void displayTransaction(JPanel panel, String transactionType, long transactionId, OffsetDateTime date, long amount) {
        JPanel transactionPanel = new JPanel();
        transactionPanel.setBackground(Color.WHITE); // Set background color
        transactionPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1)); // Set border color and width
        transactionPanel.setLayout(new BorderLayout()); // Use BorderLayout for better organization

        // Create labels for the transaction details
        JLabel typeLabel = new JLabel(transactionType);
        JLabel idLabel = new JLabel("ID: " + transactionId);
        JLabel dateLabel = new JLabel("Date: " + date);
        JLabel amountLabel = new JLabel("Amount: " + amount);

        // Create a sub-panel for the labels
        JPanel labelsPanel = new JPanel();
        labelsPanel.setBackground(Color.WHITE); // Set background color
        labelsPanel.setLayout(new GridLayout(4, 1, 0, 5)); // Use GridLayout to arrange labels vertically with spacing

        // Add labels to the labels panel
        labelsPanel.add(typeLabel);
        labelsPanel.add(idLabel);
        labelsPanel.add(dateLabel);
        labelsPanel.add(amountLabel);

        // Add the labels panel to the transaction panel
        transactionPanel.add(labelsPanel, BorderLayout.CENTER);

        // Add the transaction panel to the main panel
        panel.add(transactionPanel);
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

    public String getUserPassportByEmail(String email) {
        String passport = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql = "SELECT u.passport FROM User u WHERE u.email = :email";
            Query<String> query = session.createQuery(hql);
            query.setParameter("email", email);

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

    private List<TransfersIn> getUserTransfersInByAccount(String accountNum) {
        List<TransfersIn> transfersIn = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql = "FROM TransfersIn ti WHERE ti.accountNumber = :accountNumber";
            Query<TransfersIn> query = session.createQuery(hql);
            query.setParameter("accountNumber", accountNum);

            transfersIn = query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return transfersIn;
    }

    private List<TransfersOut> getUserTransfersOutByAccount(String accountNum) {
        List<TransfersOut> transfersOut = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql2 = "FROM TransfersOut to WHERE to.accountNumber = :accountNumber";
            Query<TransfersOut> query = session.createQuery(hql2);
            query.setParameter("accountNumber", accountNum);

            transfersOut = query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return transfersOut;
    }

    private List<TransfersBetween> getUserTransfersBetweenByAccount(String accountNum) {
        List<TransfersBetween> transfersBetween = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            String hql3 = "FROM TransfersBetween tb WHERE tb.setterNumber = :accountNumber OR tb.getterAccount = :accountNumber";
            Query<TransfersBetween> query = session.createQuery(hql3);
            query.setParameter("accountNumber", accountNum);

            transfersBetween = query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return transfersBetween;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("History Page");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setLocationRelativeTo(null);

                HistoryPage historyPage = new HistoryPage();
                frame.setContentPane(historyPage);
                frame.setVisible(true);
            }
        });
    }
}
