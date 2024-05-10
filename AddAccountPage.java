import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class AddAccountPage extends JFrame implements ActionListener {
    private final HttpConnectionManager connectionManager;
    private final UserAccount userAccount;
    private final ResultsPanel resultsPanel;
    private JPanel topPanel = null;
    private JPanel bottomPanel = null;
    private JLabel userLabel = null;
    private JLabel userAccountLabel = null;
    private AddAccountPanel addAccountPanel = null;
    private JButton submitButton = null;
    private JButton backToMainPageButton = null;

    public AddAccountPage(HttpConnectionManager connectionManager, UserAccount userAccount, ResultsPanel resultsPanel){
        super("Add Account");
        this.connectionManager = connectionManager;
        this.userAccount = userAccount;
        this.resultsPanel = resultsPanel;
        setLayout(new BorderLayout());
        makeTopPanel();
        makeBottomPanel();
        addAccountPanel = new AddAccountPanel(userAccount, this);
        add(topPanel, BorderLayout.NORTH);
        add(addAccountPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(625, 215);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == backToMainPageButton){
            dispose();
            new MainPage(connectionManager, userAccount);
        }
        if(e.getSource() == submitButton || e.getSource().getClass().equals(JTextField.class)){
            if(addAccountPanel.checkUsername() && addAccountPanel.checkPassword()) {
                try {
                    HttpResponse response = connectionManager.sendAddAccountRequest(addAccountPanel.serializeNewAccountData());
                    if(response.statusCode() == 200) {
                        resultsPanel.getTableModel().setRowCount(0);
                        Object[][] data = MainPage.deserialzeMultipleLogins((String)response.body());
                        for(int i = 0; i < data.length; i++){
                            resultsPanel.getTableModel().addRow(data[i]);
                        }
                        resultsPanel.getTableModel().fireTableDataChanged();
                        resultsPanel.resetTable(data);
                    }
                    dispose();
                    new MainPage(connectionManager, userAccount);
                } catch (IOException | InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            } else if (addAccountPanel.checkUsername()) {
                JOptionPane.showMessageDialog(this, "Invalid Account: The Password Field must be filled.");
            } else if (addAccountPanel.checkPassword()) {
                JOptionPane.showMessageDialog(this, "Invalid Account: The Username Field must be filled.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Account: The Username and Password Fields must be filled.");
            }
        }
    }
    private void makeTopPanel(){
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userLabel = new JLabel("Username: ");
        userAccountLabel = new JLabel(userAccount.getUsername());
        topPanel.add(userLabel);
        topPanel.add(userAccountLabel);
    }
    private void makeBottomPanel(){
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        backToMainPageButton = new JButton("Back to Main Page");
        backToMainPageButton.addActionListener(this);
        bottomPanel.add(backToMainPageButton);
        bottomPanel.add(submitButton);
    }
}
