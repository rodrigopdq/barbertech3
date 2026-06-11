package api.service;

import api.dto.BarbeiroDto;
import api.mapper.BarbeiroMapper;
import api.model.Barbeiro;
import api.repository.BarbeiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BarbeiroService {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    private final BarbeiroMapper mapper = BarbeiroMapper.INSTANCE;

    public void save(BarbeiroDto barbeiroDto) {
        Barbeiro entity = mapper.toModel(barbeiroDto);
        barbeiroRepository.save(entity);
    }

    public ResponseEntity<?> getBarbeiro(Long id) {
        Optional<Barbeiro> barbeiro = barbeiroRepository.findById(id);
        if (barbeiro.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(barbeiro.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barbeiro não encontrado!");
    }

    public List<BarbeiroDto> listAll() {
        List<Barbeiro> barbeiros = barbeiroRepository.findAll();
        return barbeiros.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> delete(Long id) {
        if (barbeiroRepository.existsById(id)) {
            barbeiroRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Barbeiro removido com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barbeiro não encontrado para remoção!");
    }

    public ResponseEntity<?> update(Long id, BarbeiroDto barbeiroDto) {
        Optional<Barbeiro> existente = barbeiroRepository.findById(id);
        if (existente.isPresent()) {
            Barbeiro barbeiroAlterado = existente.get();
            barbeiroAlterado.setNome(barbeiroDto.getNome());
            barbeiroAlterado.setCpf(barbeiroDto.getCpf());
            barbeiroAlterado.setTelefone(barbeiroDto.getTelefone());
            barbeiroAlterado.setEspecialidade(barbeiroDto.getEspecialidade());
            
            barbeiroRepository.save(barbeiroAlterado);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(barbeiroAlterado));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barbeiro não encontrado para atualização!");
    }
}