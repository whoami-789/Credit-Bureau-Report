package org.creditbureaureport.repository;

import org.creditbureaureport.dto.ContractDTO;
import org.creditbureaureport.model.AzolikFiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AzolikFizRepository extends JpaRepository<AzolikFiz, String> {
    @Query(value = "SELECT top 20 " +
            "    af.kodchlen," +
            "    af.name," +
            "    k.numdog," +
            "    k.vidkred," +
            "    k.dats_izm," +
            "    k.kod," +
            "    k.sost," +
            "    k.status," +
            "    k.valut," +
            "    k.datadog," +
            "    k.dats," +
            "    k.dats_zakr," +
            "    k.summa," +
            "    k.graf," +
            "    k.nalbeznal," +
            "    k.xatar," +
            "    k.tipkred," +
            "    k.prosent," +
//            "    k.numdog," +
//            "    k.kod_dog," +
            "    s.sums," +
            "    g.dats," +
//            "    g.numdog," +
            "    g.pog_kred + g.pog_proc," +
            "    d.dats," +
//            "    d.kod_dog," +
            "    z.sums," +
            "    z.kod_cb " +
//            "    zd.kod_zalog," +
//            "    zd.detal," +
//            "    zd.stroka," +
//            "    zx.data_priem," +
//            "    zx.data_vozvrat " +
            "FROM " +
            "    azolik_fiz af " +
            "        INNER JOIN kredit k ON af.users = k.users" +
            "        INNER JOIN saldo s ON k.lskred = s.ls" +
            "        INNER JOIN grafik g ON g.numdog = k.numdog" +
            "        INNER JOIN dok d ON d.ls = k.lskred" +
            "        INNER JOIN zalog z ON z.numdog = k.numdog" +
            "        INNER JOIN zalog_detal zd ON z.kod = zd.kod_zalog" +
            "        INNER JOIN zalog_xranenie zx ON z.kod = zx.kod_dog", nativeQuery = true)
    List<Object[]> findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie();

    @Query("FROM AzolikFiz WHERE MONTH(datsIzm) = :month AND YEAR(datsIzm) = :year")
    List<AzolikFiz> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

}
