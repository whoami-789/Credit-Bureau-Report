package org.creditbureaureport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "spr_rayon")
@Data
public class SprRayon {

    @Id
    private String kod;  // nchar(3)

    private String kodcb;  // nchar(2)
    private String namer;  // varchar(30)
    private String dopol;  // nchar(2)
    private Date datsaktiv;  // date
    private Date datsdeaktiv;  // date
    private String sost;  // nchar(10)
    private String nameu;  // varchar(30)

}
