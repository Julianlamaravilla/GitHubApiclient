import java.util.List;


public class GitHubActivity {

    public static void main(String[] args){
        if (args.length < 1 || args[0].trim().isEmpty()){
            System.out.println("Usage: java GitHubActivity <username>");
            System.exit(1);
        }

        String username = args[0].trim();
        GitHubApiClient apiClient = new GitHubApiClient();

        try {
            String rawJson = apiClient.fetchUserEvent(username);

            if (rawJson.equals("[]")){
                System.out.printf("No recent activity found for user '%s' .%n", username);
                return;
            }

            List<String> rawEvents = JsonParserUtil.splitEVENTS(rawJson);
            for (String rawEvent : rawEvents){
                GitHubEvent event = new GitHubEvent(rawEvent);
                if (event.isValid()){
                    System.out.println(event.toFormattedString());
                }
            }
        } catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        } catch (java.net.http.HttpTimeoutException e){
            System.err.println("Error: connection time out. Please check your internet connection.");
        } catch (Exception e){
            System.err.println("Error fetching activity: " + e.getMessage());
        }
    }
}
