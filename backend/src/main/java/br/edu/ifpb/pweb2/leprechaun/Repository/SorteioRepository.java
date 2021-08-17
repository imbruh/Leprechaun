package br.edu.ifpb.pweb2.leprechaun.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;

public interface SorteioRepository extends JpaRepository<Sorteio, Long>{

	// List<Sorteio> findAll();
}
