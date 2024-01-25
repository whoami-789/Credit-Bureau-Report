package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "zalog_xranenie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogXranenie {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data_priem")
    private LocalDate data_priem;
    @Column(name = "data_vozvrat")
    private LocalDate data_vozvrat;

    @ManyToOne
    @JoinColumn(name = "kod_dog", referencedColumnName = "kod_dog")
    private Kredit kredit;
}

