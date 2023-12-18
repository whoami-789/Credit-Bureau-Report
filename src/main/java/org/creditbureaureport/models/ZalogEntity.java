package org.creditbureaureport.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "zalog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "kod_zalog", nullable = false)
    private Byte kodZalog;

    @Column(name = "sums")
    private BigDecimal sums;

    @Column(name = "prim")
    private String prim;

    @Column(name = "dtime")
    private Date dtime;

    @Column(name = "numdog")
    private String numdog;

    @Column(name = "sost")
    private Byte sost;

    @Column(name = "dats")
    private Date dats;

    @Column(name = "dats_snyat")
    private Date datsSnyat;

    @Column(name = "ls")
    private String ls;

    @Column(name = "kod_cb")
    private String kodCb;

    @Column(name = "dopinfo", columnDefinition = "TEXT")
    private String dopinfo;

    // Геттеры и сеттеры (или используй Lombok)

}
