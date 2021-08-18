package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
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
    public List<Sorteio> getNome() {
        return this.sorteioRepository.findAll();
    }

    @PostMapping("/criar")
    public String criarSorteio(@RequestBody Sorteio sorteio) {
        return this.sorteioService.criarSorteio(sorteio.getId(), sorteio.getDezenasSorteadas(), sorteio.getDataHora(), sorteio.getValorPremio());
    }
}
