package model.pages;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.Controller;
import model.session.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import util.PageLoader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static model.pages.DirectoryPage.populateContent;

/**
 * This class is responsible for the upload page request.
 * <br><br>
 * Thanks to the great work of
 * <a href="https://github.com/shuntaochen/ArticlesCollections/blob/master/formdatahandlersun.java">shuntaochen</a>
 *
 * @version 0.1.3
 * @since 21.Oct.2023
 * @author ShuraBlack
 */
public class UploadPage implements HttpHandler {

    /**
     * Logger for the UploadPage class.
     */
    private static final Logger LOGGER = LogManager.getLogger(UploadPage.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (!Config.isUploadAllowed()) {
            return;
        }

        Headers headers = httpExchange.getRequestHeaders();
        String contentType = headers.getFirst("Content-Type");
        if(contentType.startsWith("multipart/form-data")){
            String boundary = contentType.substring(contentType.indexOf("boundary=")+9);
            byte[] boundaryBytes = ("\r\n--" + boundary).getBytes(StandardCharsets.UTF_8);
            byte[] payload = getInputAsBinary(httpExchange.getRequestBody());
            ArrayList<MultiPart> list = new ArrayList<>();

            List<Integer> offsets = searchBytes(payload, boundaryBytes, 0, payload.length - 1);
            offsets.add(0, 0);
            for(int idx=0;idx<offsets.size();idx++){
                int startPart = offsets.get(idx);
                int endPart = payload.length;
                if(idx<offsets.size()-1){
                    endPart = offsets.get(idx+1);
                }
                byte[] part = Arrays.copyOfRange(payload,startPart,endPart);

                int headerEnd = indexOf(part,"\r\n\r\n".getBytes(StandardCharsets.UTF_8),0,part.length-1);
                if(headerEnd>0) {
                    MultiPart p = new MultiPart();
                    byte[] head = Arrays.copyOfRange(part, 0, headerEnd);
                    String header = new String(head);

                    int nameIndex = header.indexOf("\r\nContent-Disposition: form-data; name=");
                    if (nameIndex >= 0) {
                        int startMarker = nameIndex + 39;

                        int fileNameStart = header.indexOf("; filename=");
                        if (fileNameStart >= 0) {
                            String filename = header.substring(fileNameStart + 11, header.indexOf("\r\n", fileNameStart));
                            p.filename = filename.replace('"', ' ').replace('\'', ' ').trim();
                        } else {
                            int endMarker = header.indexOf("\r\n", startMarker);
                            if (endMarker == -1)
                                endMarker = header.length();
                        }
                    } else {
                        continue;
                    }

                    int typeIndex = header.indexOf("\r\nContent-Type:");
                    if (typeIndex >= 0) {
                        int startMarker = typeIndex + 15;
                        int endMarker = header.indexOf("\r\n", startMarker);
                        if (endMarker == -1)
                            endMarker = header.length();
                    }

                    p.bytes = Arrays.copyOfRange(part, headerEnd + 4, part.length);
                    list.add(p);
                }
            }

            handle(httpExchange,list);
        }else{
            handle(httpExchange,null);
        }
    }

    /**
     * Handles the upload request.
     * @param he The HttpExchange.
     * @param parts The parts of the request.
     * @throws IOException If an I/O error occurs.
     */
    public void handle(HttpExchange he,List<MultiPart> parts) throws IOException {
        Optional<UserSession> session = Controller.getSessions().stream().filter(s -> s.getIp().equals(he.getRemoteAddress().getAddress().getHostAddress())).findFirst();
        UserSession userSession;
        if (session.isEmpty()) {
            return;
        } else {
            userSession = session.get();
        }

        if (parts != null && !parts.isEmpty()) {
            Controller.updateDriverData();
            for (MultiPart part : parts) {
                try (FileOutputStream stream = new FileOutputStream(userSession.getWorkDirectory() + "/" + part.filename)) {
                    stream.write(part.bytes);
                }
                if (Config.isVerbose())
                    LOGGER.info(String.format("%nExchange for \033[0;33m</upload>\033[0m...%nfrom %s:%s%nfile \033[0;33m<%s>\033[0m"
                            , he.getRemoteAddress().getAddress().getHostAddress(), he.getRemoteAddress().getPort(), part.filename));
            }
        }

        String response = populateContent(userSession, PageLoader.getPage("directory"));
        he.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Returns the input stream as binary.
     * @param requestStream The input stream.
     * @return The input stream as binary.
     */
    public static byte[] getInputAsBinary(InputStream requestStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[100000];
            int bytesRead=0;
            while ((bytesRead = requestStream.read(buf)) != -1){
                bos.write(buf, 0, bytesRead);
            }
            requestStream.close();
            bos.close();
        } catch (IOException e) {
            LOGGER.error("Error while decoding http input stream", e);
        }
        return bos.toByteArray();
    }

    /**
     * Searches the given bytes for the search bytes.
     * @param srcBytes The bytes to search in.
     * @param searchBytes The bytes to search for.
     * @param searchStartIndex The start index of the search.
     * @param searchEndIndex The end index of the search.
     * @return The list of found indexes.
     */
    public List<Integer> searchBytes(byte[] srcBytes, byte[] searchBytes, int searchStartIndex, int searchEndIndex) {
        final int destSize = searchBytes.length;
        final List<Integer> positionIndexList = new ArrayList<>();
        int cursor = searchStartIndex;
        while (cursor < searchEndIndex + 1) {
            int index = indexOf(srcBytes, searchBytes, cursor, searchEndIndex);
            if (index >= 0) {
                positionIndexList.add(index);
                cursor = index + destSize;
            } else {
                cursor++;
            }
        }
        return positionIndexList;
    }

    /**
     * Returns the index of the search bytes in the source bytes.
     * @param srcBytes The source bytes.
     * @param searchBytes The search bytes.
     * @param startIndex The start index of the search.
     * @param endIndex The end index of the search.
     * @return The index of the search bytes in the source bytes.
     */
    public int indexOf(byte[] srcBytes, byte[] searchBytes, int startIndex, int endIndex) {
        if (searchBytes.length == 0 || (endIndex - startIndex + 1) < searchBytes.length) {
            return -1;
        }
        int maxScanStartPosIdx = srcBytes.length - searchBytes.length;
        final int loopEndIdx;
        if (endIndex < maxScanStartPosIdx) {
            loopEndIdx = endIndex;
        } else {
            loopEndIdx = maxScanStartPosIdx;
        }
        int lastScanIdx = -1;
        label:
        for (int i = startIndex; i <= loopEndIdx; i++) {
            for (int j = 0; j < searchBytes.length; j++) {
                if (srcBytes[i + j] != searchBytes[j]) {
                    continue label;
                }
                lastScanIdx = i + j;
            }
            if (endIndex < lastScanIdx || lastScanIdx - i + 1 < searchBytes.length) {
                return -1;
            }
            return i;
        }
        return -1;
    }

    /**
     * This class represents a multipart.
     */
    public static class MultiPart {
        private String filename;
        private byte[] bytes;
    }
}
