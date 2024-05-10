import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpConnectionManager {
    private final HttpClient client;
    private String uri = null;

    public HttpConnectionManager(String uri){
        client = HttpClient.newBuilder().build();
        this.uri = uri;
    }
    public HttpResponse<String> sendLoginRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendSignupRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/signup"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendForgotPasswordRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/forgot-password"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendChangePasswordRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/forgot-password/change-password"))
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendAddAccountRequest(String data) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/add-account"))
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendPullAccountInfoRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + '/'))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendDeleteAccountRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/delete-login"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendEditAccountRequest(String data) throws IOException, InterruptedException {
        HttpRequest  request = HttpRequest.newBuilder(URI.create(uri + "/edit-login"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendSearchRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/search"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> sendDeleteUserAccountRequest(String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "/delete-account"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}