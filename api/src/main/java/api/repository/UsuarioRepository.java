package api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import api.model.Usuario;
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Método customizado que o Spring Security usa para filtrar o usuário pelo login (Slide 20)
    UserDetails findByLogin(String login);
}