package br.edu.ifpb.pweb2.leprechaun.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifpb.pweb2.leprechaun.Dto.UsuarioLoginDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Usuario> getClientes() {
        return usuarioRepository.findByTipoUsuario(TipoUsuario.CLIENTE);
    }
    
    public String cadastrarUsuario(Usuario usuario){
    	int idade = LocalDate.now().getYear() - usuario.getData_nascimento().getYear();
    	
    	if(idade < 18) {
    		return "Você precisa ter 18 anos ou mais para se cadastrar.";	
    	}
    	
    	Usuario usuarioExistente = this.usuarioRepository.findByCpfOrLogin(usuario.getCpf(), usuario.getLogin());
    	
    	if(usuarioExistente != null) {
    		return "Usuario ja existe.";	
    	}
    	
        usuarioRepository.save(usuario);     
        return "Usuario criado com sucesso.";
    }
    
    public Usuario logarUsuario(Usuario usuario){
    	Usuario usuarioExistente = this.usuarioRepository.findByLoginAndSenha(usuario.getLogin(), usuario.getSenha());
    	
    	return usuarioExistente;
    }
}
