package br.edu.ifpb.pweb2.leprechaun.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class ClienteService {
    
    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Usuario> getClientes() {
        return usuarioRepository.findByControlador(false);
    }
}
