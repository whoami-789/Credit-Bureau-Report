package org.creditbureaureport.projections;

import java.time.LocalDate;
import java.util.Date;

public interface FizProjection {
    LocalDate getDatsIzm();
    String getKodchlen();
    String getImya();
    String getFam();
    String getOtch();
    Byte getFsobst();
    Date getDatsRojd();
    String getAdres();
    String getKodRayon();
    String getKodObl();
    String getKodPension();
    String getInn();
    String getTipDok();
    String getSerNumPasp();
    Date getVidanPasp();
    Date getPaspdo();
    String getTelmobil();
    String getTelhome();
    String getName();
    String getIndpred();
}


