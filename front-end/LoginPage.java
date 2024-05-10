import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends JFrame implements ActionListener {
    private final HttpConnectionManager connectionManager;
    private JPanel contentPanel = null;
    private JPanel titlePanel = null;
    private JPanel bottomPanel = null;
    private JButton forgotPasswordButton = null;
    private JButton signupButton = null;
    private JButton loginButton = null;
    private LoginPanel loginPanel = null;
    private JLabel title = null;

    public LoginPage(HttpConnectionManager connectionManager){
        super("Login");
        this.connectionManager = connectionManager;
        makeWindow();
        createContentPanel();
        add(contentPanel, "Center");
        repaint();
        revalidate();
    }
    private void makeWindow(){
        setVisible(true);
        setSize(500, 250);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void createContentPanel(){
        contentPanel = new JPanel(new BorderLayout());
        makeTitlePanel();
        contentPanel.add(titlePanel, "North");
        makeBottomPanel();
        contentPanel.add(bottomPanel, "South");
        contentPanel.add((loginPanel = new LoginPanel(this)), "Center");
    }
    private void makeTitlePanel(){

        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title = new JLabel("Login");
        titlePanel.add(title);
    }
    private void makeBottomPanel(){
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupButton = new JButton("Sign Up");
        signupButton.setOpaque(true);
        signupButton.setVisible(true);
        loginButton = new JButton("Login");
        loginButton.setOpaque(true);
        loginButton.setVisible(true);
        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setOpaque(true);
        forgotPasswordButton.setVisible(true);
        signupButton.addActionListener(this);
        forgotPasswordButton.addActionListener(this);
        loginButton.addActionListener(this);
        bottomPanel.add(signupButton);
        bottomPanel.add(forgotPasswordButton);
        bottomPanel.add(loginButton);
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae.getActionCommand());
        if (ae.getSource().equals(loginButton) || ae.getSource().getClass().equals(JTextField.class) || ae.getSource().getClass().equals(JPasswordField.class)) {
            //check the fields
            if(myIsValid()) {
                //if username and password are good log in
                try {
                    logIn();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(ae.getSource().equals(signupButton)) {
            dispose();
            new SignUpPage(connectionManager);
        }
        else if (ae.getSource().equals(forgotPasswordButton)) {
            dispose();
            new ForgotPasswordPage(connectionManager);
        }
    }
    private Boolean myIsValid(){
        return loginPanel.checkFields();
    }
    private void logIn() throws IOException, InterruptedException {
        String data = loginPanel.serializeLoginInfo();
        HttpResponse<String> response = connectionManager.sendLoginRequest(data);
        System.out.println(response.body().toString());
        int statusCode = response.statusCode();
        if(statusCode == 200){
            dispose();
            UserAccount userAccount = new UserAccount(response);
            new MainPage(connectionManager, userAccount);
        }
    }
    public static Map<String, String> deserialize(String serializedData) {
        Map<String, String> map = new HashMap<>();
        String[] keyValuePairs = serializedData.split("&");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            String key = entry[0];
            String value = entry.length > 1 ? entry[1] : ""; // Handle cases where value is empty
            map.put(key, value);
        }
        return map;
    }
}