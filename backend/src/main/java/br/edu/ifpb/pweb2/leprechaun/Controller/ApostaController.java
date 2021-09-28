package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.ApostasFavoritasDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.ConferirApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.DezenasDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.FazerApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Aposta;
import br.edu.ifpb.pweb2.leprechaun.Repository.ApostaRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.ApostaService;

@Controller
public class ApostaController {

    @Autowired
    ApostaService apostaService;
    
    @Autowired
    ApostaRepository apostaRepository;
    
    @RequestMapping("/aposta/{idCliente}")
    public ModelAndView index(ModelAndView mav, HttpSession session,@PathVariable Long idCliente) {
    	
    	List<String> apostasFav = apostaService.listarApostasFavoritas(idCliente);
    	FazerApostaDTO apostaDTO = new FazerApostaDTO();
    	
    	List<String> msgApostaFavorita = new ArrayList<>();
      	
    	for(String a: apostasFav) {
    		msgApostaFavorita.add(a);
    	}
    	
    	msgApostaFavorita.add("Ou escolha uma aposta favorita");
    	
    	apostaDTO.setApostasFavoritas(msgApostaFavorita);
    	
    	mav.addObject("apostaDTO", apostaDTO);
    	   	
    	mav.setViewName("Telas/TelaAposta");
    	    	
    	return mav;
    }
   
    @RequestMapping("/aposta/criar/{idCliente}")
    public String criarAposta(@ModelAttribute("apostaDTO") FazerApostaDTO apostaDTO, ModelAndView mav, @PathVariable Long idCliente, RedirectAttributes redirectAttributes, HttpSession session) {
        
    	ApostaDTO dto = this.apostaService.criar(idCliente, apostaDTO);
    	   	
    	if(dto.getMensagem().equals("Aposta criada com sucesso")) {
    		session.setAttribute("msgSucesso", true);
    	}
    	else {
    		session.setAttribute("msgSucesso", false);
    	}
    	  	
    	redirectAttributes.addFlashAttribute("mensagem", dto.getMensagem());
    				  	    	 	    
        return "redirect:/aposta/" + idCliente;
    }
    
    @RequestMapping("/aposta/conferir/{idCliente}")
    public ModelAndView conferirApostas(ModelAndView mav, @PathVariable Long idCliente, RedirectAttributes redirectAttributes, HttpSession session) {
          
    	List<ConferirApostaDTO> lista = this.apostaService.conferirApostas(idCliente);
    	
    	mav.addObject("listaApostas", lista);
    	mav.setViewName("Telas/TelaConferirAposta");
        return mav;
    }

//    @PostMapping("/criarApostasFavoritas")
//    public ResponseEntity<?> criarApostaFavorita(@RequestBody ApostasFavoritasDTO dto) {
//        this.apostaService.criarApostaFavorita(dto.getIdCliente(), dto.getIdAposta());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    
//    @GetMapping("/listar-apostas-favoristas")
//    public List<String> listarApostasFavoritas(@RequestParam Long idCliente) {
//    	return this.apostaService.listarApostasFavoritas(idCliente);
//    }
}
