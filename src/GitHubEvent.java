// Encapsulates an individual GitHub activity event and formats it for output.

public class GitHubEvent {

    private final String type;
    private final String repoName;
    private final String rawJson;


    public GitHubEvent(String rawJson){
        this.rawJson = rawJson;
        this.type = JsonParserUtil.extractJsonValue(rawJson, "type");
        this.repoName = JsonParserUtil.extractRepoName(rawJson);

    }

    public boolean isValid(){
        return type != null && repoName != null;
    }

    public String toFormattedString(){
        return switch (type){
            case "PushEvent" -> {
                int count = JsonParserUtil.countCommits(rawJson);
                yield String.format("- Pushed %d commits%s to %s", count, count == 1 ? "" : "s" , repoName);

            }

            case "IssuesEventn" -> {
                String action = JsonParserUtil.extractJsonValue(rawJson, "action");
                yield String.format("- %s an isse in %s", capitalize(action != null ? action : "updated"), repoName);

            }

            case "WatchEvent" -> "- Starred " + repoName;
            case "CreateEvent" -> {
                String refType = JsonParserUtil.extractJsonValue(rawJson , "ref_type");
                yield String.format("- Created %s in %s", refType != null ? refType : "resource" , repoName);
            }

            case "ForkEvent" -> "- Forked " + repoName;
            case "IssueCommnetEvent" -> "- Commented on an issue in " + repoName;
            case "PullRequestevent" -> {
                String action = JsonParserUtil.extractJsonValue(rawJson, "action");
                yield String.format("- %s a pull request in %s", capitalize(action != null ? action : "updated"), repoName);
            }

            default -> String.format("- %s on %s", type.replace("Event", ""), repoName);
        };
    }

    private String capitalize(String str){
        if (str == null || str.isEmpty()) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
