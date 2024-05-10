import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class SignUpPage extends JFrame implements ActionListener {
    private JPanel contentPanel = null;
    private JPanel titlePanel = null;
    private JPanel bottomPanel = null;
    private SignUpPanel signUpPanel = null;
    private JLabel title = null;
    private JButton signUpButton = null;
    private JButton backToLoginButton = null;
    private final HttpConnectionManager connectionManager;

    public SignUpPage(HttpConnectionManager connectionManager) {
        super("Password Manager: Sign-Up");
        this.connectionManager = connectionManager;
        makeWindow();
        createContentPanel();
        add(contentPanel, "Center");
        repaint();
    }
    private void makeWindow() {
        setVisible(true);
        setSize(500, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        makeTitlePanel();
        contentPanel.add(titlePanel, "North");
        makeBottomPanel();
        contentPanel.add(bottomPanel, "South");
        contentPanel.add((signUpPanel = new SignUpPanel(this)), "Center");
    }
    private void makeTitlePanel() {
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title = new JLabel("Sign-Up");
        titlePanel.add(title);
    }
    private void makeBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpButton = new JButton("Sign-Up");
        signUpButton.setOpaque(true);
        signUpButton.setVisible(true);
        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setOpaque(true);
        backToLoginButton.setVisible(true);
        signUpButton.addActionListener(this);
        backToLoginButton.addActionListener(this);
        bottomPanel.add(backToLoginButton);
        bottomPanel.add(signUpButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(signUpButton) || e.getSource().getClass().equals(JTextField.class) || e.getSource().getClass().equals(JPasswordField.class)) {
            //check the fields
            if(myIsValid()){
                try {
                    signUpNewUser();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource().equals(backToLoginButton)) {
            dispose();
            new LoginPage(connectionManager);
        }
    }
    private Boolean myIsValid(){
        return signUpPanel.checkFields();
    }
    private void signUpNewUser() throws IOException, InterruptedException {
        //fill an arraylist with the stringified stuff entered by the user on the signup panel
        String data = signUpPanel.serializeNewUserData();
        HttpResponse<String> response = connectionManager.sendSignupRequest(data);
        if(response.statusCode() == 304){
            JOptionPane.showMessageDialog(new JOptionPane(), "User with this account already exists");
        }
        //this needs to be sent as a new user request to the rust backend
        //inputs are valid and the signup button was pressed
        if(response.statusCode() == 200){
            //Map<String, String> info = LoginPage.deserialize(data);
            UserAccount userAccount = new UserAccount(response);
            dispose();
            new MainPage(connectionManager, userAccount);
        }
    }
}