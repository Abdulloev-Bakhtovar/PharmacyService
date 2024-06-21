package ru.bakht.pharmacy.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileFormat {

    EXCEL(".xlsx"),
    PDF(".pdf");

    private final String extension;
}
