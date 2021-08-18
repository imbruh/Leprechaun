package br.edu.ifpb.pweb2.leprechaun.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Service.ApostaService;

@RestController
@RequestMapping("/aposta")
public class ApostaController {

    @Autowired
    ApostaService apostaService;
   
    @PostMapping("/criar")
    public String criarSorteio(@RequestBody ApostaDTO dto) {
        return this.apostaService.criar(dto.getIdCliente(), dto.getNumerosEscolhidos());
    }
}
