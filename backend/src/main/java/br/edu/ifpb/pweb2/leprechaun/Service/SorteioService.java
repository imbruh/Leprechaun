package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifpb.pweb2.leprechaun.Dto.SorteioSentDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoSorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.ApostaRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.SorteioRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class SorteioService {

	@Autowired
	private SorteioRepository sorteioRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ApostaRepository apostaRepository;
	
	public Sorteio getSorteioAberto() {
		Sorteio sorteio = sorteioRepository.findFirstByOrderByDataHoraDesc();
		
		if(sorteio.getDezenasSorteadas() == null) {
			return sorteio;
		}
		
		return null;
	}
	
	
	public String criarSorteio(Long idControlador, LocalDate dataHora, double valor) {
	
		Sorteio ultimoSorteio = sorteioRepository.findFirstByOrderByDataHoraDesc();
		
		if(ultimoSorteio != null) {
			String diaDisponivel = sorteioRepository.ultimaData(ultimoSorteio.getDataHora()).replace(" ","T");

	        LocalDate diaDisponivelConvert = LocalDate.parse(diaDisponivel);
	             
	        LocalDate dataHoje = LocalDate.now(); 
	            
	        if(dataHoje.isBefore(ultimoSorteio.getDataHora()) || ultimoSorteio.getDezenasSorteadas() == null ) {
	        	return "Já tem um sorteio em aberto";
		    }
	        
	        if(dataHora.isBefore(diaDisponivelConvert) || ultimoSorteio.getDezenasSorteadas()==null) {	        	
	        	return "Só pode criar sorteio a partir do dia "+ diaDisponivelConvert.format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
	        }
	        
		}
		        
        Usuario controlador = usuarioRepository.findByIdAndTipoUsuario(idControlador, TipoUsuario.CONTROLADOR);
       
        if(controlador == null) {
        	throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Controlador inexistente");
        }

        Sorteio sorteio = new Sorteio();
		sorteio.setDataHora(dataHora);
		sorteio.setValorPremio(valor);	
		sorteio.setControlador(controlador);	
        sorteioRepository.save(sorteio);
        
        return null;
	}
	
	public SorteioSentDTO realizarSorteio(String[] dezenasEscolhidas, TipoSorteio tipoSorteio, Long idControlador) {
			
		Sorteio ultimoSorteio = sorteioRepository.findFirstByOrderByDataHoraDesc();
		
		if(ultimoSorteio.getControlador().getId() != idControlador) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Você não é o controlador desse sorteio");
		}
		
		LocalDate hoje = LocalDate.parse("2021-09-23"); //LocalDateTime.now();
		if(hoje.isBefore(ultimoSorteio.getDataHora())) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "O sorteio só poderá ser realizado no dia: " + ultimoSorteio.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		if (ultimoSorteio.getDezenasSorteadas()!=null) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Sorteio já realizado");			
		}
		if (tipoSorteio == TipoSorteio.ALEATORIO) {
			dezenasEscolhidas = this.gerarNumeros();
		}
		else if (dezenasEscolhidas.length!=6) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Apenas 6 números devem ser escolhidos para o resultado do sorteio");

		}
		
		
		for (int x = 0; x<6; x++) {
			if(Integer.parseInt(dezenasEscolhidas[x]) < 10 && dezenasEscolhidas[x].length()==1) {
				String numero = "0"+dezenasEscolhidas[x];
				dezenasEscolhidas[x]=numero;
			}
		}
		
		
		ultimoSorteio.setDezenasSorteadas(dezenasEscolhidas);
		ultimoSorteio.setTipo(tipoSorteio);
		
		sorteioRepository.save(ultimoSorteio);
		
		List<Aposta> apostas = this.apostaRepository.findBySorteio(ultimoSorteio);
		
		List<Usuario> vencedores = this.listarVencendor(apostas, dezenasEscolhidas);
		
		SorteioSentDTO dto = new SorteioSentDTO();
		dto.setDezenasSorteadas(ultimoSorteio.getDezenasSorteadas());
		dto.setDataHora(ultimoSorteio.getDataHora());
		dto.setValorPremio(ultimoSorteio.getValorPremio());
		List<String> ganhadores = new ArrayList<>();
		if(!vencedores.isEmpty()) {
			for(Usuario vencedor: vencedores) {
				ganhadores.add(vencedor.getNome());
			}
		}
		dto.setGanhador(ganhadores);
	
		return dto;
	}
	
	public String[] gerarNumeros() {
		String[] dezenas = new String[6];
		Random random = new Random();
		int cont = 0;
		while(dezenas[5]==null) {
			int num = random.nextInt(60) + 1;
			String numConvert = (num < 10) ? ("0" + num) : Integer.toString(num);
			boolean existe = false;
			for(String n: dezenas) {
				if(n!=null){
					if(n.equals(numConvert)){
						existe = true;
					}
				}	
			}
			if(!existe) {              
				dezenas[cont] = numConvert;
				cont++;                         
			}                            
		}
		return dezenas;
	}
	
	public List<Usuario> listarVencendor(List<Aposta> apostas, String[] dezenasEscolhidas) {
		List<Usuario> usuarios = new ArrayList<>();
		
		for(Aposta aposta: apostas) {
			int cont = 0;
			for(String numero: aposta.getNumEscolhidos()) {
				for(String numSorteio: dezenasEscolhidas) {
					if(numero.equals(numSorteio)) {
						cont ++;
					}
				}
			}
			if(cont == 6) {
				usuarios.add(aposta.getCliente());
			}
		}
		
		return usuarios;
	}
	
}
