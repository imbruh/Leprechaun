 package br.edu.ifpb.pweb2.leprechaun.Repository;

import java.util.List;
import java.util.Optional;

import br.edu.ifpb.pweb2.leprechaun.Model.TipoUsuario;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public List<Usuario> findAll();

    public Usuario findByIdAndTipoUsuario(Long id,TipoUsuario tipo);

    public List<Usuario> findByTipoUsuario(TipoUsuario tipo);

    public Optional<Usuario> findById(Long id);
    
    public Usuario findByLoginAndSenha(String login, String senha);
    
    public Usuario findByCpfOrLogin(String cpf, String login);
}
