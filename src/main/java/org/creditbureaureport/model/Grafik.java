package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.creditbureaureport.customId.GrafikId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "grafik")
@IdClass(GrafikId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grafik {

    @Id
    @Column(name = "dats")
    private LocalDate dats;

    @Id
    @Column(name = "pog_kred")
    private BigDecimal pogKred;

    @Id
    @Column(name = "pog_proc")
    private BigDecimal pogProc;

    @Id
    @Column(name = "ostatok")
    private BigDecimal ostatok;

    @Id
    @Column(name = "mes", nullable = false)
    private Byte mes;

    @Id
    @Column(name = "sost")
    private Byte sost;

    @Id
    @Column(name = "numdog", nullable = false, columnDefinition = "nchar(12)")
    private String numdog;

    @ManyToOne
    @JoinColumn(name = "numdog", referencedColumnName = "numdog", insertable = false, updatable = false)
    private Kredit kredit;
}


