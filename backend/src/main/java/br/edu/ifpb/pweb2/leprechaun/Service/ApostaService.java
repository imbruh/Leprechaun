package br.edu.ifpb.pweb2.leprechaun.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.ApostaRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class ApostaService {

    @Autowired
    ApostaRepository apostaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public String criar(Long idCliente, String[] numEscolhidos){
        Usuario cliente = usuarioRepository.findById(idCliente).orElse(null);

        if(cliente == null) {
            return "Cliente inexistente";
        }

        int qntNumEscolhidos = numEscolhidos.length;
        double valor = 0;

        if(qntNumEscolhidos < 6 || qntNumEscolhidos > 10) {
            return "Escolha entre 6 e 10 numeros";
        }
        
        for(String n1: numEscolhidos) {
            int cont = 0;
            for(String n2: numEscolhidos) {
                if(cont < 2){
                    if(n2.equals(n1)) {
                        System.out.println(n1 + " " + n2);
                        cont ++;
                    }
                }
                else {
                    return "Não é permitido numeros iguais";
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
        
        apostaRepository.save(aposta);

        return "aposta criada";
    }

}
