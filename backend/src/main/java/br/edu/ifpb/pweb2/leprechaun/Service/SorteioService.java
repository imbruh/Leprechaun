package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoSorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.SorteioRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class SorteioService {

	@Autowired
	private SorteioRepository sorteioRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void criarSorteio(Long idControlador, String[] dezenasSorteadas, LocalDateTime dataHora, double valor) {
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
		Usuario controlador = usuarioRepository.findById(idControlador).orElse(null);
		sorteio.setControlador(controlador);	
        sorteioRepository.save(sorteio);
	}
}
