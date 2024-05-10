import javax.swing.*;
import java.awt.*;

public class AddAccountPanel extends JPanel {
    private final UserAccount userAccount;
    private JLabel serviceNameLabel = null;
    private JLabel accountNicknameLabel = null;
    private JLabel usernameLabel = null;
    private JLabel passwordLabel = null;
    private JLabel siteURLlabel = null;
    private JTextField serviceNameTextField = null;
    private JTextField accountNicknameTextField = null;
    private JTextField usernameTextField = null;
    //use of a text field instead of password field since the user is already logged in
    private JTextField passwordTextField = null;
    private JTextField siteURLTextField = null;

    public AddAccountPanel(UserAccount userAccount, AddAccountPage addAccountPage) {
        this.userAccount = userAccount;
        setLayout(new GridLayout(5, 2));
        //init top labels
        serviceNameLabel = new JLabel("Service Name");
        accountNicknameLabel = new JLabel("Account Nickname");
        usernameLabel = new JLabel("Usernmame");
        passwordLabel = new JLabel("Password");
        siteURLlabel = new JLabel("Site URL");
        //init bottoms fields
        serviceNameTextField = new JTextField();
        accountNicknameTextField = new JTextField();
        usernameTextField = new JTextField();
        passwordTextField = new JTextField();
        siteURLTextField = new JTextField();
        //set sizes
        serviceNameTextField.setPreferredSize(new Dimension(216,30));
        accountNicknameTextField.setPreferredSize(new Dimension(216,30));
        usernameTextField.setPreferredSize(new Dimension(216,30));
        passwordTextField.setPreferredSize(new Dimension(216,30));
        siteURLTextField.setPreferredSize(new Dimension(216,30));
        //add action listeners
        serviceNameTextField.addActionListener(addAccountPage);
        accountNicknameTextField.addActionListener(addAccountPage);
        usernameTextField.addActionListener(addAccountPage);
        passwordTextField.addActionListener(addAccountPage);
        siteURLTextField.addActionListener(addAccountPage);
        //add everything
        add(serviceNameLabel);
        add(serviceNameTextField);
        add(accountNicknameLabel);
        add(accountNicknameTextField);
        add(usernameLabel);
        add(usernameTextField);
        add(passwordLabel);
        add(passwordTextField);
        add(siteURLlabel);
        add(siteURLTextField);
    }
    public String serializeNewAccountData(){
        StringBuilder sb = new StringBuilder();
        sb.append("service_name=");
        if(serviceNameTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(serviceNameTextField.getText());
        }
        sb.append("&account_nickname=");
        if(accountNicknameTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(accountNicknameTextField.getText());
        }
        sb.append("&username=");
        if(usernameTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(usernameTextField.getText());
        }
        sb.append("&password=");
        if(passwordTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(passwordTextField.getText());
        }
        sb.append("&site_url=");
        if(siteURLTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(siteURLTextField.getText());
        }
        sb.append("&account_id=").append(userAccount.getID());
        return sb.toString();
    }
    public Boolean checkUsername() {
        if(usernameTextField.getText().isEmpty()) {
            return false;
        }
        return true;
    }
    public Boolean checkPassword() {
        if(passwordTextField.getText().isEmpty()) {
            return false;
        }
        return true;
    }
}
