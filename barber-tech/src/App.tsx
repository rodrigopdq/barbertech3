// src/App.tsx
import { useState, useEffect } from 'react';
import type { IAgendamento } from './types/IAgendamento';
import Sidebar from './components/Sidebar';
import CardServico from './components/CardServico';
import Login from './components/Login';
import ModalAgendamento from './components/ModalAgendamento'; // Importa o novo modal
import { api } from './services/api'; 
import './styles/custom.css';

function App() {
  const [autenticado, setAutenticado] = useState<boolean>(!!localStorage.getItem('barbertech_token'));
  const [agendamentos, setAgendamentos] = useState<IAgendamento[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  
  // Estado para controlar a abertura do modal de cadastro
  const [modalAberto, setModalAberto] = useState<boolean>(false);

  const carregarDadosDoBanco = async () => {
    try {
      setLoading(true);
      
      const [respostaAgendamentos, respostaClientes] = await Promise.all([
        api.get('/agendamento'),
        api.get('/cliente')
      ]);

      const listaClientes = respostaClientes.data; 
      const listaAgendamentos = respostaAgendamentos.data; 

      const dadosFormatados: IAgendamento[] = listaAgendamentos.map((item: any) => {
        const clienteEncontrado = listaClientes.find((c: any) => c.id === item.clienteId);
        
        return {
          id: item.id,
          cliente: clienteEncontrado ? clienteEncontrado.nome : `Cliente Desconhecido (ID #${item.clienteId})`,
          tipo: "Serviço Completo", 
          horario: item.dataHora ? item.dataHora.split('T')[1].substring(0, 5) : "00:00",
          status: "agendado",
          value: 45
        };
      });

      setAgendamentos(dadosFormatados);
      setError(null);
    } catch (err) {
      console.error("Erro ao buscar dados integrados do backend:", err);
      setError("Não foi possível conectar ao servidor Java na porta 8080 ou sincronizar dados.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (autenticado) {
      carregarDadosDoBanco();
    }
  }, [autenticado]);

  const concluirServico = async (id: number) => {
    try {
      await api.delete(`/agendamento/${id}`);
      setAgendamentos(agendamentos.filter(item => item.id !== id));
    } catch (err) {
      console.error("Erro ao deletar agendamento:", err);
      alert("Erro ao concluir o serviço no banco de dados.");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('barbertech_token');
    setAutenticado(false);
    setAgendamentos([]);
  };

  if (!autenticado) {
    return <Login onLoginSucesso={() => setAutenticado(true)} />;
  }

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-md-3 p-0">
          <Sidebar agendamentos={agendamentos} />
          <div className="p-3 bg-dark text-center">
            <button className="btn btn-outline-danger btn-sm w-100" onClick={handleLogout}>
              Sair do Sistema
            </button>
          </div>
        </div>

        <main className="col-md-9 py-4 px-5">
          <header className="mb-4 d-flex justify-content-between align-items-center">
            <div>
              <h1 className="display-6 fw-bold">Agenda do Dia (Conectada ao Java)</h1>
              <p className="text-muted">Gerencie os atendimentos integrados em tempo real com o banco MySQL.</p>
            </div>
            
            {/* 🆕 BOTÃO DE ADICIONAR NOVO AGENDAMENTO */}
            <button 
              className="btn btn-primary fw-bold px-4 py-2"
              onClick={() => setModalAberto(true)}
            >
              + Novo Agendamento
            </button>
          </header>

          {loading && (
            <div className="alert alert-info">Sincronizando agendamentos e clientes no banco...</div>
          )}

          {error && (
            <div className="alert alert-danger">{error}</div>
          )}

          {!loading && !error && agendamentos.length === 0 && (
            <div className="alert alert-warning">Nenhum agendamento encontrado no MySQL. Crie um novo botão acima!</div>
          )}

          <section>
            {!loading && agendamentos.map(item => (
              <CardServico 
                key={item.id} 
                agendamento={item} 
                onConcluir={concluirServico} 
              />
            ))}
          </section>

          <footer className="mt-5 pt-4 border-top">
            <address>
              <strong>Rodrigo Pinheiro de Queiroz</strong><br />
              Data: 11 de Abril de 2026<br />
              Desenvolvimento de Software WEB | Prof. Alexandre Almeida
            </address>
          </footer>
        </main>
      </div>

      {/* 🆕 RENDERIZA O MODAL FLUTUANTE SE ESTIVER ABERTO */}
      {modalAberto && (
        <ModalAgendamento 
          onClose={() => setModalAberto(false)} 
          onSuccess={carregarDadosDoBanco} 
        />
      )}
    </div>
  );
}

export default App;