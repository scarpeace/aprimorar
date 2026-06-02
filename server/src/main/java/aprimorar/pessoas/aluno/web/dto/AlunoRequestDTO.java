package aprimorar.pessoas.aluno.web.dto;

import java.time.LocalDate;
import java.util.UUID;

import aprimorar.pessoas.common.address.dto.AddressRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Schema(description = "Formato de payload para o cadastro de um aluno")
public record AlunoRequestDTO(
    @NotBlank(message = "O Nome do aluno é obrigatório")
    @Schema(nullable = false, description = "Nome do aluno", example = "John Doe")
    String name,

    @NotNull(message = "Data de nascimento do aluno é obrigatória")
    @PastOrPresent()
    @Schema(nullable = false, description = "Data de nascimento do aluno", example = "2000-01-01")
    LocalDate birthdate,

    @Schema(nullable = true, description = "Pix do aluno", example = "pix@example.com")
    String pix,

    @NotBlank(message = "O CPF do aluno é obrigatório")
    @Schema(nullable = false, description = "CPF do aluno", example = "123.456.789-00")
    String cpf,

    @NotBlank(message = "A escola do aluno é obrigatória")
    @Schema(nullable = false,description = "Escola do aluno", example = "School Name")
    String school,

    @NotBlank(message = "O Contato do aluno é obrigatório")
    @Schema(nullable = false,description = "Contato do aluno", example = "(61) 99999-9999")
    String contact,

    @NotBlank(message = "O email do aluno é obrigatório")
    @Email()
    @Schema(nullable = false,description = "Email do aluno", example = "john.doe@example.com")
    String email,

    @NotNull(message = "O Endereço do aluno é obrigatório")
    @Schema(nullable = false,description = "Endereço do aluno", implementation = AddressRequestDTO.class)
    AddressRequestDTO address,

    @NotNull(message = "Aluno não pode ser criado sem um responsável")
    @Schema(nullable = false,description = "ID do responsável atual do aluno")
    UUID parentId
) {}
