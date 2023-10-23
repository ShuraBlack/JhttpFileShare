package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to parse the query string.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class Query {

    /**
     * Private constructor to prevent instantiation.
     */
    private Query() { }

    /**
     * Parses the given query string and returns a map with the key value pairs.
     * @param query The query string.
     * @return The map with the key value pairs.
     */
    public static Map<String, String> toMap(String query) {
        if(query == null || query.isEmpty()){
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

}
