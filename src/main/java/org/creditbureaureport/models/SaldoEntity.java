package org.creditbureaureport.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "saldo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaldoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ls", nullable = false)
    private String ls;

    @Column(name = "dats", nullable = false)
    private Date dats;

    @Column(name = "sums", nullable = false)
    private BigDecimal sums;

    @Column(name = "activate")
    private Byte activate;

    // Геттеры и сеттеры (или используй Lombok)

}
