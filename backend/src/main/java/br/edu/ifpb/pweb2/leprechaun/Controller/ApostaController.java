package br.edu.ifpb.pweb2.leprechaun.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Service.ApostaService;

@RestController
@RequestMapping("/aposta")
public class ApostaController {

    @Autowired
    ApostaService apostaService;
   
    @PostMapping("/criar")
    public ResponseEntity<?> criarSorteio(@RequestBody ApostaDTO dto) {
        this.apostaService.criar(dto.getIdCliente(), dto.getNumerosEscolhidos());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
