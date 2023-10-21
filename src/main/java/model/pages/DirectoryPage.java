package model.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.Controller;
import model.data.FileData;
import model.session.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import util.PageLoader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

import static util.FileSize.convertSize;
import static util.Query.queryToMap;

/**
 * This class is responsible for the directory page request.
 *
 * @version 0.1.1
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
        if (Config.isVerbose())
            LOGGER.info(String.format("%nExchange for \033[0;33m</directory>\033[0m...%nfrom %s:%s", he.getRemoteAddress().getAddress().getHostAddress(), he.getRemoteAddress().getPort()));
        Optional<UserSession> session = Controller.getSessions().stream().filter(s -> s.getIp().equals(he.getRemoteAddress().getAddress().getHostAddress())).findFirst();
        UserSession userSession;
        if (session.isEmpty()) {
            userSession = new UserSession(he.getRemoteAddress().getAddress().getHostAddress());
            userSession.setWorkDirectory(Config.getRootDirectory());
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

        String response = populateContent(userSession, PageLoader.getPage("directory"));
        he.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // =================================================================================================================
    // Public Methods
    // =================================================================================================================

    public static String htmlFromUserSession(UserSession session) {
        StringBuilder html = new StringBuilder();
        String position = null;
        for (FileData file : session.getFiles()) {
            if (position == null || !position.equals(file.getName().substring(0, 1))) {
                position = file.getName().substring(0, 1);
                html.append("<p class=\"position\">").append(position).append("</p>\n");
            }
            html.append("<dl class=\"table\">\n");

            String name;
            if (file.getType().equals("Directory")) {

                name = String.format("<a class=\"download\" title=\"Download Archive\" href=\"%s\">\uD83D\uDCE6</a>" +
                                "<a class=\"dir\" href=\"%s\">\uD83D\uDCC1 %s</a>"
                        , "/download/?filename=" + session.getWorkDirectory() + "/"+file.getName()
                        , "/directory/?folder=" + session.getWorkDirectory() + "/"+file.getName(), file.getName());
            } else {
                name = String.format("<a class=\"download\" title=\"Download File\" href=\"%s\">\uD83D\uDCBE </a>", "/download/?filename="
                        + session.getWorkDirectory() + "/"+file.getName()) + file.getName();
            }

            html.append("<dt>").append(name).append("</dt>\n");
            if (file.getSize() > 0) {
                html.append("<dd>").append(convertSize(file.getSize())).append("</dd>\n");
            }
            html.append("</dl>\n");
        }
        if (session.getFiles().isEmpty()) {
            html.append("<h1>Empty Directory</h1>");
        }
        return html.toString();
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
    private String populateContent(UserSession userSession, String content) {
        content = content.replace("{{returnDisabled}}",
                Config.isRootRestricted() && userSession.getWorkDirectory().equals(Config.getRootDirectory())
                        ? "display: none;" : "display: inline-block;");
        content = content.replace("{{workingDir}}", userSession.getWorkDirectory());
        content = content.replace("{{files}}", htmlFromUserSession(userSession));
        content = content.replace("{{return}}", userSession.getReturnWorkingDirectory());
        content = content.replace("{{structure}}", "<p class=\"structure\">" + (Config.isRootRestricted() ? Config.getRootStructure() : "Not available while root limit is disabled") + "</p>");
        return content;
    }
}
