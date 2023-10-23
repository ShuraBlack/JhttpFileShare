package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryTest {

    @Test
    @DisplayName("queryToMap() - valid input - return mapping")
    void queryToMap_validInput() {
        final String query = "key1=value1&key2=value2&key3=value3";
        final Map<String, String> map = Query.toMap(query);

        assertEquals(3, map.size());
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        assertEquals("value3", map.get("key3"));
    }

    @Test
    @DisplayName("queryToMap() - empty input - return empty mapping")
    void queryToMap_emptyInput() {
        final String query = "";
        final Map<String, String> map = Query.toMap(query);

        assertEquals(0, map.size());
    }

    @Test
    @DisplayName("queryToMap() - null input - return empty mapping")
    void queryToMap_nullInput() {
        final String query = null;
        final Map<String, String> map = Query.toMap(query);

        assertEquals(0, map.size());
    }
}
