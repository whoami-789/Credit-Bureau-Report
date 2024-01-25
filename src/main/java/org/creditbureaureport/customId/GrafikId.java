package org.creditbureaureport.customId;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GrafikId implements Serializable {
    private LocalDate dats;
    private BigDecimal pogKred;
    private BigDecimal pogProc;
    private BigDecimal ostatok;
    private Byte mes;
    private Byte sost;
    private String numdog;
}

