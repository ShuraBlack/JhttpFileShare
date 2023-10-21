package util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This class is used to zip files.
 *
 * @version 0.1.2
 * @since 20.Oct.2023
 * @autor ShuraBlack
 */
public class Zip {

    /**
     * Private constructor to prevent instantiation.
     */
    private Zip() { }

    /**
     * Zips the given file.
     * @param sourceFile The file to zip.
     * @param destinationFile The destination of the zip file.
     * @throws IOException If an I/O error occurs.
     */
    public static ZipOutputStream zipFolder(String sourceFile, String destinationFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(destinationFile + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(sourceFile);
        zipSubFiles(fileToZip, fileToZip.getName(), zipOut);

        return zipOut;
    }

    /**
     * Private Method for recursively zipping files.
     * @param fileToZip The file to zip.
     * @param fileName The name of the file.
     * @param zipOut The zip output stream.
     * @throws IOException
     */
    private static void zipSubFiles(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipSubFiles(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
