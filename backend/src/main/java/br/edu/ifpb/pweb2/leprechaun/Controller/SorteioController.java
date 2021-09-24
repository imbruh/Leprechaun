package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.time.format.DateTimeFormatter;
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
    
    @PutMapping("/realizar-sorteio")
    public ResponseEntity<SorteioSentDTO> realizarSorteio(@RequestParam(defaultValue = "", required = false) String[] dezenasEscolhidas, @RequestParam TipoSorteio tipoSorteio, @RequestParam Long idControlador){
    	SorteioSentDTO dto = sorteioService.realizarSorteio(dezenasEscolhidas,tipoSorteio,idControlador); 
    	return new ResponseEntity<SorteioSentDTO>(dto,HttpStatus.OK);
    }
}
