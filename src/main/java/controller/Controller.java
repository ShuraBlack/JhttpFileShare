package controller;

import model.data.FileData;
import model.interpreter.ArgsInterpreter;
import model.server.Server;
import model.session.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main component for the JhttpFileShare application.<br><br>
 * This class is the entry point and will initialize the server and the command line interface.
 *
 * @version 0.1.4
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class Controller {

    /**
     * Logger for the Controller class.
     */
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    /**
     * The server instance.
     */
    private final Server server;

    /**
     * List of all active sessions.
     */
    private static final List<UserSession> SESSIONS = new ArrayList<>();

    /**
     * Entry point of the application.
     *
     * @param args Command line arguments.
     */
    public Controller(String[] args) {
        Config.init();
        ArgsInterpreter.interpret(args);
        server = new Server();

        FileData.getDirectoryStructure(Config.getRootDirectory());

        new Thread(() -> {
            if (Config.isVerbose())
                LOGGER.info("Start Command Line Interface...");

            System.out.println(
                      "==================================================================================\n"
                    + "= JhttpFileShare - Version 0.1.4                                                 =\n"
                    + "= Code by ShuraBlack                                                             =\n"
                    + "==================================================================================\n"
                    + getProperties() + "\n"
                    + "==================================================================================\n"
                    + "= exit -> will close the server                                                  =\n"
                    + "==================================================================================\n");

            Scanner input = new Scanner(System.in);
            while (true) {
                String command = input.nextLine();
                if (command.equals("exit")) {
                    server.stop();
                    LOGGER.info("Server closed and CLI will be shutdowned...");
                    System.exit(0);
                    break;
                }
            }
        }, "CLI").start();
    }

    // =================================================================================================================
    // Getter
    // =================================================================================================================

    /**
     * Getter for session list.
     * @return List of all active sessions.
     */
    public static List<UserSession> getSessions() {
        return SESSIONS;
    }

    // =================================================================================================================
    // Private Methods
    // =================================================================================================================

    /**
     * Returns the properties of the server.
     * @return The properties
     */
    private static String getProperties() {
        return String.format("= Verbose: %-27s IP: %-59s =%n"
                        + "= Root Restriction: %-18s Port: %-57s =%n"
                        + "= Upload Allowed: %-20s Thread Pool Size: %-45s =",
                colorize(Config.isVerbose()),
                "\033[0;36m" + Config.getIpAddress() + "\033[0m",
                colorize(Config.isRootRestricted()),
                "\033[0;36m" + Config.getPort() + "\033[0m",
                colorize(Config.isUploadAllowed()),
                "\033[0;36m" + Config.getThreadPoolSize() + "\033[0m");
    }

    /**
     * Colorizes the given boolean.
     * @param state The boolean to colorize.
     * @return The colorized boolean.
     */
    private static String colorize(boolean state) {
        if (state) {
            return "\033[0;32m" + state + "\033[0m";
        } else {
            return "\033[0;31m" + state + "\033[0m";
        }
    }
}
