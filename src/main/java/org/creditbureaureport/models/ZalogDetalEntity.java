package org.creditbureaureport.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "zalog_detal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogDetalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "kod_zalog", nullable = false)
    private Long kodZalog;

    @Column(name = "stroka")
    private String stroka;

    @Column(name = "detal", columnDefinition = "TEXT")
    private String detal;

    @Column(name = "sums")
    private BigDecimal sums;

    // Геттеры и сеттеры (или используй Lombok)

}

