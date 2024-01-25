package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "zalog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kod", nullable = false)
    private Byte kod;

    @Column(name = "sums")
    private BigDecimal sums;

    @Column(name = "prim")
    private String prim;

    @Column(name = "dtime")
    private Date dtime;

    @Column(name = "numdog", nullable = false, columnDefinition = "char(12)")
    private String numdog;

    @Column(name = "sost")
    private Byte sost;

    @Column(name = "dats")
    private LocalDate dats;

    @Column(name = "dats_snyat")
    private LocalDate datsSnyat;

    @Column(name = "ls", nullable = false, columnDefinition = "nchar(20)")
    private String ls;

    @Column(name = "kod_cb")
    private String kodCb;

    @Column(name = "dopinfo", columnDefinition = "TEXT")
    private String dopinfo;

    @ManyToOne
    @JoinColumn(name = "numdog", referencedColumnName = "numdog", insertable = false, updatable = false)
    private Kredit kredit;
}
