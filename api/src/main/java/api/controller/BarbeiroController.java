package api.controller;

import api.dto.BarbeiroDto;
import api.service.BarbeiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiro")
@CrossOrigin(origins = "*")
public class BarbeiroController {

    @Autowired
    private BarbeiroService service;

    @PostMapping(value = "/salvar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@RequestBody BarbeiroDto barbeiroDto) {
        service.save(barbeiroDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Barbeiro cadastrado com sucesso!");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBarbeiro(@PathVariable("id") Long id) {
        return service.getBarbeiro(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BarbeiroDto>> listAll() {
        List<BarbeiroDto> lista = service.listAll();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody BarbeiroDto barbeiroDto) {
        return service.update(id, barbeiroDto);
    }
}