package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Dto.SorteioDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.SorteioSentDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoSorteio;
import br.edu.ifpb.pweb2.leprechaun.Repository.SorteioRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.SorteioService;

@RestController
@RequestMapping("/sorteio")
public class SorteioController {

    @Autowired
    SorteioService sorteioService;

    @Autowired
    SorteioRepository sorteioRepository;
  
    @GetMapping("/")
    public List<Sorteio> listarSorteios() {
        return this.sorteioRepository.findAll();
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarSorteio(@RequestBody SorteioDTO sorteio) {
        this.sorteioService.criarSorteio(sorteio.getIdControlador(), sorteio.getDataHora(), sorteio.getValorPremio());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/realizar-sorteio")
    public ResponseEntity<SorteioSentDTO> realizarSorteio(@RequestParam(defaultValue = "", required = false) String[] dezenasEscolhidas, @RequestParam TipoSorteio tipoSorteio){
    	SorteioSentDTO dto = sorteioService.realizarSorteio(dezenasEscolhidas,tipoSorteio); 
    	return new ResponseEntity<SorteioSentDTO>(dto,HttpStatus.OK);
    }
}
