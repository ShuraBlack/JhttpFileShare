package model.data;

import util.Config;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all important information about a file.<br><br>
 * This includes the name, type and size of the file.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class FileData {

    /**
     * The name of the file.
     */
    private final String name;

    /**
     * The type of the file.
     * This can be either "Directory" or "File".
     */
    private final String type;

    /**
     * The size of the file.
     */
    private final long size;

    /**
     * The last modified date of the file.
     */
    private final long modified;

    /**
     * Creates a new FileData object with the given parameters.
     * @param name The name of the file.
     * @param type The type of the file.
     * @param size The size of the file.
     */
    public FileData(String name, String type, long size, long modified) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.modified = modified;
    }

    /**
     * Loads all files from the given path and returns them as a list.
     * @param path The absolute path to the directory.
     * @return A list of all files in the given directory.
     */
    public static List<FileData> loadFiles(String path) {
        List<FileData> files = new ArrayList<>();
        File file = new File(path);

        List<File> filesList = Arrays
                .stream(Objects.requireNonNull(file.listFiles()))
                .sorted(Comparator.comparing(a -> a.getName().toLowerCase()))
                .collect(Collectors.toList());

        for (File f : filesList) {
            files.add(new FileData(f.getName(), f.isDirectory() ? "Directory" : "File", f.length(), f.lastModified()));
        }
        return files;
    }

    /**
     * Creates a directory structure of the given path and saves it in the ROOT_TREE variable.
     * @param path The absolute path to the directory.
     */
    public static void getDirectoryStructure(String path) {
        StringBuilder builder = new StringBuilder();
        addFile(new File(path), 0, builder);
        Config.set("ROOT_STRUCTURE", builder.toString().replace("null",""));
    }

    /**
     * Adds a file to the directory structure. This method runs recursively.
     * @param file The file to add.
     * @param depth The depth of the file in the directory structure.
     */
    private static void addFile(File file, int depth, StringBuilder builder) {
        builder.append("  ".repeat(depth)).append("> ").append(file.getName()).append("<br>");

        for (File f : file.listFiles()) {
            if (f.isDirectory() && !f.isHidden() && f.getName().length() > 1) {
                addFile(f, depth + 1, builder);
            }
        }
    }

    // =================================================================================================================
    // Getter
    // =================================================================================================================

    /**
     * Getter for name
     * @return The name of the file.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for type
     * @return The type of the file.
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for size
     * @return The size of the file.
     */
    public long getSize() {
        return size;
    }

    /**
     * Getter for modified
     * @return The last modified date of the file.
     */
    public long getModified() {
        return modified;
    }

}
