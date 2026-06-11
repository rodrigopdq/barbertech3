// src/components/CardServico.tsx
import type { IAgendamento } from '../types/IAgendamento';

interface Props {
  agendamento: IAgendamento;
  onConcluir: (id: number) => void;
}

const CardServico = ({ agendamento, onConcluir }: Props) => {
  const isConcluido = agendamento.status === 'concluido';

  return (
    <article className={`card mb-3 shadow-sm ${isConcluido ? 'opacity-50' : ''}`}>
      <div className="card-body d-flex justify-content-between align-items-center">
        <div>
          <h5 className="mb-1">{agendamento.cliente}</h5>
          <span className={`badge ${agendamento.tipo === 'Corte' ? 'bg-primary' : 'bg-danger'}`}>
            {agendamento.tipo}
          </span>
          <span className="ms-2 text-muted">{agendamento.horario}</span>
        </div>

        {!isConcluido && (
          <button 
            className="btn btn-outline-dark btn-sm"
            onClick={() => onConcluir(agendamento.id)}
          >
            Concluir
          </button>
        )}
        {isConcluido && <span className="text-success fw-bold">✓ Finalizado</span>}
      </div>
    </article>
  );
};

export default CardServico;