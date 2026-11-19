package unoeste.fipp.vagasonlinebackend.restcontrollers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.vagasonlinebackend.dto.InteresseRequest;
import unoeste.fipp.vagasonlinebackend.entities.Interesse;
import unoeste.fipp.vagasonlinebackend.services.VagaService;

@RestController
@RequestMapping("/apis/interesse")
public class InteresseRestController {
    private final VagaService vagaService;

    public InteresseRestController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @PostMapping("/candidato")
    public ResponseEntity<Interesse> registrar(@Valid @RequestBody InteresseRequest request) {
        return ResponseEntity.ok(vagaService.registrarInteresse(request));
    }
}
