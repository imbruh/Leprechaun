package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.Date;
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
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
   @Autowired
   private UsuarioRepository usuarioRepository;

   @Autowired
   private UsuarioService usuarioService;
    
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
    
    @GetMapping("/index")
    public ModelAndView testando(ModelAndView mav) {
    	mav.setViewName("index");
    	mav.addObject("idade", 20);
    	return mav;
    }
}
