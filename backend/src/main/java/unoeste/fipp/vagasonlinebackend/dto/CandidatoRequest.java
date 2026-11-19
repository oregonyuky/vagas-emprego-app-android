package unoeste.fipp.vagasonlinebackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CandidatoRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "deve conter 11 dígitos") String cpf,
        @NotBlank @Email String email,
        @NotBlank @Pattern(
                regexp = "(?:[0-9]{10,11}|\\([0-9]{2}\\) [0-9]{4,5}-[0-9]{4})",
                message = "deve conter DDD e 10 ou 11 dígitos"
        ) String telefone,
        @Size(max = 500) String formacao
) {
}
