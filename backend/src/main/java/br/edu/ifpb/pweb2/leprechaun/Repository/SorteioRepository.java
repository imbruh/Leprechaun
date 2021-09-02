package br.edu.ifpb.pweb2.leprechaun.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;

public interface SorteioRepository extends JpaRepository<Sorteio, Long>{

	// Sorteio findByDataHora(LocalDateTime data);
}
