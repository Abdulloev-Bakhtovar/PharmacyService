package ru.bakht.pharmacy.service.enums;

public enum Format {
    EXCEL("xlsx"),
    PDF("pdf");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
