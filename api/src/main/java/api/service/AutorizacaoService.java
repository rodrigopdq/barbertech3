package api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import api.repository.UsuarioRepository;
@Service
public class AutorizacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    // Método obrigatório do Spring Security para buscar o usuário durante a autenticação (Slide 21)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}