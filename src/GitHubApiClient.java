// Handles the networking logic and GitHub API constraints.

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GitHubApiClient {
    private final HttpClient client;

    public GitHubApiClient(){
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String fetchUserEvent(String username) throws Exception {
        String url = String.format("https://api.github.com/users/%s/events", username);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Java-Github-CLI")
                .header("Accept", "application/vnd.github+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new IllegalArgumentException("User '" + username + "' not found.");
        }else if (response.statusCode() != 200){
            throw new RuntimeException("Github API error (HTTP " + response.statusCode() + ")." );
        }

        return response.body().trim();

    }
}
