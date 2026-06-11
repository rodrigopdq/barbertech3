// src/components/Sidebar.tsx
import type { IAgendamento } from '../types/IAgendamento';

interface Props {
  agendamentos: IAgendamento[];
}

const Sidebar = ({ agendamentos }: Props) => {
  // Lógica do Dashboard 
  const concluidos = agendamentos.filter(a => a.status === 'concluido');
  const faturamento = concluidos.reduce((acc, curr) => acc + curr.valor, 0);
  const proximo = agendamentos.find(a => a.status === 'agendado');

  return (
    <aside className="p-4 text-white bg-dark vh-100 shadow sticky-top">
      <h2 className="h4 mb-5 border-bottom pb-3">BarberTech 💈</h2>
      
      <div className="mb-4">
        <label className="text-secondary small d-block">CONCLUÍDOS HOJE</label>
        <h3 className="text-info">{concluidos.length}</h3>
      </div>

      <div className="mb-4">
        <label className="text-secondary small d-block">FATURAMENTO</label>
        <h3 className="text-danger">R$ {faturamento.toFixed(2)}</h3>
      </div>

      <div className="mt-5 p-3 bg-secondary bg-opacity-25 rounded">
        <small className="text-secondary d-block">PRÓXIMO CLIENTE:</small>
        <strong>{proximo ? proximo.cliente : "Fim do expediente"}</strong>
      </div>
    </aside>
  );
};

export default Sidebar;