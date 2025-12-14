package com.philem.philem.domain.model.entity;

public enum SensorFormat {
    FULL_FRAME("Full Frame"),
    APS_C("APS-C"),
    MICRO_FOUR_THIRDS("Micro Four Thirds"),
    MEDIUM_FORMAT("Medium Format"),
    ONE_INCH("1 inch");

    private final String label;
    SensorFormat(String label) { this.label = label; }
    public String label() { return label; }
}
