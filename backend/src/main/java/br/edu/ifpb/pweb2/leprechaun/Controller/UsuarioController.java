package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.ClienteService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
   @Autowired
   private UsuarioRepository usuarioRepository;

   @Autowired
   private ClienteService clienteService;
    
    @GetMapping("/")
    public List<Usuario> getNome() {
        return this.usuarioRepository.findAll();
    }

    @GetMapping("/clientes")
    public List<Usuario> getClientes() {
        return this.clienteService.getClientes();
    }
}
