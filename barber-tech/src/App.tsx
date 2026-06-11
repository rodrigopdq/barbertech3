// src/App.tsx
import { useState, useEffect } from 'react';
import type { IAgendamento } from './types/IAgendamento';
import Sidebar from './components/Sidebar';
import CardServico from './components/CardServico';
import { api } from './services/api'; // Importa a conexão com o Java
import './styles/custom.css';

function App() {
  const [agendamentos, setAgendamentos] = useState<IAgendamento[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Função para buscar os agendamentos e cruzar com os clientes reais do Java
  const carregarDadosDoBanco = async () => {
    try {
      setLoading(true);
      
      // 1. Dispara buscas simultâneas para Agendamentos e Clientes no Java
      const [respostaAgendamentos, respostaClientes] = await Promise.all([
        api.get('/agendamento'),
        api.get('/cliente')
      ]);

      const listaClientes = respostaClientes.data; // Array com seus 5 clientes reais
      const listaAgendamentos = respostaAgendamentos.data; // Array com os agendamentos

      // 2. Mapeia os agendamentos substituindo o ID pelo Nome do cliente real
      const dadosFormatados: IAgendamento[] = listaAgendamentos.map((item: any) => {
        // Procura na lista de clientes aquele que possui o id igual ao clienteId do agendamento
        const clienteEncontrado = listaClientes.find((c: any) => c.id === item.clienteId);
        
        return {
          id: item.id,
          // Se achar o cliente no banco, usa o nome dele. Se não achar, mostra o ID de fallback
          cliente: clienteEncontrado ? clienteEncontrado.nome : `Cliente Desconhecido (ID #${item.clienteId})`,
          tipo: "Serviço Completo", 
          horario: item.dataHora ? item.dataHora.split('T')[1].substring(0, 5) : "00:00",
          status: "agendado",
          valor: 45
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

  // Executa a busca integrada assim que a tela abre
  useEffect(() => {
    carregarDadosDoBanco();
  }, []);

  // Função para concluir o serviço (Remove do MySQL via DELETE)
  const concluirServico = async (id: number) => {
    try {
      await api.delete(`/agendamento/${id}`);
      // Remove da tela localmente após o sucesso no banco
      setAgendamentos(agendamentos.filter(item => item.id !== id));
    } catch (err) {
      console.error("Erro ao deletar agendamento:", err);
      alert("Erro ao concluir o serviço no banco de dados.");
    }
  };

  return (
    <div className="container-fluid">
      <div className="row">
        {/* Coluna da Esquerda: Sidebar */}
        <div className="col-md-3 p-0">
          <Sidebar agendamentos={agendamentos} />
        </div>

        {/* Coluna da Direita: Conteúdo Principal */}
        <main className="col-md-9 py-4 px-5">
          <header className="mb-4">
            <h1 className="display-6 fw-bold">Agenda do Dia (Conectada ao Java)</h1>
            <p className="text-muted">Gerencie os atendimentos integrados em tempo real com o banco MySQL.</p>
          </header>

          {loading && (
            <div className="alert alert-info">Sincronizando agendamentos e clientes no banco...</div>
          )}

          {error && (
            <div className="alert alert-danger">{error}</div>
          )}

          {!loading && !error && agendamentos.length === 0 && (
            <div className="alert alert-warning">Nenhum agendamento encontrado no MySQL. Crie um no Postman para ver os nomes!</div>
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

          {/* Rodapé */}
          <footer className="mt-5 pt-4 border-top">
            <address>
              <strong>Rodrigo Pinheiro de Queiroz</strong><br />
              Data: 11 de Abril de 2026<br />
              Desenvolvimento de Software WEB | Prof. Alexandre Almeida
            </address>
          </footer>
        </main>
      </div>
    </div>
  );
}

export default App;