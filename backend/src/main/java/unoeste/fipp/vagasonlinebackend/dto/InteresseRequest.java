package unoeste.fipp.vagasonlinebackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;

public record InteresseRequest(
        @NotNull Vaga vaga,
        @NotNull @Valid CandidatoRequest candidato
) {
}
