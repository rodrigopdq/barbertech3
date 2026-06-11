package api.service;

import api.dto.AgendamentoDto;
import api.mapper.AgendamentoMapper;
import api.model.Agendamento;
import api.model.Barbeiro;
import api.model.Cliente;
import api.repository.AgendamentoRepository;
import api.repository.ClienteRepository;
import api.repository.BarbeiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private AgendamentoMapper mapper;

    public ResponseEntity<?> save(AgendamentoDto dto) {
        Optional<Cliente> cliente = clienteRepository.findById(dto.getClienteId());
        Optional<Barbeiro> barbeiro = barbeiroRepository.findById(dto.getBarbeiroId());

        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Cliente não encontrado!");
        }
        if (barbeiro.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Barbeiro não encontrado!");
        }

        Agendamento entity = mapper.toModel(dto, cliente.get(), barbeiro.get());
        agendamentoRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Agendamento realizado com sucesso!");
    }

    public List<AgendamentoDto> listAll() {
        return agendamentoRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> delete(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento cancelado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado!");
    }
}