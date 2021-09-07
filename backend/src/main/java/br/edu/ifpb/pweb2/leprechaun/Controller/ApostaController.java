package br.edu.ifpb.pweb2.leprechaun.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Model.ApostasFavoritas;
import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.ApostasFavoritasDTO;
import br.edu.ifpb.pweb2.leprechaun.Service.ApostaService;

@RestController
@RequestMapping("/aposta")
public class ApostaController {

    @Autowired
    ApostaService apostaService;
   
    @PostMapping("/criar")
    public ResponseEntity<?> criarAposta(@RequestBody ApostaDTO dto) {
        this.apostaService.criar(dto.getIdCliente(), dto.getNumerosEscolhidos());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/criarApostasFavoritas")
    public ResponseEntity<?> criarApostaFavorita(@RequestBody ApostasFavoritasDTO dto) {
        this.apostaService.criarApostaFavorita(dto.getIdCliente(), dto.getIdAposta());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
