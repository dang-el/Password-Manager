import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class EditPage extends JFrame implements ActionListener {
    private final HttpConnectionManager connection;
    private JPanel titlePanel = null;
    private JLabel titleLabel = null;
    private JPanel editPanel = null; //6x2
    private JPanel editServiceNamePanel = null;
    private JLabel editServiceNameLabel = null;
    private JTextField editServiceNameTextField = null;
    private JPanel editAccountNicknamePanel = null;
    private JLabel editAccountNicknameLabel = null;
    private JTextField editAccountNicknameTextField = null;
    private JPanel editSiteURLPanel = null;
    private JLabel editSiteURLLabel = null;
    private JTextField editSiteURLTextField = null;
    private JPanel editUsernamePanel = null;
    private JLabel editUsernameLabel = null;
    private JTextField editUsernameTextField = null;
    private JPanel editPasswordPanel = null;
    private JLabel editPasswordLabel = null;
    private JTextField editPasswordTextField = null;
    private JPanel deleteAccountPanel = null;
    private JButton deleteAccountButton = null;
    private JButton backToMainPageButton = null;
    private JButton editAccountButton = null;
    private UserAccount userAccount = null;
    private String serializedRowData = null;

    public EditPage(HttpConnectionManager conn, UserAccount userAccount, String rowData) {
        this.userAccount = userAccount;
        connection = conn;
        serializedRowData = rowData;
        makeWindow();
    }
    private void makeWindow() {
        setLayout(new BorderLayout());
        createTitlePanel();
        createEditPanel();
        add(titlePanel);
        add(editPanel);
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private void createTitlePanel() {
        titlePanel = new JPanel(new FlowLayout());
        titleLabel = new JLabel("Edit Account Details: ");
        titleLabel.setVisible(true);
        titleLabel.setPreferredSize(new Dimension(216,30));
        titlePanel.add(new JLabel(), FlowLayout.LEFT);
        titlePanel.add(titleLabel, FlowLayout.CENTER);
        titlePanel.add(new JLabel(), FlowLayout.RIGHT);
    }
    private void createEditPanel() {
        editPanel = new JPanel(new GridLayout(6,2));
        String[] rowData = deserialzeRowData(serializedRowData);
        editServiceNamePanel = new JPanel(new GridLayout(1,2));
        editServiceNameLabel = new JLabel("Service Name");
        editServiceNameLabel.setVisible(true);
        editServiceNameLabel.setPreferredSize(new Dimension(216,30));
        editServiceNameTextField = new JTextField(rowData[0]);
        editServiceNameTextField.addActionListener(this);
        editServiceNameTextField.setEditable(true);
        editServiceNameTextField.setVisible(true);
        editServiceNameTextField.setPreferredSize(new Dimension(216,30));
        editServiceNamePanel.add(editServiceNameLabel);
        editServiceNamePanel.add(editServiceNameTextField);
        editAccountNicknamePanel = new JPanel(new GridLayout(1,2));
        editAccountNicknameLabel = new JLabel("Account Nickname");
        editAccountNicknameLabel.setVisible(true);
        editAccountNicknameLabel.setPreferredSize(new Dimension(216,30));
        editAccountNicknameTextField = new JTextField(rowData[1]);
        editAccountNicknameTextField.addActionListener(this);
        editAccountNicknameTextField.setEditable(true);
        editAccountNicknameTextField.setVisible(true);
        editAccountNicknameTextField.setPreferredSize(new Dimension(216,30));
        editAccountNicknamePanel.add(editAccountNicknameLabel);
        editAccountNicknamePanel.add(editAccountNicknameTextField);
        editUsernamePanel = new JPanel(new GridLayout(1,2));
        editUsernameLabel = new JLabel("Edit Username");
        editUsernameLabel.setVisible(true);
        editUsernameLabel.setPreferredSize(new Dimension(216,30));
        editUsernameTextField = new JTextField(rowData[2]);
        editUsernameTextField.addActionListener(this);
        editUsernameTextField.setEditable(true);
        editUsernameTextField.setVisible(true);
        editUsernameTextField.setPreferredSize(new Dimension(216,30));
        editUsernamePanel.add(editUsernameLabel);
        editUsernamePanel.add(editUsernameTextField);
        editPasswordPanel = new JPanel(new GridLayout(1,2));
        editPasswordLabel = new JLabel("Edit Password");
        editPasswordLabel.setVisible(true);
        editPasswordLabel.setPreferredSize(new Dimension(216, 30));
        editPasswordTextField = new JTextField(rowData[3]);
        editPasswordTextField.addActionListener(this);
        editPasswordTextField.setEditable(true);
        editPasswordTextField.setVisible(true);
        editPasswordTextField.setPreferredSize(new Dimension(216,30));
        editPasswordPanel.add(editPasswordLabel);
        editPasswordPanel.add(editPasswordTextField);
        editSiteURLPanel = new JPanel(new GridLayout(1,2));
        editSiteURLLabel = new JLabel("Edit Site URL");
        editSiteURLLabel.setVisible(true);
        editSiteURLLabel.setPreferredSize(new Dimension(216,30));
        editSiteURLTextField = new JTextField(rowData[4]);
        editSiteURLTextField.addActionListener(this);
        editSiteURLTextField.setEditable(true);
        editSiteURLTextField.setVisible(true);
        editSiteURLTextField.setPreferredSize(new Dimension(216,30));
        editSiteURLPanel.add(editSiteURLLabel);
        editSiteURLPanel.add(editSiteURLTextField);
        deleteAccountPanel = new JPanel(new FlowLayout());
        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(this);
        deleteAccountButton.setOpaque(true);
        deleteAccountButton.setVisible(true);
        deleteAccountButton.setPreferredSize(new Dimension(216, 30));
        backToMainPageButton = new JButton("Back to Main Page");
        backToMainPageButton.addActionListener(this);
        backToMainPageButton.setOpaque(true);
        backToMainPageButton.setVisible(true);
        backToMainPageButton.setPreferredSize(new Dimension(216,30));
        editAccountButton = new JButton("Edit Account");
        editAccountButton.addActionListener(this);
        editAccountButton.setOpaque(true);
        editAccountButton.setVisible(true);
        editAccountButton.setPreferredSize(new Dimension(216,30));
        deleteAccountPanel.add(deleteAccountButton);
        deleteAccountPanel.add(backToMainPageButton);
        deleteAccountPanel.add(editAccountButton);
        editPanel.add(editServiceNamePanel);
        editPanel.add(editAccountNicknamePanel);
        editPanel.add(editUsernamePanel);
        editPanel.add(editPasswordPanel);
        editPanel.add(editSiteURLPanel);
        editPanel.add(deleteAccountPanel);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getSource() == backToMainPageButton){
            dispose();
            new MainPage(connection, userAccount);
        } else if (e.getSource() == deleteAccountButton) {
            String confirmPassword = JOptionPane.showInputDialog(this, "Confirm Delete: Please enter your password.");
            if (!userAccount.getPassword().equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect Password, try again.");
                dispose();
                new MainPage(connection, userAccount);
            } else if (userAccount.getPassword().equals(confirmPassword)) {
                //send a delete request
                try {
                    HttpResponse<String> response = connection.sendDeleteAccountRequest(serializedRowData); //the row data from the ResultsPanel (serialized)
                    if(response.statusCode() == 200) {
                        JOptionPane.showMessageDialog(this, "Account successfully deleted.");
                    }
                    dispose();
                    new MainPage(connection, userAccount);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == editAccountButton || e.getSource().getClass().equals(JTextField.class)) {
            try {
                HttpResponse<String> response = connection.sendEditAccountRequest(serializeEditData());
                dispose();
                new MainPage(connection, userAccount);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    private String[] deserialzeRowData(String data) {
        String[] splitone = data.split("&");
        String[] attributes = new String[6];
        for(int i = 0; i < splitone.length; i++) {
            attributes[i] = splitone[i].split("=")[1];
        }
        return attributes;
    }
    private String serializeEditData() {
        StringBuilder sb = new StringBuilder();
        sb.append(serializedRowData).append("&new_service_name=").append(editServiceNameTextField.getText())
                .append("&new_account_nickname=").append(editAccountNicknameTextField.getText())
                .append("&new_username=").append(editUsernameTextField.getText())
                .append("&new_password=").append(editPasswordTextField.getText())
                .append("&new_site_url=").append(editSiteURLTextField.getText());
        return sb.toString();
    }
}