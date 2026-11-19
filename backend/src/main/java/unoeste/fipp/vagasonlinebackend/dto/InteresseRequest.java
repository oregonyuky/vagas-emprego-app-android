package unoeste.fipp.vagasonlinebackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InteresseRequest(
        @NotBlank String vagaId,
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "deve conter 11 dígitos") String cpf,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "[0-9()+\\-\\s]{8,20}", message = "telefone inválido") String telefone
) {
}
