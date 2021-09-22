package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Dto.ApostaDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.ApostasFavoritasDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Service.ApostaService;

@Controller
public class ApostaController {

    @Autowired
    ApostaService apostaService;
    
    @RequestMapping("/aposta")
    public String index(Model model, @ModelAttribute("usuario") Usuario usuario) {
    	model.addAttribute("usuario", usuario);
//    	
//    	if(mensagem.isEmpty()) {
//    		mensagem = null;
//    	}
//    	 	
//    	model.addAttribute("mensagem", mensagem);
    	
    	return "Telas/TelaAposta";
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
