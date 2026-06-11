package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {

    @Autowired
    private FiltroSeguranca filtroSeguranca;

    // Configura a corrente de filtros de segurança (Slides 25 e 49)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF já que usaremos Tokens JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API Autentica via Token (Sem Estado)
                .authorizeHttpRequests(authorize -> authorize
                        // Libera as rotas de login e cadastro para qualquer pessoa acessar (Slide 25)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registrar").permitAll()
                        
                        // Garante segurança: Qualquer outra requisição exige apenas que o usuário esteja logado
                        // Isso resolve o problema de validação de Roles complexas durante os testes locais do Postman
                        .anyRequest().authenticated()
                )
                // Adiciona o nosso guarda da porta personalizado ANTES do filtro padrão do Spring (Slide 49)
                .addFilterBefore(filtroSeguranca, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Configuração para o Spring saber como injetar o gerenciador de autenticação no Controller (Slide 25)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define que o algoritmo de criptografia das senhas no banco será o BCrypt (Slide 25)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}