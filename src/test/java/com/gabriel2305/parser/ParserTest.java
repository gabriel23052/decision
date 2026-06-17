package com.gabriel2305.parser;

import com.gabriel2305.exceptions.ParserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    private final String[] INVALID_DHIS = {
            "abc[validNodeId]",
            "[]",
            "[ ]",
            "123[validNodeId]",
            " abc[validNodeId]",
            " 123[validNodeId]",
            " [ invalidNodeId]",
            " [invalid NodeId]",
            " [invalidNodeId ]",
            " [inval|dNodeId]",
            " [invalid-nodeId]",
            " [invalídNodeId]",
    };

    private final String[] VALID_DHIS = {
            "[validNodeId]",
            "   [validNodeId]",
            "[validNodeId]  ",
            "[valid_NodeId]",
            "[valid_N0de1d]",
    };

    @Test
    @DisplayName("Should throw exception for invalid dhis")
    void shouldThrowExceptionForInvalidDhis() {
        for(String invalidDhis : INVALID_DHIS) {
            Parser parser = new Parser(invalidDhis);
            assertThrows(ParserException.class, parser::createFragments);
        }
    }

    @Test
    @DisplayName("Should does not throw exception for valid dhis")
    void shouldDoesNotThrowExceptionForValidDhis() {
        for(String validDhis : VALID_DHIS) {
            Parser parser = new Parser(validDhis);
            assertDoesNotThrow(parser::createFragments);
        }
    }

}
