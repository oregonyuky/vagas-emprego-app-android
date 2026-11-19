package unoeste.fipp.vagasonlinebackend.restcontrollers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.vagasonlinebackend.dto.VagaRequest;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;
import unoeste.fipp.vagasonlinebackend.services.VagaService;

import java.util.List;

@RestController
@RequestMapping("/apis/vagas")
public class VagaRestController {
    private final VagaService vagaService;

    public VagaRestController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @PostMapping("/vaga")
    public ResponseEntity<Vaga> criar(@Valid @RequestBody VagaRequest request) {
        return ResponseEntity.ok(vagaService.criar(request));
    }

    @PutMapping
    public ResponseEntity<Vaga> atualizar(@Valid @RequestBody VagaRequest request) {
        if (!StringUtils.hasText(request.id())) {
            throw new IllegalArgumentException("O registro da vaga é obrigatório");
        }
        return ResponseEntity.ok(vagaService.atualizar(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        vagaService.excluir(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Vaga>> listar() {
        return ResponseEntity.ok(vagaService.listar());
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<Vaga> buscar(@PathVariable String id) {
        return ResponseEntity.ok(vagaService.buscar(id));
    }
}
