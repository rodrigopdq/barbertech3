// src/types/IAgendamento.ts
export interface IAgendamento {
  id: number;
  cliente: string;
  tipo: 'Corte' | 'Barba'; 
  horario: string;
  status: 'agendado' | 'concluido';
  valor: number;
}