package org.creditbureaureport.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "zalog_xranenie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogXranenieEntity {

    @Id
    @Column(name = "num_dog", nullable = false)
    private Integer numDog;

    @Column(name = "num_korobki", nullable = false)
    private Integer numKorobki;

    @Column(name = "status", nullable = false)
    private Byte status;

    // Геттеры и сеттеры (или используй Lombok)

}

