package com.gabriel2305.parser;

import com.gabriel2305.exceptions.ParserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FragmentParserTest {

    private final String[] INVALID_STORIES = {
            "text [id] (\"content\", goto) ",
            "[id] inexistent (\"content\", goto) ",
            "text (\"content\", goto) [id] ",
            "[id] (\"content\", goto) text ",
            "(\"content\", goto) text [id] ",
            "(\"content\", goto) [id] text ",
            "[id] (\"content\", goto) ",
            "text (\"content\", goto) ",
            "[id] text ",
    };

    private final String[] VALID_STORIES = {
            "[id] text (\"content\", goto)",
            """
            [ida] text ("contenta", gotoa)
            [idb] text ("contentb", gotob)
            [idc] text ("contentc", gotoc)
            """
    };

    private List<Fragment> getFragments(String dhis) {
        DhisParser dhisParser = new DhisParser(dhis);
        return dhisParser.createFragments();
    }

    @Test
    @DisplayName("Should throw exception for invalid stories")
    void shouldThrowExceptionForInvalidDhis() {
        for(String invalidStories : INVALID_STORIES) {
            List<Fragment> fragments = getFragments(invalidStories);
            FragmentParser fragmentParser = new FragmentParser(fragments);
            assertThrows(ParserException.class, fragmentParser::createHistory);
        }
    }

    @Test
    @DisplayName("Should does not throw exception for valid stories")
    void shouldDoesNotThrowExceptionForInvalidDhis() {
        for(String validStories : VALID_STORIES) {
            List<Fragment> fragments = getFragments(validStories);
            FragmentParser fragmentParser = new FragmentParser(fragments);
            assertDoesNotThrow(fragmentParser::createHistory);
        }
    }
}
