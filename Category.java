package com.ember.client.module;

public enum Category {
    RENDER("RENDER"),
    HUD("HUD"),
    MOVEMENT("MOVEMENT"),
    UTILITY("UTILITY");

    public final String label;

    Category(String label) {
        this.label = label;
    }
}
