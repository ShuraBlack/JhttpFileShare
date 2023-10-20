package model.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserSessionTest {

    @Test
    @DisplayName("getReturnWorkingDirectory() - valid input - return value")
    void getReturnWorkingDirectory_validInput() {
        final UserSession userSession = new UserSession("");
        userSession.setWorkDirectory("C:/Users/");

        final String directory = userSession.getReturnWorkingDirectory();

        assertEquals("/directory/?folder=C:/", directory);
    }

    @Test
    @DisplayName("getReturnWorkingDirectory() - invalid input - return empty")
    void getReturnWorkingDirectory_invalidInput() {
        final UserSession userSession = new UserSession("");
        userSession.setWorkDirectory("C:/");

        final String directory = userSession.getReturnWorkingDirectory();

        assertEquals("", directory);
    }

    @Test
    @DisplayName("getFileHtml() - empty directory - return empty information")
    void getReturnWorkingDirectory_rootLimited() {
        final UserSession userSession = new UserSession("");
        userSession.setWorkDirectory("C:/Users/");

        final String html = userSession.getFileHtml();

        assertEquals("<h1>Empty Directory</h1>", html);
    }
}
