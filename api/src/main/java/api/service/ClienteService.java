package api.service;

import api.dto.ClienteDto;
import api.mapper.ClienteMapper;
import api.model.Cliente;
import api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private final ClienteMapper mapper = ClienteMapper.INSTANCE;

    public void save(ClienteDto clienteDto) {
        Cliente entity = mapper.toModel(clienteDto);
        clienteRepository.save(entity);
    }

    public ResponseEntity<?> getCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(cliente.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
    }

    public List<ClienteDto> listAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> delete(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente removido com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado para remoção!");
    }

    public ResponseEntity<?> update(Long id, ClienteDto clienteDto) {
        Optional<Cliente> existente = clienteRepository.findById(id);
        if (existente.isPresent()) {
            Cliente clienteAlterado = existente.get();
            clienteAlterado.setNome(clienteDto.getNome());
            clienteAlterado.setCpf(clienteDto.getCpf());
            clienteAlterado.setTelefone(clienteDto.getTelefone());
            clienteAlterado.setEndereco(clienteDto.getEndereco());
            
            clienteRepository.save(clienteAlterado);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(clienteAlterado));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado para atualização!");
    }
}