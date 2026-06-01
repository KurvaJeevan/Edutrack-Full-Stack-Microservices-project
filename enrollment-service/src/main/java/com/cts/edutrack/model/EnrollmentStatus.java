package com.cts.edutrack.model;

public enum EnrollmentStatus {
    Active,
    Completed,
    Dropped;

    public static boolean isValid(String status) {
        for (EnrollmentStatus es : values()) {
            if (es.name().equalsIgnoreCase(status)) return true;
        }
        return false;
    }

    public static String normalize(String status) {
        if (status == null) return null;
        for (EnrollmentStatus es : values()) {
            if (es.name().equalsIgnoreCase(status)) return es.name();
        }
        return status; // fallback
    }
}