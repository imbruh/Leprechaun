package br.edu.ifpb.pweb2.leprechaun.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Model.ApostasFavoritas;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;

public interface ApostasFavoritasRepository extends JpaRepository<ApostasFavoritas, Long>{
    public ApostasFavoritas findByClienteAndAposta(Usuario cliente, Aposta aposta);
    
    @Query("select af.aposta.numEscolhidos from ApostasFavoritas af where af.cliente.id = ?1")
    public List<String[]> getByCliente(Long idCliente);
}
