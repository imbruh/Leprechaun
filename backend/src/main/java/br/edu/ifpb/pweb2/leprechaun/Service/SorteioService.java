package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoSorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.SorteioRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class SorteioService {

	@Autowired
	private SorteioRepository sorteioRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public String criarSorteio(Long idControlador, String[] dezenasSorteadas, LocalDateTime dataHora, double valor) {
		Sorteio ultimoSorteio = sorteioRepository.findFirstByOrderByDataHoraDesc();
        String diaDisponivel = sorteioRepository.ultimaData(ultimoSorteio.getDataHora()).replace(" ","T");

        LocalDateTime diaDisponivelConvert = LocalDateTime.parse(diaDisponivel);

        if (dataHora.isBefore(diaDisponivelConvert)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Dia indisponivel");
        }
        
        Usuario controlador = usuarioRepository.findByIdAndTipoUsuario(idControlador, TipoUsuario.CONTROLADOR);
       
        if(controlador == null) {
            return "Controlador inexistente";
        }

        Sorteio sorteio = new Sorteio();
		if(dezenasSorteadas == null) {
            sorteio.setTipo(TipoSorteio.ALEATORIO);
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
            dezenasSorteadas = dezenas;
		}
        else {
            sorteio.setTipo(TipoSorteio.NAO_ALEATORIO);
        }
		sorteio.setDezenasSorteadas(dezenasSorteadas);
        System.out.println(LocalDateTime.now());
		sorteio.setDataHora(dataHora);
		sorteio.setValorPremio(valor);	
		sorteio.setControlador(controlador);	
        
        sorteioRepository.save(sorteio);
        return "";
	}
}
