package model.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.Controller;
import model.data.FileData;
import model.data.Structure;
import model.session.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.PageLoader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

import static util.Query.queryToMap;

/**
 * This class is responsible for the directory page request.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class DirectoryPage implements HttpHandler {

    /**
     * Logger for the DirectoryPage class.
     */
    private static final Logger LOGGER = LogManager.getLogger(DirectoryPage.class);


    @Override
    public void handle(HttpExchange he) throws IOException {
        if (Controller.isVerbose())
            LOGGER.info(String.format("%nExchange for \033[0;33m</directory>\033[0m...%nfrom %s:%s", he.getRemoteAddress().getAddress().getHostAddress(), he.getRemoteAddress().getPort()));
        Optional<UserSession> session = Controller.getSessions().stream().filter(s -> s.getIp().equals(he.getRemoteAddress().getAddress().getHostAddress())).findFirst();
        UserSession userSession;
        if (session.isEmpty()) {
            userSession = new UserSession(he.getRemoteAddress().getAddress().getHostAddress());
            userSession.setWorkDirectory(Structure.ROOT_DIR);
            userSession.getFiles().addAll(FileData.loadFiles(userSession.getWorkDirectory()));
            Controller.getSessions().add(userSession);
        } else {
            userSession = session.get();
        }

        Map<String, String> params = queryToMap(he.getRequestURI().getQuery());
        if (params.containsKey("folder")) {
            userSession.setWorkDirectory(params.get("folder"));
            userSession.clear();
            userSession.getFiles().addAll(FileData.loadFiles(userSession.getWorkDirectory()));
        }

        String response = fillContent(userSession, PageLoader.getContent("Directory.html"));
        he.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // =================================================================================================================
    // Private Methods
    // =================================================================================================================

    /**
     * Fills the content of the page with the given user session.
     * @param userSession The user session.
     * @param content The content of the page.
     * @return The filled content.
     */
    private String fillContent(UserSession userSession, String content) {
        content = content.replace("{{returnDisabled}}",
                UserSession.isRootLimited() && userSession.getWorkDirectory().equals(Structure.ROOT_DIR)
                        ? "display: none;" : "display: inline-block;");
        content = content.replace("{{workingDir}}", userSession.getWorkDirectory());
        content = content.replace("{{files}}", userSession.getFileHtml());
        content = content.replace("{{return}}", userSession.getReturnWorkingDirectory());
        content = content.replace("{{structure}}", "<p class=\"structure\">" + (UserSession.isRootLimited() ? Structure.ROOT_TREE : "Not available while root limit is disabled") + "</p>");
        return content;
    }
}
