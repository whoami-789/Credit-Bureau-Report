package org.creditbureaureport.projections;

import java.time.LocalDate;
import java.util.Date;

public interface YurProjection {

    // Inform
    String getNumks();

    // AzolikYur
    LocalDate getDatsIzm();
    String getKodchlen();
    Byte getShotname();
    String getAdres();
    String getKodRayon();
    String getKodObl();
    String getTelmobil();
    String getTelhome();
    String getName();
}


