package model.session;

import model.data.FileData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a user session.
 * It contains the ip address of the user and the current working directory.
 * It also contains a list of all files in the current working directory.
 * <br><br>
 * This class is used to store the data of a user session.
 * It is also used to generate the html code for the directory page.
 * <br><br>
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class UserSession {

    /**
     * Defines if the root directory is limited.
     */
    private static boolean ROOT_LIMITED = true;

    /**
     * The ip address of the user.
     */
    private final String ip;

    /**
     * The current working directory.
     */
    private String workDirectory;

    /**
     * List of all files in the current working directory.
     */
    private final List<FileData> files;

    /**
     * Creates a new UserSession instance.
     * @param ip The ip address of the user.
     */
    public UserSession(String ip) {
        this.ip = ip;
        files = new ArrayList<>();
    }

    /**
     * Generates the html code for the directory page.
     * @return The html code for the directory page.
     */
    public String getFileHtml() {
        StringBuilder html = new StringBuilder();
        String position = null;
        for (FileData file : files) {
            if (position == null || !position.equals(file.getName().substring(0, 1))) {
                position = file.getName().substring(0, 1);
                html.append("<p class=\"position\">").append(position).append("</p>\n");
            }
            html.append("<dl class=\"table\">\n");

            String name;
            if (file.getType().equals("Directory")) {
                name = String.format("<a class=\"dir\" href=\"%s\">\uD83D\uDCC1 %s</a>"
                    , "/directory/?folder=" + workDirectory.replaceAll("\\\\", "/") + "/"+file.getName(), file.getName());
            } else {
                name = String.format("<a class=\"download\" href=\"%s\">\uD83D\uDCBE </a>", "/download/?filename="
                        + workDirectory.replaceAll("\\\\", "/") + "/"+file.getName()) + file.getName();
            }

            html.append("<dt>").append(name).append("</dt>\n");
            if (file.getSize() > 0) {
                html.append("<dd>").append(convertSize(file.getSize())).append("</dd>\n");
            }
            html.append("</dl>\n");
        }
        if (files.isEmpty()) {
            html.append("<h1>Empty Directory</h1>");
        }
        return html.toString();
    }

    /**
     * Clears the list of all files in the current working directory.
     */
    public void clear() {
        files.clear();
    }

    // =================================================================================================================
    // Getter
    // =================================================================================================================

    /**
     * Getter for the current working directory.
     * @return The current working directory.
     */
    public String getWorkDirectory() {
        return workDirectory;
    }

    /**
     * Getter for the ip address of the user.
     * @return The ip address of the user.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Getter for the list of all files in the current working directory.
     * @return The list of all files in the current working directory.
     */
    public List<FileData> getFiles() {
        return files;
    }

    /**
     * Getter for the parent directory.
     * @return The parent directory or empty String.
     */
    public String getReturnWorkingDirectory() {
        File file = new File(workDirectory);
        if (ROOT_LIMITED && file.getAbsolutePath().equals(System.getProperty("user.dir"))) {
            return "";
        }
        if (workDirectory.split("/").length == 1) {
            return "";
        }

        return "/directory/?folder=" + file.getParent().replaceAll("\\\\", "/");
    }

    /**
     * Getter for root limited.
     * @return true, if root is limited.
     */
    public static boolean isRootLimited() {
        return ROOT_LIMITED;
    }

    // =================================================================================================================
    // Setter
    // =================================================================================================================

    /**
     * Setter for root limited.
     * @param rootLimited true, if root should be limited.
     */
    public static void setRootLimited(boolean rootLimited) {
        ROOT_LIMITED = rootLimited;
    }

    /**
     * Setter for the current working directory.
     * @param workDirectory The current working directory.
     */
    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    // =================================================================================================================
    // Private Methods
    // =================================================================================================================

    /**
     * Converts the size of a file to a human-readable format.
     * @param size The size of the file in bytes.
     * @return The size of the file.
     */
    private static String convertSize(long size) {
        if (size < 1024) {
            return size + " byte";
        } else if (size < 1024 * 1024) {
            return size / 1024 + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return size / (1024 * 1024) + " MB";
        } else {
            return size / (1024 * 1024 * 1024) + " GB";
        }
    }

}
