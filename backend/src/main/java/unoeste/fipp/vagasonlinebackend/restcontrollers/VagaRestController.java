package unoeste.fipp.vagasonlinebackend.restcontrollers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.vagasonlinebackend.dto.InteresseRequest;
import unoeste.fipp.vagasonlinebackend.dto.VagaRequest;
import unoeste.fipp.vagasonlinebackend.entities.Interesse;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;
import unoeste.fipp.vagasonlinebackend.services.VagaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaRestController {
    private final VagaService vagaService;

    public VagaRestController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @GetMapping
    public ResponseEntity<List<Vaga>> listar(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) String empresa) {
        return ResponseEntity.ok(vagaService.listar(cidade, estado, cargo, empresa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaga> buscar(@PathVariable String id) {
        return ResponseEntity.ok(vagaService.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Vaga> criar(@Valid @RequestBody VagaRequest request) {
        Vaga criada = vagaService.criar(request);
        return ResponseEntity.created(URI.create("/api/vagas/" + criada.getId())).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vaga> atualizar(@PathVariable String id, @Valid @RequestBody VagaRequest request) {
        return ResponseEntity.ok(vagaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/interesses")
    public ResponseEntity<Interesse> registrarInteresse(
            @PathVariable String id, @Valid @RequestBody InteresseRequest request) {
        Interesse interesse = vagaService.registrarInteresse(id, request);
        return ResponseEntity.created(URI.create("/api/vagas/" + id + "/interesses/" + interesse.getId()))
                .body(interesse);
    }

    @GetMapping("/{id}/interesses")
    public ResponseEntity<List<Interesse>> interesses(@PathVariable String id) {
        return ResponseEntity.ok(vagaService.listarInteresses(id));
    }
}
