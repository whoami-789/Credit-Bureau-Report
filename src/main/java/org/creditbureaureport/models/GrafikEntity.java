package org.creditbureaureport.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "grafik")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrafikEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "dats")
    private Date dats;

    @Column(name = "pog_kred")
    private BigDecimal pogKred;

    @Column(name = "pog_proc")
    private BigDecimal pogProc;

    @Column(name = "ostatok")
    private BigDecimal ostatok;

    @Column(name = "mes", nullable = false)
    private Byte mes;

    @Column(name = "sost")
    private Byte sost;

    @Column(name = "numdog", nullable = false)
    private String numdog;

    // Геттеры и сеттеры (или используй Lombok)
}

