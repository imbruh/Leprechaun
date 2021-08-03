package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@RestController
@RequestMapping("/")
public class UsuarioController {
    
   @Autowired
   private UsuarioRepository usuarioRepository;
    
    @GetMapping("/usuarios")
    public List<Usuario> getNome() {
        return this.usuarioRepository.findAll();
    }
}
