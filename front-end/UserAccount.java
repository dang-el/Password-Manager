import java.net.http.HttpResponse;
import java.util.Map;

public class UserAccount {
    private String username = null;
    private String password = null;
    private String email = null;
    private String phone = null;
    private String id = null;

    public UserAccount(HttpResponse<String> responseData){
        Map<String, String> map = LoginPage.deserialize(responseData.body().toString());
        this.id = map.get("id");
        this.username = map.get("username");
        this.password = map.get("password");
        this.email = map.get("email");
        this.phone = map.get("phone_number");
    }
    public String getID(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String serializeUserAccountInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id).append("&username=").append(username)
                .append("&password=").append(password)
                .append("&email=").append(email)
                .append("&phone_number=").append(phone);
        return sb.toString();
    }
}
