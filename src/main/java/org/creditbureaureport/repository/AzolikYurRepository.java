package org.creditbureaureport.repository;

import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.model.AzolikYur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AzolikYurRepository extends JpaRepository<AzolikYur, String>{

    @Query("FROM AzolikYur WHERE MONTH(datsIzm) = :month AND YEAR(datsIzm) = :year")
    List<AzolikYur> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
