import { useState } from 'react';
import { api } from '../services/api';

interface LoginProps {
  onLoginSucesso: () => void;
}

export default function Login({ onLoginSucesso }: LoginProps) {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState<string | null>(null);
  const [carregando, setCarregando] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setCarregando(true);
    setErro(null);

    try {
      // Faz a chamada real para a rota pública do Java
      const resposta = await api.post('/auth/login', {
        login: usuario,
        senha: senha
      });

      // Se o Java devolver o token, salvamos no localStorage
      if (resposta.data && resposta.data.token) {
        localStorage.setItem('barbertech_token', resposta.data.token);
        onLoginSucesso(); // Avisa o App.tsx que deu certo!
      } else {
        setErro('Resposta inválida do servidor.');
      }
    } catch (err: any) {
      console.error('Erro ao fazer login:', err);
      setErro('Usuário ou senha incorretos ou servidor fora do ar.');
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center vh-100 bg-dark text-white">
      <div className="card bg-secondary p-4 shadow" style={{ width: '100%', maxWidth: '400px' }}>
        <div className="card-body">
          <h2 className="text-center mb-4 fw-bold">BarberTech</h2>
          <p className="text-center text-light small mb-4">Aceda ao sistema da Barbearia</p>

          {erro && <div className="alert alert-danger p-2 small">{erro}</div>}

          <form onSubmit={handleLogin}>
            <div className="mb-3">
              <label className="form-label small">Utilizador</label>
              <input
                type="text"
                className="form-control"
                value={usuario}
                onChange={(e) => setUsuario(e.target.value)}
                required
                disabled={carregando}
                placeholder="Ex: admin"
              />
            </div>

            <div className="mb-4">
              <label className="form-label small">Palavra-passe</label>
              <input
                type="password"
                className="form-control"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                required
                disabled={carregando}
                placeholder="••••••"
              />
            </div>

            <button type="submit" className="btn btn-primary w-100 fw-bold" disabled={carregando}>
              {carregando ? 'A autenticar...' : 'Entrar'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}