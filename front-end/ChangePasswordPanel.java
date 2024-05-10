import javax.swing.*;
import java.awt.*;

public class ChangePasswordPanel extends JPanel {
    private final ChangePasswordPage changePasswordPage;
    private JLabel newPasswordLabel = null;
    private JLabel confirmPasswordLabel = null;
    private JPasswordField newPasswordTextField = null;
    private JPasswordField confirmPasswordTextField = null;
    private JPanel createPanel = null;
    private JPanel confirmPanel = null;
    private JPanel emptyPanel = null;
    private JLabel emptyLabel = null;
    public ChangePasswordPanel(ChangePasswordPage cpp){
        changePasswordPage = cpp;
        setLayout(new GridLayout(3,1));
        //initialize labels
        createNewPasswordInputPanel();
        createConfirmPasswordInputPanel();
        add(createPanel);
        emptyPanel = new JPanel();
        emptyPanel.setLayout(new FlowLayout());
        emptyLabel = new JLabel();
        emptyPanel.add(emptyLabel);
        add(emptyPanel);
        add(confirmPanel);
        setVisible(true);
    }
    private void createNewPasswordInputPanel(){
        createPanel = new JPanel();
        createPanel.setLayout(new GridLayout(1,2));
        newPasswordLabel = new JLabel("New Password");
        newPasswordTextField = new JPasswordField();
        newPasswordTextField.addActionListener(changePasswordPage);
        newPasswordTextField.setPreferredSize(new Dimension(216, 30));
        newPasswordTextField.setEditable(true);
        newPasswordTextField.setVisible(true);
        createPanel.add(newPasswordLabel);
        createPanel.add(newPasswordTextField);
        createPanel.setVisible(true);
    }
    private void createConfirmPasswordInputPanel(){
        confirmPanel = new JPanel();
        confirmPanel.setLayout(new GridLayout(1,2));
        confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordTextField = new JPasswordField();
        confirmPasswordTextField.addActionListener(changePasswordPage);
        confirmPasswordTextField.setPreferredSize(new Dimension(216, 30));
        confirmPasswordTextField.setEditable(true);
        confirmPasswordTextField.setVisible(true);
        confirmPanel.add(confirmPasswordLabel);
        confirmPanel.add(confirmPasswordTextField);
        confirmPanel.setVisible(true);
    }
    public String getSerializeData(){
        String newPassword = SignUpPanel.getPasswordTextFromField(newPasswordTextField);
        return "new_password=" + newPassword;
    }
    public Boolean checkFields(){
        if(checkPassword(newPasswordTextField) && checkPassword(confirmPasswordTextField)){
            String newPassword = SignUpPanel.getPasswordTextFromField(newPasswordTextField);
            String confirmPassword= SignUpPanel.getPasswordTextFromField(confirmPasswordTextField);
            return newPassword.equals(confirmPassword);
        }
        return false;
    }
    private Boolean checkPassword(JPasswordField passwordField){
        Boolean isUpper = false;
        Boolean isLower = false;
        Boolean isNumber = false;
        Boolean isSymbol = false;
        //get text from password field
        String password = SignUpPanel.getPasswordTextFromField(passwordField);
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
}