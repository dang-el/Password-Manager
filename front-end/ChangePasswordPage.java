import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class ChangePasswordPage extends JFrame implements ActionListener {
    private final HttpConnectionManager connectionManager;
    private ChangePasswordPanel changePasswordPanel = null;
    private JButton changePasswordButton = null;
    private JButton backToLoginButton = null;
    private JPanel titlePanel = null;
    private JPanel bottomPanel = null;
    private String forgotPasswordBodyData = null;
    private JPanel buttonPanel = null;

    public ChangePasswordPage(HttpConnectionManager connectionManager, String forgotPasswordBodyData){
        super("Change Password");
        this.forgotPasswordBodyData = forgotPasswordBodyData;
        //this page will only be created if
        // 1. the input fields of the ForgotPasswordPage are valid.
        // 2. an http request with the serialized input data in the body is sent to the rust backend
        // 3. the rust backend receives the call, parses the data, checks the database, and has confirmed that this user exists
        // 4. the rust backend responds with a message allowing the changePasswordPage to be displayed
        // then the user will have the abilty to make a change to the saved password
        this.connectionManager = connectionManager;
        setLayout(new BorderLayout());
        setSize(new Dimension(500,250));
        titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(new JLabel("Change Password"));
        add(titlePanel, BorderLayout.NORTH);
        add((changePasswordPanel = new ChangePasswordPanel(this)), BorderLayout.CENTER);
        changePasswordButton = new JButton("Done");
        changePasswordButton.addActionListener(this);
        changePasswordButton.setOpaque(true);
        changePasswordButton.setVisible(true);
        changePasswordButton.setPreferredSize(new Dimension(216,30));
        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.addActionListener(this);
        backToLoginButton.setOpaque(true);
        backToLoginButton.setVisible(true);
        backToLoginButton.setPreferredSize(new Dimension(216,30));
        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(changePasswordButton, BorderLayout.NORTH);
        buttonPanel.add(backToLoginButton, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private Boolean myIsValid(){
        return changePasswordPanel.checkFields();
    }
    private void changePassword(String data) throws IOException, InterruptedException {
        HttpResponse<String> response = connectionManager.sendChangePasswordRequest(data);
        if(response.statusCode() == 200){
            dispose();
            new LoginPage(connectionManager);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //get inputs and make http request
        if(e.getSource().equals(changePasswordButton) || e.getSource().getClass().equals(JPasswordField.class)) {
            if(myIsValid()){
                String data = changePasswordPanel.getSerializeData();
                data = forgotPasswordBodyData + "&" + data  ;
                try {
                    changePassword(data);
                    JOptionPane.showMessageDialog(this, "Password change successful.");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if(e.getSource().equals(backToLoginButton)){
            dispose();
            new LoginPage(connectionManager);
        }
    }
}