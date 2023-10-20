package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageLoaderTest {

    @Test
    @DisplayName("loadPage() - valid input - return value")
    void loadPage_validInput() {
        final String content = PageLoader.getPage("directory");

        assertNotNull(content);
    }

    @Test
    @DisplayName("loadPage() - empty input - return name")
    void loadPage_emptyInput() {
        final String content = PageLoader.getPage("");

        assertNotNull(content);
        assertEquals("Directory.html", content);
    }

    @Test
    @DisplayName("loadPage() - null input - throw exception")
    void loadPage_nullInput() {
        assertThrows(NullPointerException.class, () -> PageLoader.getPage(null));
    }

    @Test
    @DisplayName("loadPage() - invalid input - throw exception")
    void loadPage_invalidInput() {
        assertThrows(NullPointerException.class, () -> PageLoader.getPage("invalid.html"));
    }
}
