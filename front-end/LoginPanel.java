import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginPanel extends JPanel {
    private LoginPage loginPage = null;
    private JPanel usernameInputPanel = null;
    private JLabel usernameLabel = null;
    private JTextField usernameTextField = null;
    private JPanel passwordInputPanel = null;
    private JLabel passwordLabel = null;
    private JPasswordField passwordTextField = null;

    public LoginPanel(LoginPage lp){
        loginPage = lp;
        setLayout(new GridLayout(2,1));
        createUsernameInputPanel();
        createPasswordInputPanel();
        add(usernameInputPanel);
        add(passwordInputPanel);
        setVisible(true);
    }
    private void createUsernameInputPanel(){
        usernameInputPanel = new JPanel(new FlowLayout());
        usernameLabel = new JLabel("Enter Your Username: ");
        usernameLabel.setVisible(true);
        usernameLabel.setPreferredSize(new Dimension(216, 30));
        usernameTextField = new JTextField();
        usernameTextField.addActionListener(loginPage);
        usernameTextField.setEditable(true);
        usernameTextField.setVisible(true);
        usernameTextField.setPreferredSize(new Dimension(216, 30));
        usernameInputPanel.add(usernameLabel);
        usernameInputPanel.add(usernameTextField);
    }
    private void createPasswordInputPanel(){
        passwordInputPanel = new JPanel(new FlowLayout());
        passwordLabel = new JLabel();
        passwordLabel.setText("Enter your Password: ");
        passwordLabel.setVisible(true);
        passwordLabel.setPreferredSize(new Dimension(216, 30));
        passwordTextField = new JPasswordField();
        passwordTextField.addActionListener(loginPage);
        passwordTextField.setEditable(true);
        passwordTextField.setVisible(true);
        passwordTextField.setPreferredSize(new Dimension(216, 30));
        passwordInputPanel.add(passwordLabel);
        passwordInputPanel.add(passwordTextField);
    }
    private Boolean checkUsername(){
        String username = usernameTextField.getText().toString();
        if(username.length() > 6){
            for(int i = 0; i < username.length(); i++){
                if(username.charAt(i) == ' ' || username.charAt(i) == ','){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    private Boolean checkPassword(){
        Boolean isUpper = false;
        Boolean isLower = false;
        Boolean isNumber = false;
        Boolean isSymbol = false;
        //get text from password field
        String password = SignUpPanel.getPasswordTextFromField(passwordTextField);
        //do some password logic
        if(password.length() >= 8){
            if(password.length()  <= 30){
                for(int i = 0; i < password.length(); i++){
                    Character c = password.charAt(i);
                    if(c.equals(' ') || c.equals(',')){
                        return false;
                    }
                    else if(Character.isDigit(c)){
                        isNumber = true;
                    }
                    else if(Character.isLetter(c)){
                        if(Character.isLowerCase(c)){
                            isLower = true;
                        }
                        else if(Character.isUpperCase(c)){
                            isUpper = true;
                        }
                    }
                    else {
                        isSymbol = true;
                    }
                }
                if (isUpper && isLower && isNumber && isSymbol){
                    return true;

                }
            }
        }
        return false;
    }
    public Boolean checkFields(){
        return checkUsername() && checkPassword();
    }
    private ArrayList<String> getInfo(){
        //username, password
        ArrayList<String> info = new ArrayList<>();
        info.add(usernameTextField.getText());
        info.add(SignUpPanel.getPasswordTextFromField(passwordTextField));
        return info;
    }
    public String serializeLoginInfo(){
        ArrayList<String> info = getInfo();
        StringBuilder sb = new StringBuilder();
        sb.append("username=").append(info.get(0)).append("&password=").append(info.get(1));
        return sb.toString();
    }
}