package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "saldo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Saldo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ls", nullable = false)
    private String ls;

//    @ManyToOne
//    @JoinColumn(name = "ls", referencedColumnName = "lscor", insertable=false, updatable=false)
//    private Dokument dokument;

    @Column(name = "dats", nullable = false)
    private LocalDate dats;

    @Column(name = "sums", nullable = false)
    private BigDecimal sums;

    @Column(name = "activate")
    private Byte activate;

}
