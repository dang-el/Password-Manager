public class Driver {
    public static void main(String args[]) {
        HttpConnectionManager connectionManager = new HttpConnectionManager("http://127.0.0.1:7878");
        LoginPage loginPage = new LoginPage(connectionManager);
    }
}