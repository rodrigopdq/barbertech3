package api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<UserDetails> findFirstByLogin(String login);
}