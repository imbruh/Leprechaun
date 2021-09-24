package br.edu.ifpb.pweb2.leprechaun.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;

public interface SorteioRepository extends JpaRepository<Sorteio, Long>{

    Sorteio findFirstByOrderByDataHoraDesc();

    @Query(value="select date_add(?1, interval 7 day)", nativeQuery = true )
    String ultimaData(LocalDate dia);

}
