package br.edu.ifpb.pweb2.leprechaun.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Model.ApostasFavoritas;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;

public interface ApostasFavoritasRepository extends JpaRepository<ApostasFavoritas, Long>{
    public ApostasFavoritas findByClienteAndAposta(Usuario cliente, Aposta aposta);
}
