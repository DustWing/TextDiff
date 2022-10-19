package org.example.diff;

public enum DiffType {
    INSERT,
    DELETE,
    EQUAL;

    public static boolean isDelete(DiffType type){
        return DELETE.equals(type);
    }
}
