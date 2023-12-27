package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zalog_detal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogDetal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kod", nullable = false)
    private Integer kod;

    @Column(name = "kod_zalog", nullable = false)
    private Long kodZalog;

    @Column(name = "stroka")
    private String stroka;

    @Column(name = "detal", columnDefinition = "TEXT")
    private String detal;
}

