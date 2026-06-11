package api.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import api.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroSeguranca extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método que intercepta a requisição HTTP (Slide 47)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        var token = this.recuperarToken(request);
        
        if (token != null) {
            var login = tokenService.validarToken(token); // Decodifica o token e pega o login (Slide 1263)
            UserDetails usuario = usuarioRepository.findByLogin(login); // Busca os dados do usuário (Slide 1264)
            
            // Se encontrou, injeta o usuário autenticado no contexto do Spring (Slide 1261)
            var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }
        
        // Passa a requisição para frente (Slide 1262)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair a String do token tirando a palavra "Bearer " (Slide 48)
    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization"); // Busca o cabeçalho (Slide 1282)
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Remove "Bearer " e mantém só o token (Slide 1281)
    }
}