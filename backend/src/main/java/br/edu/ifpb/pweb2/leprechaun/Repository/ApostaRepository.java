package br.edu.ifpb.pweb2.leprechaun.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;

public interface ApostaRepository extends JpaRepository<Aposta, Long>{
	public Aposta findByClienteAndNumEscolhidosAndSorteio(Usuario cliente, String[] numEscolhidos, Sorteio sorteio);
	
	public List<Aposta> findBySorteio(Sorteio sorteio);
	
	public Aposta findByNumEscolhidos(String[] numeros);
}
