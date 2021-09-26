package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.ApostasFavoritasDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.FazerApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Service.ApostaService;

@Controller
public class ApostaController {

    @Autowired
    ApostaService apostaService;
    
    @RequestMapping("/aposta/{idCliente}")
    public ModelAndView index(ModelAndView mav, HttpSession session,@PathVariable Long idCliente) {
    	
    	List<String[]> apostasFav = apostaService.listarApostasFavoritas(idCliente);
    	
    	for (String[] aposta: apostasFav) {
    		System.out.println(aposta.toString());
    	}
    	
    	mav.addObject("fazerApostaDTO", new FazerApostaDTO());
    	mav.addObject("apostasFav",apostasFav);
    	mav.setViewName("Telas/TelaAposta");
    	
    	
    	return mav;
    }
   
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
    
    @GetMapping("/listar-apostas-favoristas")
    public List<String[]> listarApostasFavoritas(@RequestParam Long idCliente) {
    	return this.apostaService.listarApostasFavoritas(idCliente);
    }
}
