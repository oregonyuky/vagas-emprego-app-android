package unoeste.fipp.vagasonlinebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import unoeste.fipp.vagasonlinebackend.entities.Empresa;

public record VagaRequest(
        @NotBlank @Size(max = 30) String registro,
        @NotNull Empresa empresa,
        @NotBlank @Size(max = 150) String cargo,
        @NotBlank @Size(max = 100) String cidade,
        @NotBlank @Pattern(
                regexp = "(?i)(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)",
                message = "deve ser uma unidade federativa brasileira válida"
        ) String estado,
        @JsonProperty("pre_requisitos") @NotBlank @Size(max = 2000) String preRequisitos,
        @NotBlank @Size(max = 500) String formacao,
        @JsonProperty("conhecimentos_requeridos") @NotBlank @Size(max = 2000) String conhecimentosRequeridos,
        @NotBlank @Size(max = 50) String regime,
        @JsonProperty("jornada_trabalho") @NotBlank @Size(max = 100) String jornadaTrabalho,
        @NotBlank @Size(max = 50) String remuneracao
) {
}
