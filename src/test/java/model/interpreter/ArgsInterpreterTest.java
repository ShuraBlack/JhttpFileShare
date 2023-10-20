package model.interpreter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Config;

import static org.junit.jupiter.api.Assertions.*;

class ArgsInterpreterTest {

    @BeforeAll
    static void setup() {
        Config.init();
    }

    @Test
    @DisplayName("interpret() - valid input - set port")
    void interpret_portSet_validInput() {
        final String[] args = {"-p=8080"};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getPort(), 8080);
    }

    @Test
    @DisplayName("interpret() - invalid input - default port")
    void interpret_portSet_invalidInput() {
        final String[] args = {"-p=xxxx"};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getPort(), 80);
    }

    @Test
    @DisplayName("interpret() - empty input - default port")
    void interpret_portSet_nullInput() {
        final String[] args = {"-p="};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getPort(), 80);
    }

    @Test
    @DisplayName("interpret() - valid input - set ip")
    void interpret_ipSet_validInput() {
        final String[] args = {"-ip=lo"};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getIpAddress(), "127.0.0.1");
    }

    @Test
    @DisplayName("interpret() - invalid input - default ip")
    void interpret_ipSet_invalidInput() {
        final String[] args = {"-ip=invalid"};

        ArgsInterpreter.interpret(args);

        assertEquals("0.0.0.0", Config.getIpAddress());
    }

    @Test
    @DisplayName("interpret() - valid input - set verbose")
    void interpret_verboseSet_validInput() {
        final String[] args = {"-verbose"};

        ArgsInterpreter.interpret(args);

        assertTrue(Config.isVerbose());
    }

    @Test
    @DisplayName("interpret() - valid input - set root limit")
    void interpret_rootLimitSet_validInput() {
        final String[] args = {"-nolimit"};

        ArgsInterpreter.interpret(args);

        assertFalse(Config.isRootRestricted());
    }

    @Test
    @DisplayName("interpret() - valid input - set threads")
    void interpret_threadsSet_validInput() {
        final String[] args = {"-threads=1"};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getThreadPoolSize(), 1);
    }

    @Test
    @DisplayName("interpret() - invalid input - default threads")
    void interpret_threadsSet_invalidInput() {
        final String[] args = {"-threads=invalid"};

        ArgsInterpreter.interpret(args);

        assertEquals(3, Config.getThreadPoolSize());
    }

    @Test
    @DisplayName("interpret() - valid input - set root dir")
    void interpret_rootSet_validInput() {
        final String[] args = {"-root=C:/Users/"};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getRootDirectory(), "C:/Users/");
    }

    @Test
    @DisplayName("interpret() - invalid input - default root dir")
    void interpret_rootSet_invalidInput() {
        final String[] args = {"-root=invalid"};

        ArgsInterpreter.interpret(args);

        assertEquals(Config.getRootDirectory(), System.getProperty("user.dir").replaceAll("\\\\","/"));
    }
}
