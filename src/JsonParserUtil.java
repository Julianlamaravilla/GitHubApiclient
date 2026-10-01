// Isolates string parsing logic from business logic

import java.util.ArrayList;
import java.util.List;

public class JsonParserUtil {

    public static List<String> splitEVENTS(String jsonArray){
        List<String> eventList = new ArrayList<>();
        int depth = 0;  // how many {} we are inside
        int start = -1; // index where the current event began
        boolean inString = false;

        for (int i = 0; i < jsonArray.length(); i++){
            char c = jsonArray.charAt(i);

            if (inString){
                if ( c == '\\') i++;                    //skip escaped char, e.g \"
                else if (c == '\\') inString = false;   // String ended
                continue;                               // ignore everything inside strings
            }

            if (c == '"') {
                inString = true;
            } else if (c == '{'){
                if (depth == 0) start = i;      // a new top-level event start
                depth++;
            } else if (c == '}'){
                depth--;
                if (depth == 0 && start != -1){
                    eventList.add(jsonArray.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return eventList;

    }

    public static String extractJsonValue(String json, String key){
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return null;

        int valueStart = keyIndex + searchKey.length();
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))){
            valueStart++;
        }

        if (valueStart < json.length() && json.charAt(valueStart) == '"'){
            valueStart++;
            int valueEnd = json.indexOf('"', valueStart);
            if(valueEnd != -1){
                return json.substring(valueStart, valueEnd);
            }
        }
        return null;
    }

    public static String extractRepoName(String json){
        int repoIndex = json.indexOf("\"repo\":");
        if (repoIndex == -1) return null;
        return extractJsonValue(json.substring(repoIndex), "name");
    }

    public static int countCommits(String json) {
        int payloadIndex = json.indexOf("\"payload\":");
        if (payloadIndex == -1) return 1;

        String payload = json.substring(payloadIndex);
        int commitsIndex = payload.indexOf("\"commits\":");
        if (commitsIndex == -1) return 1;

        int arrayStart = payload.indexOf('[', commitsIndex);
        int arrayEnd = payload.indexOf(']', arrayStart);
        if (arrayStart == -1 || arrayEnd == -1) return 1;

        String commitsArray = payload.substring(arrayStart + 1 , arrayEnd);
        if (commitsArray.isBlank()) return 0;

        int count = 0;
        int idx = 0;
        while ((idx = commitsArray.indexOf("\"sha\":", idx)) != -1){
            count++;
            idx += 6;
        }

        return count > 0 ? count : 1;
    }

}
