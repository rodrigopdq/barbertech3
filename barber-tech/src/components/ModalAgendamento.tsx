import { useState, useEffect } from 'react';
import { api } from '../services/api';

interface ModalAgendamentoProps {
  onClose: () => void;
  onSuccess: () => void;
}

export default function ModalAgendamento({ onClose, onSuccess }: ModalAgendamentoProps) {
  const [clientes, setClientes] = useState<any[]>([]);
  const [barbeiros, setBarbeiros] = useState<any[]>([]);
  const [clienteId, setClienteId] = useState('');
  const [barbeiroId, setBarbeiroId] = useState('');
  const [dataHora, setDataHora] = useState('');
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    api.get('/cliente').then(res => setClientes(res.data)).catch(err => console.error(err));
    api.get('/barbeiro').then(res => setBarbeiros(res.data)).catch(err => console.error(err));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!clienteId || !barbeiroId || !dataHora) return;

    setCarregando(true);
    setErro(null);

    try {
      const dataHoraFormatada = dataHora.includes('T')
        ? `${dataHora}:00`
        : `${dataHora.replace(' ', 'T')}:00`;

      await api.post('/agendamento/salvar', {
        clienteId: Number(clienteId),
        barbeiroId: Number(barbeiroId),
        dataHora: dataHoraFormatada
      });

      onSuccess();
      onClose();
    } catch (err: any) {
      console.error("Erro ao salvar agendamento:", err);
      if (err.response && err.response.data && err.response.data.message) {
        setErro(`Erro no Java: ${err.response.data.message}`);
      } else {
        setErro("Não foi possível salvar o agendamento no banco. Verifique as restrições do Java.");
      }
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="modal d-block" style={{ backgroundColor: 'rgba(0,0,0,0.7)' }} tabIndex={-1}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content bg-secondary text-white border-0 shadow">
          <div className="modal-header border-bottom border-dark">
            <h5 className="modal-title fw-bold">📅 Novo Agendamento</h5>
            <button type="button" className="btn-close btn-close-white" onClick={onClose}></button>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="modal-body">
              {erro && <div className="alert alert-danger p-2 small">{erro}</div>}

              <div className="mb-3">
                <label className="form-label small">Selecione o Cliente</label>
                <select
                  className="form-select"
                  value={clienteId}
                  onChange={(e) => setClienteId(e.target.value)}
                  required
                >
                  <option value="">-- Escolha um cliente --</option>
                  {clientes.map(c => (
                    <option key={c.id} value={c.id}>{c.nome}</option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label small">Selecione o Barbeiro</label>
                <select
                  className="form-select"
                  value={barbeiroId}
                  onChange={(e) => setBarbeiroId(e.target.value)}
                  required
                >
                  <option value="">-- Escolha um barbeiro --</option>
                  {barbeiros.map(b => (
                    <option key={b.id} value={b.id}>{b.nome}</option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label small">Data e Horário</label>
                <input
                  type="datetime-local"
                  className="form-control"
                  value={dataHora}
                  onChange={(e) => setDataHora(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="modal-footer border-top border-dark">
              <button type="button" className="btn btn-outline-light btn-sm" onClick={onClose} disabled={carregando}>
                Cancelar
              </button>
              <button type="submit" className="btn btn-primary btn-sm fw-bold" disabled={carregando}>
                {carregando ? 'A salvar...' : 'Confirmar Agendamento'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}