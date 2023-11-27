package model.session;

import model.data.FileData;
import util.Config;

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
        if (Config.isRootRestricted() && file.getAbsolutePath().equals(System.getProperty("user.dir"))) {
            return "";
        }
        if (workDirectory.split("/").length == 1) {
            return "";
        }

        return "/directory/?folder=" + file.getParent().replaceAll("\\\\", "/");
    }

    // =================================================================================================================
    // Setter
    // =================================================================================================================

    /**
     * Setter for the current working directory.
     * @param workDirectory The current working directory.
     */
    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
        this.workDirectory = this.workDirectory.replaceAll("\\\\", "/");
    }

}
