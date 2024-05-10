import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.http.HttpResponse;

public class ForgotPasswordPage extends JFrame implements ActionListener {
    private JPanel contentPanel = null;
    private JPanel titlePanel = null;
    private JPanel bottomPanel = null;
    private JLabel title = null;
    private ForgotPasswordPanel forgotPasswordPanel = null;
    private JButton backtologinButton = null;
    private JButton forgotPasswordButton = null;
    private final HttpConnectionManager connectionManager;

    public ForgotPasswordPage(HttpConnectionManager connectionManager) {
        super("Password Manager: Reset Your Password");
        this.connectionManager = connectionManager;
        makeWindow();
        createContentPanel();
        add(contentPanel, "Center");
        repaint();
    }
    private void makeWindow() {
        setVisible(true);
        setSize(500, 250);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        makeTitlePanel();
        contentPanel.add(titlePanel, "North");
        makeBottomPanel();
        contentPanel.add(bottomPanel, "South");
        contentPanel.add(forgotPasswordPanel = new ForgotPasswordPanel(), "Center");
    }
    private void makeTitlePanel() {
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title = new JLabel("Reset Password");
        titlePanel.add(title);
    }
    private void makeBottomPanel()
    {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backtologinButton = new JButton("Back to Login");
        backtologinButton.setOpaque(true);
        backtologinButton.setVisible(true);
        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setOpaque(true);
        forgotPasswordButton.setVisible(true);
        backtologinButton.addActionListener(this);
        forgotPasswordButton.addActionListener(this);
        bottomPanel.add(backtologinButton);
        bottomPanel.add(forgotPasswordButton);
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource().equals(backtologinButton)) {
            dispose();
            new LoginPage(connectionManager);
        } else if (ae.getSource().equals(forgotPasswordButton)) {
            //check the fields
            if(myIsValid()) {
                try {
                    forgotPassword();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //make http get request to see if the inputs specified were in the database
            }
        }
    }
    private void forgotPassword() throws IOException, InterruptedException {
        String bodyData = forgotPasswordPanel.serializeForgotPasswordInfo();
        HttpResponse<String> response = connectionManager.sendForgotPasswordRequest(bodyData);
        if(response.statusCode() == 200){
            dispose();
            new ChangePasswordPage(connectionManager, response.body());
        }
    }
    private Boolean myIsValid(){
        return forgotPasswordPanel.checkFields();
    }
}
