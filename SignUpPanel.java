import javax.swing.*;
import java.awt.*;

public class SignUpPanel extends JPanel {
    private final SignUpPage signUpPage;
    private final SignUpPanel signUpPanel;
    private JPanel usernameInputPanel = null;
    private JLabel usernameLabel = null;
    private JTextField usernameTextField = null;
    private JPanel phoneNumInputPanel = null;
    private JLabel phoneNumLabel = null;
    private JTextField phoneNumTextField = null;
    private JPanel emailInputPanel = null;
    private JLabel emailLabel = null;
    private JTextField emailTextField = null;
    private JPanel createPasswordInputPanel = null;
    private JLabel createPasswordLabel = null;
    private JPasswordField createPasswordTextField = null;
    private JPanel confirmPasswordInputPanel = null;
    private JLabel confirmPasswordLabel = null;
    private JPasswordField confirmPasswordTextField = null;

    public SignUpPanel(SignUpPage sup) {
        signUpPage = sup;
        signUpPanel = this;
        signUpPanel.setLayout(new GridLayout(5,1));
        createUsernameInputPanel();
        createPhoneNumInputPanel();
        createEmailInputPanel();
        createNewPasswordInputPanel();
        createConfirmPasswordInputPanel();
        signUpPanel.add(usernameInputPanel);
        signUpPanel.add(phoneNumInputPanel);
        signUpPanel.add(emailInputPanel);
        signUpPanel.add(createPasswordInputPanel);
        signUpPanel.add(confirmPasswordInputPanel);
        signUpPanel.setVisible(true);
    }
    public static String getPasswordTextFromField(JPasswordField pf){
        char[] passwordChars = pf.getPassword();
        StringBuilder passwordBuilder = new StringBuilder();
        for (char c : passwordChars) {
            passwordBuilder.append(c);
        }
        return passwordBuilder.toString();
    }
    private void createUsernameInputPanel() {
        usernameInputPanel = new JPanel(new FlowLayout());
        usernameLabel = new JLabel("Create Username: ");
        usernameLabel.setVisible(true);
        usernameLabel.setPreferredSize(new Dimension(216, 30));
        usernameTextField = new JTextField();
        usernameTextField.addActionListener(signUpPage);
        usernameTextField.setEditable(true);
        usernameTextField.setVisible(true);
        usernameTextField.setPreferredSize(new Dimension(216, 30));
        usernameInputPanel.add(usernameLabel);
        usernameInputPanel.add(usernameTextField);
    }
    private void createPhoneNumInputPanel() {
        phoneNumInputPanel = new JPanel(new FlowLayout());
        phoneNumLabel = new JLabel("Enter Phone Number:");
        phoneNumLabel.setVisible(true);
        phoneNumLabel.setPreferredSize(new Dimension(216,30));
        phoneNumTextField = new JTextField();
        phoneNumTextField.addActionListener(signUpPage);
        phoneNumTextField.setEditable(true);
        phoneNumTextField.setVisible(true);
        phoneNumTextField.setPreferredSize(new Dimension(216,30));
        phoneNumInputPanel.add(phoneNumLabel);
        phoneNumInputPanel.add(phoneNumTextField);
    }
    private void createEmailInputPanel() {
        emailInputPanel = new JPanel(new FlowLayout());
        emailLabel = new JLabel("Enter Email: ");
        emailLabel.setVisible(true);
        emailLabel.setPreferredSize(new Dimension(216,30));
        emailTextField = new JTextField();
        emailTextField.addActionListener(signUpPage);
        emailTextField.setEditable(true);
        emailTextField.setVisible(true);
        emailTextField.setPreferredSize(new Dimension(216,30));
        emailInputPanel.add(emailLabel);
        emailInputPanel.add(emailTextField);
    }
    private void createNewPasswordInputPanel() {
        createPasswordInputPanel = new JPanel(new FlowLayout());
        createPasswordLabel = new JLabel("Create Password: ");
        createPasswordLabel.setVisible(true);
        createPasswordLabel.setPreferredSize(new Dimension(216,30));
        createPasswordTextField = new JPasswordField();
        createPasswordTextField.addActionListener(signUpPage);
        createPasswordTextField.setEditable(true);
        createPasswordTextField.setVisible(true);
        createPasswordTextField.setPreferredSize(new Dimension(216,30));
        createPasswordInputPanel.add(createPasswordLabel);
        createPasswordInputPanel.add(createPasswordTextField);
    }
    private void createConfirmPasswordInputPanel() {
        confirmPasswordInputPanel = new JPanel(new FlowLayout());
        confirmPasswordLabel = new JLabel("Confirm Password: ");
        confirmPasswordLabel.setVisible(true);
        confirmPasswordLabel.setPreferredSize(new Dimension(216,30));
        confirmPasswordTextField = new JPasswordField();
        confirmPasswordTextField.addActionListener(signUpPage);
        confirmPasswordTextField.setEditable(true);
        confirmPasswordTextField.setVisible(true);
        confirmPasswordTextField.setPreferredSize(new Dimension(216,30));
        confirmPasswordInputPanel.add(confirmPasswordLabel);
        confirmPasswordInputPanel.add(confirmPasswordTextField);
    }
    public Boolean checkFields(){
        //this method returns true when username, password, and either email or phone number are valid
        Boolean retval = false;
        if(checkUsername() && checkPasswords()){
            if(checkEmail()){
                retval = true;
            }
            if(checkPhoneNumber()){
                retval = true;
            }
        }
        return retval;
    }
    private Boolean checkUsername(){
        String username = usernameTextField.getText();
        if(username.length() < 6){
            return false;
        }
        for(Character c : username.toCharArray()){
            if(c.equals(' ')||c.equals(',')){
                return false;
            }
        }
        return true;
    }
    private Boolean checkPasswords(){
        if(checkPassword()){
            return checkConfirmPassword();
        }
        return false;
    }
    private Boolean checkPassword(){
        Boolean isUpper = false;
        Boolean isLower = false;
        Boolean isNumber = false;
        Boolean isSymbol = false;
        //get text from password field
        String password = getPasswordTextFromField(createPasswordTextField);
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
    private Boolean checkConfirmPassword(){
        String password = getPasswordTextFromField(createPasswordTextField);
        String confirmPassword = getPasswordTextFromField(confirmPasswordTextField);
        if (password.equals(confirmPassword)){
            return true;
        }
        return false;
    }
    private Boolean checkEmail(){
        //returns true if string contains an @ and no spaces or commas
        String email = emailTextField.getText();
        Boolean isAt = false;
        for(Character c : email.toCharArray()){
            if(c.equals(' ') || c.equals(',')){
                return false;
            }
            if(c.equals('@')){
                isAt = true;
            }
        }
        return isAt;
    }
    private Boolean checkPhoneNumber() {
        //returns true if no letters are found in the phone numbers and if 10 chars
        String phoneNumber = phoneNumTextField.getText();
        if (phoneNumber.length() == 10) {
            for (int i = 0; i < 10; i++) {
                Character c = phoneNumber.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        } else return false;
    }
    public String serializeNewUserData(){
        StringBuilder sb = new StringBuilder();
        sb.append("username=");
        if(usernameTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(usernameTextField.getText());
        }
        sb.append("&phone_number=");
        if(phoneNumTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(phoneNumTextField.getText());
        }
        sb.append("&email=");
        if(emailTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(emailTextField.getText());
        }
        sb.append("&password=").append(getPasswordTextFromField(createPasswordTextField));
        return sb.toString();
    }
}