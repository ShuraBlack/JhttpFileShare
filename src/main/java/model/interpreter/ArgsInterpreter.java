package model.interpreter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;

import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Interpreter for the command line arguments.
 * <br><br>
 * This class will interpret the command line arguments and set the corresponding values.
 * <br><br>
 * The following arguments are available:
 * <ul>
 *     <li>-ip</li>
 *     <li>-p, -port</li>
 *     <li>-v, -verbose</li>
 *     <li>-h, -help</li>
 *     <li>-nolimit</li>
 *     <li>-threads</li>
 *     <li>-root</li>
 * </ul>
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class ArgsInterpreter {

    /**
     * Logger for the ArgsInterpreter class.
     */
    private static final Logger LOGGER = LogManager.getLogger(ArgsInterpreter.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private ArgsInterpreter() {
    }

    // =================================================================================================================
    // Public Methods
    // =================================================================================================================

    /**
     * Interprets the command line arguments.
     * @param args Command line arguments.
     */
    public static void interpret(String[] args) {
        Map<String, String> params = new HashMap<>();

        for (String arg : args) {
            String[] split = arg.split("=");
            if (params.containsKey(split[0])) {
                continue;
            }
            if (split.length == 1) {
                params.put(split[0], null);
            } else {
                params.put(split[0], split[1]);
            }
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case "-ip":
                    interpretIP(entry.getValue());
                    break;
                case "-port":
                case "-p":
                    interpretPort(entry.getValue());
                    break;
                case "-verbose":
                case "-v":
                    interpretVerbose();
                    break;
                case "-nr":
                    interpretRootRestriction();
                    break;
                case "-threads":
                    interpretThreads(entry.getValue());
                    break;
                case "-root":
                    interpretRoot(entry.getValue());
                        break;
                case "-help":
                case "-h":
                    interpretHelp();
                    break;
                default:
                    LOGGER.warn("Unknown Argument {}!", entry.getKey());
                    break;
            }
        }
    }

    // =================================================================================================================
    // Private Methods
    // =================================================================================================================

    /**
     * Prints out all available network interfaces or sets the IP Address to the given network name.
     * @param value The network name or null.
     */
    private static void interpretIP(String value) {
        try {
            if (value == null) {
                LOGGER.info("Network Interfaces:\n(Search for the the local network name you use on the devices)\n");
                Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
                for (NetworkInterface netint : Collections.list(nets))
                    displayInterfaceInformation(netint);
                System.exit(0);
            } else {
                NetworkInterface netint = NetworkInterface.getByName(value);
                if (netint == null) {
                    LOGGER.warn("Network Interface not found! Use default IP Address 0.0.0.0");
                } else {
                    Optional<InetAddress> inetAddress = Collections.list(netint.getInetAddresses())
                            .stream()
                            .filter(Inet4Address.class::isInstance)
                            .findFirst();
                    if (inetAddress.isPresent()) {
                        Config.set("IP_ADDRESS", inetAddress.get().getHostAddress());
                        LOGGER.info("Set IP Address to {}", Config.getIpAddress());
                    } else {
                        LOGGER.warn("No IPv4 Address found! Use default IP Address");
                    }
                }
            }
        } catch (SocketException e) {
            LOGGER.error("Couldnt get Network Interfaces!", e);
        }
    }

    /**
     * Sets the port to the given value.
     * @param value The port or null.
     */
    private static void interpretPort(String value) {
        if (value == null || value.isEmpty()) {
            LOGGER.warn("Port not found! Use default port 80.");
            return;
        }

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn( "Port is not a number! Use default port 80.", e);
            return;
        }

        Config.set("PORT", value);
        LOGGER.info("Set Port to {}", Config.getPort());
    }

    /**
     * Sets the verbose mode to the given value.
     */
    private static void interpretVerbose() {
        Config.set("VERBOSE", "true");
        LOGGER.info("Verbose got enabled");
    }

    /**
     * Sets the root limit to the given value.
     */
    private static void interpretRootRestriction() {
        Config.set("ROOT_RESTRICTED", "false");
        LOGGER.info("Root folder restriction got disabled");
    }

    /**
     * Sets the thread pool size to the given value.
     * @param value The size or null.
     */
    private static void interpretThreads(String value) {
        if (value == null || value.isEmpty()) {
            LOGGER.warn("Thread Pool Size not found! Use default thread pool size 3.");
            return;
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Thread Pool Size is not a number! Use default thread pool size 3.", e);
            return;
        }
        Config.set("THREAD_POOL_SIZE", value);
        LOGGER.info("Set Thread Pool Size to {}", Config.getThreadPoolSize());
    }

    /**
     * Sets the root to the given value.
     * @param value The path or null.
     */
    private static void interpretRoot(String value) {
        if (value == null || value.isEmpty() || !Pattern.matches("([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?",value)) {
            LOGGER.warn("Root not found! Use default root.");
            return;
        }
        Config.set("ROOT_DIRECTORY", value.replaceAll("\\\\", "/"));
        LOGGER.info("Set Root to {}", Config.getRootDirectory());
    }

    /**
     * Prints out the help.
     */
    private static void interpretHelp() {
        System.out.println("JhttpFileShare Server 0.1.0\n");
        System.out.println("USAGE:\n\tjava -jar JhttpFileShare.jar [options/flags]\n");
        System.out.println("FLAGS:");
        System.out.println("\t-ip\t\t\t\t\tShows all Network Interfaces\n"
                + "\t-v, -verbose\t\t\t\tEnables verbose mode (more informations Server-side)\n"
                + "\t-nr\t\t\t\tDisables the root folder restriction (Access entire file browser)\n"
                + "\t-h, -help\t\t\t\tShows this help\n");
        System.out.println("OPTIONS:\n"
                + "\t-ip=<network_name>\t\t\tSets the IP Address to the given network name [default: 0.0.0.0]\n"
                + "\t-p, -port=<port>\t\t\tSets the Port to the given port [default: 80]\n"
                + "\t-threads=<size>\t\t\t\tSets the Thread Pool Size to the given size [default: 3]\n"
                + "\t-root=<path>\t\t\t\tSets the root folder [default: user.dir]\n");
        System.exit(0);
    }

    /**
     * Prints out the network interfaces.
     * @param netint The network interface.
     */
    private static void displayInterfaceInformation(NetworkInterface netint) {
        StringBuilder sb = new StringBuilder();
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if (inetAddress instanceof Inet6Address) continue;
            sb.append(netint.getName()).append(" - ").append(inetAddress.getHostAddress()).append("\n");
        }
        System.out.printf(sb.toString());
    }
}
