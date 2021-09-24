package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Model.ApostasFavoritas;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.ApostaRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.ApostasFavoritasRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.SorteioRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class ApostaService {

    @Autowired
    ApostaRepository apostaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ApostasFavoritasRepository apostasFavoritasRepository;

    @Autowired
    SorteioRepository sorteioRepository;

    public void criar(Long idCliente, String[] numEscolhidos){

    	Usuario cliente = usuarioRepository.findById(idCliente).orElseThrow(
    			()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado!"));

    	Sorteio sorteio = sorteioRepository.findFirstByOrderByDataHoraDesc();
    	
    	if (sorteio.getDataHora().isBefore(LocalDate.now()) || sorteio.getDezenasSorteadas()!=null){
    		throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Não ha sorteio em aberto no momento");
    	}
    	
    	Aposta apostaRepetida = apostaRepository.findByClienteAndNumEscolhidosAndSorteio(cliente, numEscolhidos, sorteio);
    	
    	if(apostaRepetida != null) {
    		throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Não é permitido a mesma aposta");
    	}
        
        int qntNumEscolhidos = numEscolhidos.length;
        double valor = 0;

        if(qntNumEscolhidos < 6 || qntNumEscolhidos > 10) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Escolha entre 6 e 10 numeros");
        }
        
        for(String n1: numEscolhidos) {
            int cont = 0;
            for(String n2: numEscolhidos) {
                if(cont < 2){
                    if(n2.equals(n1)) {
                        cont ++;
                    }
                }
                else{
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Não é permitido números iguais");
                }
            }
        }

        if(qntNumEscolhidos == 6) {
            valor = 3;
        } 
        else if(qntNumEscolhidos == 7) {
            valor = 15;
        }
        else if(qntNumEscolhidos == 8) {
            valor = 90;
        }
        else if(qntNumEscolhidos == 9) {
            valor = 300;
        }
        else if(qntNumEscolhidos == 10) {
            valor = 1200;
        }

        Aposta aposta = new Aposta();

        aposta.setCliente(cliente);
        aposta.setValor(valor);
        aposta.setNumEscolhidos(numEscolhidos);
        aposta.setSorteio(sorteio);
        
        apostaRepository.save(aposta);
    }

    public void criarApostaFavorita(Long idCliente, Long idAposta) {
     
        Usuario cliente = usuarioRepository.findById(idCliente).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado!"));

        if(cliente.getTipoUsuario() == TipoUsuario.CLIENTE) {
            Aposta aposta = apostaRepository.findById(idAposta).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aposta não encontrada!"));
            
            ApostasFavoritas apostaFavRepetida = apostasFavoritasRepository.findByClienteAndAposta(cliente, aposta);
            
            if(apostaFavRepetida != null) {
            	throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Essa aposta ja foi salva");
            }
                
            ApostasFavoritas apostasFavs = new ApostasFavoritas();
            apostasFavs.setCliente(cliente);
            apostasFavs.setAposta(aposta);
        
            apostasFavoritasRepository.save(apostasFavs);
        }
        else {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "O usuário nao é um cliente");
        }
    }
    
    public List<String[]> listarApostasFavoritas(Long idCliente) {
    	this.usuarioRepository.findById(idCliente).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado!"));
    	
    	List<String[]> numApostasFavoritas = this.apostasFavoritasRepository.getByCliente(idCliente);
    	
    	if(numApostasFavoritas == null || numApostasFavoritas.isEmpty()) {
    		 throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Você não possui apostas favoritas cadastradas");
    	}
    	
    	return numApostasFavoritas;
    }
}
