package api.mapper;

import api.dto.AgendamentoDto;
import api.model.Agendamento;
import api.model.Barbeiro;
import api.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public AgendamentoDto toDto(Agendamento agendamento) {
        if (agendamento == null) return null;
        
        AgendamentoDto dto = new AgendamentoDto();
        dto.setId(agendamento.getId());
        dto.setDataHora(agendamento.getDataHora());
        dto.setClienteId(agendamento.getCliente() != null ? agendamento.getCliente().getId() : null);
        dto.setBarbeiroId(agendamento.getBarbeiro() != null ? agendamento.getBarbeiro().getId() : null);
        
        return dto;
    }

    public Agendamento toModel(AgendamentoDto dto, Cliente cliente, Barbeiro barbeiro) {
        if (dto == null) return null;
        
        Agendamento agendamento = new Agendamento();
        agendamento.setId(dto.getId());
        agendamento.setDataHora(dto.getDataHora());
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        
        return agendamento;
    }
}