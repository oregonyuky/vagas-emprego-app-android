package unoeste.fipp.vagasonlinebackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VagaRequest(
        @NotBlank String empresaId,
        @NotBlank String cargoId,
        @NotBlank @Size(max = 100) String cidade,
        @NotBlank @Pattern(regexp = "(?i)[A-Z]{2}", message = "deve conter a sigla com 2 letras") String estado,
        @NotBlank @Size(max = 2000) String requisitos,
        @NotBlank @Size(max = 500) String formacao,
        @NotBlank @Size(max = 2000) String conhecimentos,
        @NotBlank @Pattern(regexp = "(?i)CLT|PJ", message = "deve ser CLT ou PJ") String regime,
        @NotBlank @Size(max = 100) String jornada,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal remuneracao
) {
}
