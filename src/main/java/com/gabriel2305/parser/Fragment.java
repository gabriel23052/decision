package com.gabriel2305.parser;

public record Fragment(FragmentType type, String content) {
    Fragment(FragmentType type) {
        this(type, null);
    }

    @Override
    public String toString() {
        return type().name() + "(" + content + ")";
    }
}
