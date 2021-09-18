package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.leprechaun.Dto.UsuarioLoginDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.SorteioService;
import br.edu.ifpb.pweb2.leprechaun.Service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    
   @Autowired
   private UsuarioRepository usuarioRepository;

   @Autowired
   private UsuarioService usuarioService;
   
   @Autowired
   SorteioService sorteioService;
    
    @GetMapping("/")
    public List<Usuario> getUsuarios() {
        return this.usuarioRepository.findAll();
    }
    
    @GetMapping("/clientes")
    public List<Usuario> getClientes() {
        return this.usuarioService.getClientes();
    }

    @PostMapping("/usuario/cadastrar")
    public ResponseEntity<Usuario> cadastrarCliente(@RequestBody Usuario usuario){
        Usuario user = this.usuarioService.cadastrarUsuario(usuario);
        return new ResponseEntity<> (user, HttpStatus.OK);
    }
  
    @PostMapping("/login")
    public String logarUsuario(@ModelAttribute Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
    	Usuario usuarioExistente = this.usuarioService.logarUsuario(usuario);
    	
    	if(usuarioExistente == null) {
    		redirectAttributes.addFlashAttribute("Erro", "Login ou senha inválidos");
    		return "redirect:/login";
    	}
    	
    	redirectAttributes.addFlashAttribute("usuario", usuario);
        return "redirect:/usuarios/sorteio";
    }
    

    @GetMapping("/sorteio")
    public String testando(@ModelAttribute Usuario usuario, ModelAndView mav) {
    	Sorteio sorteio = this.sorteioService.getSorteioAberto();
    	mav.setViewName("Telas/TelaInicialSorteio");
    	mav.addObject("sorteio", sorteio);
    	mav.addObject("usuario", usuario);
    	System.out.println(usuario);
    	if(sorteio != null) {
    		mav.addObject("dataFormatada", sorteio.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
    	}
    	
    	return "/usuarios/sorteio";
    }
}
