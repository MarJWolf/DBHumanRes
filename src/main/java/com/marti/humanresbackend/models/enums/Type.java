package com.marti.humanresbackend.models.enums;

public enum Type {
    Paid,
    Unpaid,
    Special;

    public static String getTranslationBG(Type t) {
        return switch (t) {
            case Paid -> "платен";
            case Unpaid -> "неплатен";
            case Special -> "специален";
        };
    }
}
