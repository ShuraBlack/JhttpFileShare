package model.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.Controller;
import model.data.Structure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

import static util.Query.queryToMap;

/**
 * This class is responsible for the download page request.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class DownloadPage implements HttpHandler {

    /**
     * Logger for the DownloadPage class.
     */
    private static final Logger LOGGER = LogManager.getLogger(DownloadPage.class);

    /**
     * The filename parameter.
     */
    private static final String FILENAME = "filename";

    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, String> params = queryToMap(he.getRequestURI().getQuery());
        if (params.isEmpty() || !params.containsKey(FILENAME) || !params.get(FILENAME).contains(Structure.ROOT_DIR)) {
            return;
        }

        String[] path = params.get(FILENAME).split("/");
        if (Controller.isVerbose())
            LOGGER.info(String.format("%nExchange for \033[0;33m</Download>\033[0m...%nfrom %s:%s%nfile \033[0;33m<%s>\033[0m"
                , he.getRemoteAddress().getAddress().getHostAddress(), he.getRemoteAddress().getPort(), path[path.length - 1]));

        File file = new File(params.get(FILENAME));
        he.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + path[path.length - 1]);

        he.sendResponseHeaders(200, file.length());
        OutputStream outputStream = he.getResponseBody();
        Files.copy(file.toPath(), outputStream);
        outputStream.close();
    }
}
