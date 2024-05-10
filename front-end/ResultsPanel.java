import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.http.HttpResponse;

public class ResultsPanel extends JPanel {
    private final HttpConnectionManager connection;
    //These are the names for the column headers on the table
    private final String[] columnNames = {
            "Service Name",
            "Account Nickname",
            "Username",
            "Password",
            "Site URL"};
    private final MainPage mainPage;
    private ResultsPanel resultsPanel = null;
    private JScrollPane resultsPane = null;
    private JTable resultsTable = null;
    private DefaultTableModel tableModel = null;
    private Object[][] results = null;
    private UserAccount userAccount = null;

    public ResultsPanel(HttpConnectionManager conn, UserAccount userAccount, MainPage mp) {
        this.userAccount = userAccount;
        connection = conn;
        mainPage = mp;
        resultsPanel = this;
        resultsPanel.setLayout(new FlowLayout());
        createScrollPanePanel();
        resultsPanel.add(resultsPane);
    }
    private void createScrollPanePanel() {
        //Layout
        /* Service Name | Account Nickname | Username | Password | Site URL */
        results = new Object[][]{};
        try {
            HttpResponse<String> response = connection.sendPullAccountInfoRequest(userAccount.serializeUserAccountInfo());
            if(response.statusCode() != 204){
                results = MainPage.deserialzeMultipleLogins(response.body());
            }
            for(int i = 0; i < results.length; i++) {
                for(int j = 0; j < results[i].length; j++) {
                    if(results[i][j].equals("n/a")) {
                        results[i][j] = "";
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        tableModel = new DefaultTableModel(results, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setRowSelectionAllowed(true);
        resultsTable.setColumnSelectionAllowed(true);
        resultsPane = new JScrollPane(resultsTable);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2 && resultsTable.rowAtPoint(e.getPoint()) != -1) {
                    //Open a new Edit Page when the user has double-clicked on a row.
                    new EditPage(connection, userAccount, serializeRowData(resultsTable.rowAtPoint(e.getPoint())));
                    mainPage.dispose();
                }
            }
        });
    }
    public String serializeRowData(int rowIndex) {
        Object[] row = results[rowIndex];
        StringBuilder sb = new StringBuilder();
        sb.append("service_name=");
        if(row[0].equals("")) {
            sb.append("n/a");
        } else {
            sb.append(row[0]);
        }
        sb.append("&account_nickname=");
        if(row[1].equals("")) {
            sb.append("n/a");
        } else {
            sb.append(row[1]);
        }
        sb.append("&username=");
        if(row[2].equals("")) {
            sb.append("n/a");
        } else {
            sb.append(row[2]);
        }
        sb.append("&password=");
        if(row[3].equals("")) {
            sb.append("n/a");
        } else {
            sb.append(row[3]);
        }
        sb.append("&site_url=");
        if(row[4].equals("")) {
            sb.append("n/a");
        } else {
            sb.append(row[4]);
        }
        sb.append("&account_id=").append(userAccount.getID());
        return sb.toString();
    }
    public int getNumResults() {
        return resultsTable.getRowCount();
    }
    public DefaultTableModel getTableModel() { return tableModel; }
    public void resetTable(Object[][] data) {
        results = data;
    }
}