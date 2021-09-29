package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.leprechaun.Dto.DezenasDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.SorteioDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.SorteioSentDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoSorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.SorteioRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.SorteioService;

@Controller
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

    @RequestMapping("/criar/{idControlador}")
    public String criarSorteio(@PathVariable Long idControlador, @ModelAttribute("novoSorteio") Sorteio novoSorteio, HttpSession session, ModelAndView mav, RedirectAttributes redirectAttributes) {
    	
        String mensagem = this.sorteioService.criarSorteio(idControlador, novoSorteio.getDataHora(), novoSorteio.getValorPremio());
      
        redirectAttributes.addFlashAttribute("mensagem", mensagem);
             
        return "redirect:/sorteio/controlador";
    }
    
    @RequestMapping("/realizar-sorteio/{idControlador}")
    public String realizarSorteio(ModelAndView mav, @ModelAttribute("sorteio") Sorteio sorteio, @PathVariable Long idControlador, HttpSession session, RedirectAttributes redirectAttributes){
    	
    	SorteioSentDTO sorteioDTO = null;

    	if(sorteio.getTipo() == TipoSorteio.ALEATORIO) {
    		sorteioDTO = sorteioService.realizarSorteio(null, sorteio.getTipo(), idControlador); 
 
    	}
    	else {
    		session.setAttribute("aleatorio", false);
    		redirectAttributes.addFlashAttribute("dezenasDTO", new DezenasDTO());
    		return "redirect:/sorteio/controlador";
    	}
    	 	
    	String dezenas = Arrays.toString(sorteioDTO.getDezenasSorteadas());
    	dezenas = dezenas.replace(", ", " - ").replace("[", "").replace("]", "");
    	
    	redirectAttributes.addFlashAttribute("dataFormatada", sorteioDTO.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
    	redirectAttributes.addFlashAttribute("dezenas", dezenas);
    	redirectAttributes.addFlashAttribute("sorteioDTO", sorteioDTO);
    	
    	return "redirect:/sorteio/realizado";
    }
    
    @RequestMapping("/realizar-sorteio-nao-aleatorio/{idControlador}")
    public String realizarSorteioNaoAleatorio(ModelAndView mav, @ModelAttribute("dezenasDTO") DezenasDTO dezenasDTO, @PathVariable Long idControlador, HttpSession session, RedirectAttributes redirectAttributes){
    		
    	String[] dezenasEnviar = {dezenasDTO.getN1(), dezenasDTO.getN2(), dezenasDTO.getN3(), dezenasDTO.getN4(), dezenasDTO.getN5(), dezenasDTO.getN6()}; 
    	 	
    	SorteioSentDTO sorteioDTO = sorteioService.realizarSorteio(dezenasEnviar, TipoSorteio.NAO_ALEATORIO, idControlador); 
    	System.out.println(sorteioDTO);
    	if(sorteioDTO.getMensagem() != null && sorteioDTO.getMensagem().equals("Não é permitido numeros iguais")) {
    		redirectAttributes.addFlashAttribute("mensagem", sorteioDTO.getMensagem());
    		return "redirect:/sorteio/controlador";
    	}
		
		redirectAttributes.addFlashAttribute("dezenasDTO", new DezenasDTO());
    	
    	String dezenas = Arrays.toString(sorteioDTO.getDezenasSorteadas());
    	dezenas = dezenas.replace(", ", " - ").replace("[", "").replace("]", "");
    	
    	redirectAttributes.addFlashAttribute("dataFormatada", sorteioDTO.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
    	redirectAttributes.addFlashAttribute("dezenas", dezenas);
    	redirectAttributes.addFlashAttribute("sorteioDTO", sorteioDTO);
    	
    	return "redirect:/sorteio/realizado";
    }
    
    @RequestMapping("/realizado") 
    public ModelAndView sorteioRealizado(ModelAndView mav, HttpSession session, RedirectAttributes redirectAttributes, SorteioSentDTO dto) {
    	mav.setViewName("Telas/TelaSorteioRealizado");
    	return mav;
    }
    
    @RequestMapping("/setar-aleatorio")
    public String setarAleatorio(RedirectAttributes redirectAttributes, HttpSession session) {
    	session.setAttribute("aleatorio", true);
    	return "redirect:/sorteio/controlador";
    }
}
