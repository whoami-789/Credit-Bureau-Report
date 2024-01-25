package org.creditbureaureport.dto;

import lombok.Data;
import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.model.Dokument;
import org.creditbureaureport.model.Kredit;

@Data
public class CombinedDataDTO {
    private Dokument dok;
    private Kredit kredit;
    private AzolikFiz azolikFiz;

    // Конструкторы, геттеры и сеттеры
}
