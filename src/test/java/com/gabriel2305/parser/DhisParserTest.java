package com.gabriel2305.parser;

import com.gabriel2305.exceptions.ParserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DhisParserTest {

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
            "[[invalidNodeId]",
            "[[invalidNodeId]]",
            "[validNodeId] inval1dtype ",
            "[validNodeId] iNvalid ",
            "[validNodeId] validnodetype (\"\") ",
            "[validNodeId] validnodetype (\"Invalidtext) ",
            "[validNodeId] validnodetype (\"Invalidtext\"",
    };

    private final String[] VALID_DHIS = {
            "[validNodeId]",
            "   [validNodeId]",
            "[validNodeId]  ",
            "[valid_NodeId]",
            "[valid_N0de1d]",
            "[validNodeId]]",
            "[validNodeId] validnodetype ",
            "[validNodeId]  validnodetype ",
            "[validNodeId]validnodetype ",
            "[validNodeId]validnodetype  ",
            "[validNodeId]v ",
            "[validNodeId] validnodetype ",
            "[validNodeId] validnodetype (\"Valid Text\") ",
            "[validNodeId] validnodetype (\"V\")",
            "[validNodeId] validnodetype(\"Valid text\")",
            "[validNodeId]validnodetype(\"Valid text\") ",
            "[validNodeId]validnodetype(\"Valid text\")[validNodeId2]validnodetypeb(\"valid text\") ",
            """
            [validNodeId]
            validnodetype
            ("Valid text")

            [validNodeId2]
            validnodetypeb
            ("valid text")
            """,
            "[validNodeId] validnodetype (\"Valid Text\",\"Valid Text 2\")",
            "[validNodeId] validnodetype (\"Valid Text\", validnodeid)",
            "[validNodeId] validnodetype (\"Valid Text\", validnodeid,,,)",
    };

    @Test
    @DisplayName("Should throw exception for invalid dhis")
    void shouldThrowExceptionForInvalidDhis() {
        for(String invalidDhis : INVALID_DHIS) {
            DhisParser dhisParser = new DhisParser();
            dhisParser.setDhis(invalidDhis);
            assertThrows(ParserException.class, dhisParser::createFragments);
        }
    }

    @Test
    @DisplayName("Should does not throw exception for valid dhis")
    void shouldDoesNotThrowExceptionForValidDhis() {
        for(String validDhis : VALID_DHIS) {
            DhisParser dhisParser = new DhisParser();
            dhisParser.setDhis(validDhis);
            assertDoesNotThrow(dhisParser::createFragments);
        }
    }

}
