package api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import api.dto.AutenticacaoDto;
import api.dto.LoginRespostaDto;
import api.dto.RegistroDto;
import api.model.Usuario;
import api.repository.UsuarioRepository;
import api.config.TokenService;

@RestController
@RequestMapping("/auth") // ✅ FIX: barra inicial adicionada para casar com as regras do SecurityFilterChain
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    // Endpoint de Login protegido com Try-Catch para depurar o 403
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AutenticacaoDto data) {
        try {
            // Cria o token interno do Spring com o login e senha digitados
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());

            // O AuthenticationManager vai lá no banco, descriptografa a senha e checa se bate
            var auth = this.authenticationManager.authenticate(usernamePassword);

            // Se a senha estiver certa, ele passa para a linha de baixo.
            var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

            // Devolve o token dentro de um JSON para o Front-end
            return ResponseEntity.ok(new LoginRespostaDto(token));

        } catch (Exception e) {
            // Se houver qualquer erro na geração do token ou injeção,
            // isso vai forçar o terminal do VS Code a cuspir o erro real e detalhado!
            System.out.println("--- [ERRO DETECTADO NO FLUXO DE LOGIN] ---");
            e.printStackTrace();
            System.out.println("------------------------------------------");

            // Retorna um erro 500 amigável com o texto do problema no Postman
            return ResponseEntity.status(500).body("Erro interno no servidor: " + e.getMessage());
        }
    }

    // Endpoint de Cadastro de novos usuários/admin (Baseado no Slide 26)
    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody RegistroDto data) {
        // Verifica se o login já não existe cadastrado no sistema
        if (this.repository.findFirstByLogin(data.login()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        // Criptografa a senha usando BCrypt antes de salvar no banco de dados! (Segurança Máxima)
        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());
        Usuario novoUsuario = new Usuario(null, data.login(), senhaCriptografada, data.funcao());

        this.repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}