// Isolates string parsing logic from business logic

import java.util.ArrayList;
import java.util.List;

public class JsonParserUtil {

    public static List<String> splitEVENTS(String jsonArray){
        List<String> eventList = new ArrayList<>();
        if(jsonArray.startsWith("[")) jsonArray = jsonArray.substring(1);
        if (jsonArray.endsWith("]")) jsonArray = jsonArray.substring(0, jsonArray.length() -1);

        String[] events = jsonArray.split("(?=\\{\\s*\"id\":)");

        for(String e : events){
            if(!e.isBlank()){
                eventList.add(e);
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
