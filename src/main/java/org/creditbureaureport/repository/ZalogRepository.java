package org.creditbureaureport.repository;

import org.creditbureaureport.dto.ZalogDTO;
import org.creditbureaureport.model.Zalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZalogRepository extends JpaRepository<Zalog, Byte> {

    @Query(value = "select z.ls from zalog z inner join dbo.kredit k on k.numdog = z.numdog where k.numdog = :numdog", nativeQuery = true)
    List<String> findLs(@Param("numdog") String numdog);

    @Query(value = "select z.sums from zalog z inner join dbo.kredit k on k.numdog = z.numdog where k.numdog = :numdog", nativeQuery = true)
    List<String> findSums(@Param("numdog") String numdog);

    @Query(value = "select z.kod_cb from zalog z inner join dbo.kredit k on k.numdog = z.numdog where k.numdog = :numdog", nativeQuery = true)
    List<String> findKodCb(@Param("numdog") String numdog);
}
