package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zalog_xranenie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogXranenie {

    @Id
    @Column(name = "kod_dog", nullable = false)
    private Integer kodDog;

    @Column(name = "num_korobki", nullable = false)
    private Integer numKorobki;

    @Column(name = "status", nullable = false)
    private Byte status;
}

