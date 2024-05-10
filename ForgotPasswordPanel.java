import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ForgotPasswordPanel extends JPanel {
    private JPanel usernamePanel = null;
    private JLabel usernameLabel = null;
    private JTextField usernameTextField = null;
    private JPanel emailPanel = null;
    private JLabel emailLabel = null;
    private JTextField emailTextField = null;
    private JPanel phoneNumberPanel = null;
    private JLabel phoneNumberLabel = null;
    private JTextField phoneNumberTextField = null;

    public ForgotPasswordPanel(){

        setLayout(new FlowLayout());
        createUsernamePanel();
        createEmailPanel();
        createPhoneNumberPanel();
        add(usernamePanel);
        add(emailPanel);
        add(phoneNumberPanel);
        setVisible(true);
    }
    private void createUsernamePanel(){
        usernamePanel = new JPanel();
        usernamePanel.setLayout(new GridLayout(1,2));
        usernameLabel = new JLabel("Username");
        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(216,30));
        usernameTextField.setVisible(true);
        usernameTextField.setEditable(true);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);
        usernamePanel.setVisible(true);
    }
    private void createEmailPanel(){
        emailPanel = new JPanel();
        emailPanel.setLayout(new GridLayout(1,2));
        emailLabel = new JLabel("Email");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(216,30));
        emailTextField.setVisible(true);
        emailTextField.setEditable(true);
        emailPanel.add(emailLabel);
        emailPanel.add(emailTextField);
        emailPanel.setVisible(true);
    }
    private void createPhoneNumberPanel(){
        phoneNumberPanel = new JPanel();
        phoneNumberPanel.setLayout(new GridLayout(1,2));
        phoneNumberLabel = new JLabel("Phone Number");
        phoneNumberTextField = new JTextField();
        phoneNumberTextField.setPreferredSize(new Dimension(216,30));
        phoneNumberTextField.setVisible(true);
        phoneNumberTextField.setEditable(true);
        phoneNumberPanel.add(phoneNumberLabel);
        phoneNumberPanel.add(phoneNumberTextField);
        phoneNumberPanel.setVisible(true);
    }
    public Boolean checkFields(){
        if(checkUsername()){
            //send a post request
            return checkEmail() || checkPhoneNumber();
        }
        return false;
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
    private Boolean checkPhoneNumber(){
        //returns true if no letters are found in the phone numbers and if 10 chars
        String phoneNumber = phoneNumberTextField.getText();

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
    public String serializeForgotPasswordInfo(){
        ArrayList<String> info = getInfo();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("username=")
                .append(info.get(0))
                .append("&email=")
                .append(info.get(1))
                .append("&phone_number=")
                .append(info.get(2));
        return stringBuilder.toString();
    }
    private ArrayList<String> getInfo(){
        ArrayList<String> info = new ArrayList<>();
        info.add(usernameTextField.getText());
        if(checkEmail()){
            info.add(emailTextField.getText());
        }
        else{
            info.add("null");
        }
        if(checkPhoneNumber()){
            info.add(phoneNumberTextField.getText());
        }
        else{
            info.add("null");
        }
        return info;
    }
}
