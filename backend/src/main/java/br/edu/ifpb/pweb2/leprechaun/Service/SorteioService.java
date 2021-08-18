package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            System.out.println(dezenas[0]);
			while(dezenas[5]==null) {
				int num = random.nextInt(60) + 1;
				String numConvert = (num < 10) ? ("0" + num) : Integer.toString(num);
               
				boolean existe = false;
				for(String n: dezenas) {
                    if(n!=null){
                        if(n.equals(numConvert)){
                            existe = true;
                        }
                        System.out.println(num);
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
		sorteio.setDataHora(dataHora);
		sorteio.setValorPremio(valor);	
		sorteio.setControlador(controlador);	
        
        sorteioRepository.save(sorteio);
        return "";
	}
}
