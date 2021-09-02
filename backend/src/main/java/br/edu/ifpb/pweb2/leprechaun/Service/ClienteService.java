package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDate;
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
        return usuarioRepository.findByTipoUsuario(TipoUsuario.CLIENTE);
    }
    
    public void cadastrarCliente(String nome, String cpf, LocalDate data_nascimento, String login, String senha){
        Usuario cliente = new Usuario(nome,cpf,data_nascimento,login,senha,TipoUsuario.CLIENTE);
        usuarioRepository.save(cliente);
        
    }
}
