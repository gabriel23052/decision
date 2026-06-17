package com.gabriel2305.parser;

public record Fragment(FragmentType type, String content) {
    Fragment(FragmentType type) {
        this(type, null);
    }

    @Override
    public String toString() {
        if(content == null) {
            return type().name() + "(null)";
        }
        return type().name() + "(\"" + content + "\")";
    }
}
