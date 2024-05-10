import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class MainPage extends JFrame implements ActionListener {
    private final HttpConnectionManager connection;
    private String accountUsername = null;
    private String accountPassword = null;
    private JPanel topPanel = null;
    private JLabel accountUsernameLabel = null;
    private JButton logoutButton = null;
    private JPanel contentPanel = null;
    private SearchPanel searchPanel = null;
    private ResultsPanel resultsPanel = null;
    private JPanel bottomPanel = null;
    private JButton searchButton = null;
    private JButton deleteUserButton = null;
    private JButton addAccountButton = null;
    private JLabel numResultsLabel = null;
    private UserAccount userAccount = null;
    private DefaultTableModel tableModel = null;

    public MainPage(HttpConnectionManager conn, UserAccount userAccount) {
        this.userAccount = userAccount;
        connection = conn;
        accountUsername = userAccount.getUsername();
        accountPassword = userAccount.getPassword();
        makeWindow();
    }
    private void makeWindow() {
        setLayout(new BorderLayout());
        setSize(1200,600);
        createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private void createTopPanel() {
        topPanel = new JPanel(new GridLayout(1,2));
        accountUsernameLabel = new JLabel("Username: " + accountUsername);
        accountUsernameLabel.setVisible(true);
        accountUsernameLabel.setPreferredSize(new Dimension(216,30));
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        logoutButton.setOpaque(true);
        logoutButton.setVisible(true);
        logoutButton.setPreferredSize(new Dimension(216,30));
        topPanel.add(accountUsernameLabel, FlowLayout.LEFT);
        topPanel.add(new JLabel(), FlowLayout.CENTER); //fixes small spacing bug (illegal placement error)
        topPanel.add(logoutButton, FlowLayout.RIGHT);
    }
    private void createContentPanel() {
        contentPanel = new JPanel(new GridLayout(1,2));
        resultsPanel = new ResultsPanel(connection, userAccount, this);
        tableModel = resultsPanel.getTableModel();
        searchPanel = new SearchPanel(this);
        contentPanel.add(searchPanel);
        contentPanel.add(resultsPanel);
    }
    private void createBottomPanel() {
        bottomPanel = new JPanel(new GridLayout(1,9));
        searchButton = new JButton("Search");
        //add the actionlistener for the search button which fires the table data change on the results panel
        //this is the connection between the search panel and the result panel
        searchButton.addActionListener(e -> {
            try {
                HttpResponse<String> response = connection.sendSearchRequest(searchPanel.serializeSearchData());
                if(response.statusCode() == 200) {
                    Object[][] data = deserialzeMultipleLogins(response.body());
                    tableModel.setRowCount(0);
                    for(int i = 0; i < data.length; i++) {
                        tableModel.addRow(data[i]);
                    }
                    tableModel.fireTableDataChanged();
                    resultsPanel.resetTable(data);
                } else if (response.statusCode() == 204) {
                    tableModel.setRowCount(0);
                    tableModel.fireTableDataChanged();
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        searchButton.setOpaque(true);
        searchButton.setVisible(true);
        searchButton.setPreferredSize(new Dimension(216,30));
        addAccountButton = new JButton("Add New Account");
        addAccountButton.addActionListener(this);
        addAccountButton.setOpaque(true);
        addAccountButton.setVisible(true);
        deleteUserButton = new JButton("Delete Profile");
        deleteUserButton.addActionListener(this);
        deleteUserButton.setOpaque(true);
        deleteUserButton.setVisible(true);
        addAccountButton.setPreferredSize(new Dimension(216,30));
        numResultsLabel = new JLabel("Results: " + resultsPanel.getNumResults());
        numResultsLabel.setVisible(true);
        numResultsLabel.setPreferredSize(new Dimension(216,30));
        bottomPanel.add(new JLabel());
        bottomPanel.add(searchButton);
        bottomPanel.add(new JLabel());
        bottomPanel.add(addAccountButton);
        bottomPanel.add(new JLabel());
        bottomPanel.add(deleteUserButton);
        bottomPanel.add(new JLabel());
        bottomPanel.add(numResultsLabel);
        bottomPanel.add(new JLabel());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if(e.getSource() == logoutButton){
            dispose();
            new LoginPage(connection);
        } else if(e.getSource() == addAccountButton){
            dispose();
            new AddAccountPage(connection, userAccount, resultsPanel);
        } else if (e.getSource() == searchButton || e.getSource().getClass().equals(JTextField.class)) {
            try {
                HttpResponse<String> response = connection.sendSearchRequest(searchPanel.serializeSearchData());
                if(response.statusCode() == 200 || response.statusCode() == 204) {
                    resultsPanel.getTableModel().setRowCount(0);
                    Object[][] data = deserialzeMultipleLogins(response.body());
                    for(int i = 0; i < data.length; i++){
                        resultsPanel.getTableModel().addRow(data[i]);
                    }
                    resultsPanel.getTableModel().fireTableDataChanged();
                    resultsPanel.resetTable(data);
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == deleteUserButton) {
            String confirmPassword = JOptionPane.showInputDialog(this, "Confirm Delete: Please enter your password.");
            if(!userAccount.getPassword().equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect Password, try again.");
            } else if(userAccount.getPassword().equals(confirmPassword)) {
                try {
                    HttpResponse<String> response = connection.sendDeleteUserAccountRequest("account_id=" + userAccount.getID());
                    if(response.statusCode() == 200) {
                        JOptionPane.showMessageDialog(this, "Account successfully deleted.");
                        dispose();
                        new LoginPage(connection);
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public static Object[][] deserialzeMultipleLogins(String responseBody) {
        if(responseBody.isEmpty()){
            return new Object[5][];
        }
        String[] usersData = responseBody.split(",");
        Object[][] retVal = new Object[usersData.length][];
        for(int i = 0; i < usersData.length; i++)
        {
            String[] userData = usersData[i].split("&");
            Object[] userArray = new Object[5];
            for(int j = 0; j < userData.length; j++)
            {
                String[] userField = userData[j].split("=");
                if(userField[1].equals("*empty*")){userField[1] = "";}
                userArray[j] = userField[1];

            }
            retVal[i] = userArray;
        }
        return retVal;
    }
    public UserAccount getUserAccount() {
        return userAccount;
    }
}