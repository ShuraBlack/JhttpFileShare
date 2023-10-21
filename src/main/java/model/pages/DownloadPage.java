package model.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import util.Zip;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.zip.ZipOutputStream;

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
        if (params.isEmpty() || !params.containsKey(FILENAME) || !params.get(FILENAME).contains(Config.getRootDirectory())) {
            return;
        }

        File file = new File(params.get(FILENAME));
        ZipOutputStream zipStream = null;
        String[] path = params.get(FILENAME).split("/");
        if (file.isDirectory()) {
            LOGGER.info(String.format("%nExchange for \033[0;33m</Download>\033[0m...%nfrom %s:%s%narchive \033[0;33m<%s>\033[0m"
                    , he.getRemoteAddress().getAddress().getHostAddress(), he.getRemoteAddress().getPort(), file.getName()));

            zipStream = Zip.zipFolder(file.getName(), file.getName());
            file = new File(file.getParent() + "/" + path[path.length - 1] + ".zip");

            he.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + path[path.length - 1] + ".zip");
            he.getResponseHeaders().set("Content-Type", "application/zip");
        } else {
            if (Config.isVerbose())
                LOGGER.info(String.format("%nExchange for \033[0;33m</Download>\033[0m...%nfrom %s:%s%nfile \033[0;33m<%s>\033[0m"
                        , he.getRemoteAddress().getAddress().getHostAddress(), he.getRemoteAddress().getPort(), file.getName()));

            he.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + path[path.length - 1]);
        }
        he.sendResponseHeaders(200, file.length());
        OutputStream outputStream = he.getResponseBody();
        Files.copy(file.toPath(), outputStream);
        outputStream.close();

        if (file.getName().endsWith(".zip")) {
            zipStream.close();
            if (!file.delete()) {
                LOGGER.warn("Could not delete temp archive \033[0;33m<{}>\033[0m", file.getName());
            }
        }
    }
}
