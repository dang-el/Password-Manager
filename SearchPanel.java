import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final MainPage mainPage;
    private SearchPanel searchPanel = null;
    private JPanel searchServiceNameInputPanel = null;
    private JLabel searchServiceNameInputLabel = null;
    private JTextField searchServiceNameInputTextField = null;
    private JPanel searchAccountNicknameInputPanel = null;
    private JLabel searchAccountNicknameInputLabel = null;
    private JTextField searchAccountNicknameInputTextField = null;
    private JPanel searchSiteNameInputPanel = null;
    private JLabel searchSiteNameInputLabel = null;
    private JTextField searchSiteNameInputTextField = null;
    private JPanel searchUsernameInputPanel = null;
    private JLabel searchUsernameInputLabel = null;
    private JTextField searchUsernameInputTextField = null;

    public SearchPanel(MainPage mp) {
        mainPage = mp;
        searchPanel = this;
        searchPanel.setLayout(new GridLayout(4,1));
        createSearchServiceNameInputPanel();
        createSearchAccountNicknameInputPanel();
        createSearchSiteNameInputPanel();
        createUsernameSearchInputPanel();
        searchPanel.add(searchServiceNameInputPanel);
        searchPanel.add(searchAccountNicknameInputPanel);
        searchPanel.add(searchSiteNameInputPanel);
        searchPanel.add(searchUsernameInputPanel);
    }
    private void createSearchServiceNameInputPanel() {
        searchServiceNameInputPanel = new JPanel(new FlowLayout());
        searchServiceNameInputLabel = new JLabel("Service Name: ");
        searchServiceNameInputLabel.setVisible(true);
        searchServiceNameInputLabel.setPreferredSize(new Dimension(216,30));
        searchServiceNameInputTextField = new JTextField();
        searchServiceNameInputTextField.addActionListener(mainPage);
        searchServiceNameInputTextField.setEditable(true);
        searchServiceNameInputTextField.setVisible(true);
        searchServiceNameInputTextField.setPreferredSize(new Dimension(216,30));
        searchServiceNameInputPanel.add(searchServiceNameInputLabel);
        searchServiceNameInputPanel.add(searchServiceNameInputTextField);
    }
    private void createSearchAccountNicknameInputPanel() {
        searchAccountNicknameInputPanel = new JPanel(new FlowLayout());
        searchAccountNicknameInputLabel = new JLabel("Account Nickname: ");
        searchAccountNicknameInputLabel.setVisible(true);
        searchAccountNicknameInputLabel.setPreferredSize(new Dimension(216,30));
        searchAccountNicknameInputTextField = new JTextField();
        searchAccountNicknameInputTextField.addActionListener(mainPage);
        searchAccountNicknameInputTextField.setEditable(true);
        searchAccountNicknameInputTextField.setVisible(true);
        searchAccountNicknameInputTextField.setPreferredSize(new Dimension(216,30));
        searchAccountNicknameInputPanel.add(searchAccountNicknameInputLabel);
        searchAccountNicknameInputPanel.add(searchAccountNicknameInputTextField);
    }
    private void createSearchSiteNameInputPanel() {
        searchSiteNameInputPanel = new JPanel(new FlowLayout());
        searchSiteNameInputLabel = new JLabel("Site Name: ");
        searchSiteNameInputLabel.setVisible(true);
        searchSiteNameInputLabel.setPreferredSize(new Dimension(216,30));
        searchSiteNameInputTextField = new JTextField();
        searchSiteNameInputTextField.addActionListener(mainPage);
        searchSiteNameInputTextField.setEditable(true);
        searchSiteNameInputTextField.setVisible(true);
        searchSiteNameInputTextField.setPreferredSize(new Dimension(216,30));
        searchSiteNameInputPanel.add(searchSiteNameInputLabel);
        searchSiteNameInputPanel.add(searchSiteNameInputTextField);
    }
    private void createUsernameSearchInputPanel() {
        searchUsernameInputPanel = new JPanel(new FlowLayout());
        searchUsernameInputLabel = new JLabel("Account Username: ");
        searchUsernameInputLabel.setVisible(true);
        searchUsernameInputLabel.setPreferredSize(new Dimension(216,30));
        searchUsernameInputTextField = new JTextField();
        searchUsernameInputTextField.addActionListener(mainPage);
        searchUsernameInputTextField.setEditable(true);
        searchUsernameInputTextField.setVisible(true);
        searchUsernameInputTextField.setPreferredSize(new Dimension(216,30));
        searchUsernameInputPanel.add(searchUsernameInputLabel);
        searchUsernameInputPanel.add(searchUsernameInputTextField);
    }
    public String serializeSearchData() {
        StringBuilder sb = new StringBuilder();
        sb.append("service_name=");
        if(searchServiceNameInputTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(searchServiceNameInputTextField.getText());
        }
        sb.append("&account_nickname=");
        if(searchAccountNicknameInputTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(searchAccountNicknameInputTextField.getText());
        }
        sb.append("&site_url=");
        if(searchSiteNameInputTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(searchSiteNameInputTextField.getText());
        }
        sb.append("&username=");
        if(searchUsernameInputTextField.getText().isEmpty()) {
            sb.append("n/a");
        } else {
            sb.append(searchUsernameInputTextField.getText());
        }
        sb.append("&account_id=").append(mainPage.getUserAccount().getID());
        return sb.toString();
    }
}