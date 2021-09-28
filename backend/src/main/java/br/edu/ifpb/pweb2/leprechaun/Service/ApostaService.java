package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.ConferirApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.FazerApostaDTO;
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

    public ApostaDTO criar(Long idCliente, FazerApostaDTO apostaDTO){
	 	
    	ApostaDTO dto = new ApostaDTO();
    	
    	if(apostaDTO.getApostasFavoritas().get(0).equals("Ou escolha uma aposta favorita")) {
        	apostaDTO.getApostasFavoritas().clear();
        }
    	
    	List<String> numeros = new ArrayList<>();
    	numeros.add(apostaDTO.getN1());
    	numeros.add(apostaDTO.getN2());
    	numeros.add(apostaDTO.getN3());
    	numeros.add(apostaDTO.getN4());
    	numeros.add(apostaDTO.getN5());
    	numeros.add(apostaDTO.getN6());
    	numeros.add(apostaDTO.getN7());
    	numeros.add(apostaDTO.getN8());
    	numeros.add(apostaDTO.getN9());
    	numeros.add(apostaDTO.getN10());
    	
    	String numEscolhidos[] = new String[10];
    	for(int i = 0; i < 10; i++) {
    		numEscolhidos[i] = numeros.get(i);
    	}
    	
    	Usuario cliente = usuarioRepository.findById(idCliente).orElse(null);
    	if(cliente == null) {
    		dto.setMensagem("Usuario não encontrado");
    		return dto;

    	}
    	
    	Sorteio sorteio = sorteioRepository.findFirstByOrderByDataHoraDesc();
    	  	             
        int contNumeros = 0;
               
        for(String n1: numEscolhidos) {
            int cont = 0;
            for(String n2: numEscolhidos) {
                if(cont < 2){
                    if(n2.equals(n1) && !n2.equals("") && !n1.equals("")) {
                        cont ++;
                    
	                    if(!n2.equals("")) {
	                    	contNumeros ++;
	                    }
                    }
                }    
                else{
                    dto.setMensagem("Não é permitido números iguais"); 
                    return dto;
                }
            }
        }
        
        String numerosSemEspacoBranco[];
        
        if(apostaDTO.getApostasFavoritas().isEmpty()) {
        	numerosSemEspacoBranco = new String[contNumeros];
        }
        else {
        	numerosSemEspacoBranco = new String[apostaDTO.getApostasFavoritas().size()];
        }
            
        for(int i=0; i<contNumeros; i++) {
        	if(Integer.parseInt(numEscolhidos[i]) < 10) {
        		numEscolhidos[i] = "0" + numEscolhidos[i];
        	}
        	numerosSemEspacoBranco[i] = numEscolhidos[i];
        }
             
        if(apostaDTO.getApostasFavoritas().isEmpty()) {
    		if(numerosSemEspacoBranco.length < 6) {
    			dto.setMensagem("Digite entre 6 a 10 numeros ou escolha uma aposta favorita.");
    			return dto;
    		}  		
    	}
    	else { 	   		
        	for(int i = 0; i < apostaDTO.getApostasFavoritas().size(); i++) {
        		numerosSemEspacoBranco[i] = apostaDTO.getApostasFavoritas().get(i);        		
        	}
    	}
        
        Aposta apostaRepetida = apostaRepository.findByClienteAndNumEscolhidosAndSorteio(cliente, numerosSemEspacoBranco, sorteio);
    	
    	if(apostaRepetida != null) {
    		dto.setMensagem("Não é permitido a mesma aposta");
    		return dto;
    	}
    	             
        int qntNumEscolhidos = numerosSemEspacoBranco.length;
        double valor = 0;

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
        aposta.setNumEscolhidos(numerosSemEspacoBranco);
        aposta.setSorteio(sorteio);
        
        apostaRepository.save(aposta);
        
        dto.setMensagem("Aposta criada com sucesso");
        dto.setIdAposta(aposta.getId());
        
        
        if(apostaDTO.getApostaFav()!=null && apostaDTO.getApostaFav().equals("true")) {  		
    		this.criarApostaFavorita(idCliente, aposta.getId());
        }
        
        return dto;
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
    
    public List<String> listarApostasFavoritas(Long idCliente) {
    	this.usuarioRepository.findById(idCliente).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado!"));
    	List<String[]> numApostasFavoritas = this.apostasFavoritasRepository.getByCliente(idCliente);
    	
    	List<String> listaNumeros = new ArrayList<>();
    		
    	for(String[] array: numApostasFavoritas) {
	    		listaNumeros.add(Arrays.toString(array));	    	
    	}
	    	
    	return listaNumeros;
    }
    
    public List<ConferirApostaDTO> conferirApostas(Long idCliente) { 
    	Usuario cliente = this.usuarioRepository.findById(idCliente).orElse(null);
    	
    	if(cliente == null) {
    		return null;
    	}
    	
    	List<Aposta> apostasDoCliente = this.apostaRepository.findByCliente(cliente);
    		
    	List<ConferirApostaDTO> conferirDTO = new ArrayList<>();
    	
    	for(Aposta aposta: apostasDoCliente) {
    			int cont = 0;
    			for(String numero: aposta.getNumEscolhidos()) {
    				if(aposta.getSorteio().getDezenasSorteadas() != null) {
    					for(String numSorteio: aposta.getSorteio().getDezenasSorteadas()) {
        					if(numero.equals(numSorteio)) {
        						cont ++;
        					}
        				}
    				}
    			}
    		if(aposta.getSorteio().getDezenasSorteadas()!=null)	{
    			ConferirApostaDTO dto = new ConferirApostaDTO();
        		dto.setAcertos(cont);	
        		dto.setDezenasSorteadas(Arrays.asList(aposta.getSorteio().getDezenasSorteadas()));
        		dto.setNumerosApostados(Arrays.asList(aposta.getNumEscolhidos()));
        		dto.setIdSorteio(aposta.getSorteio().getId());
        		
        		conferirDTO.add(dto);
    		}	
    	}
   	
    	return conferirDTO;    	 	
    }
}
