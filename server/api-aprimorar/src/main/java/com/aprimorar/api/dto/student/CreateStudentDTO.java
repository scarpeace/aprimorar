package com.aprimorar.api.dto.student;

import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public record CreateStudentDTO(
        @NotNull(message = "Nome do estudante é obrigatório")
        String name,

        @NotNull(message = "A data de nascimento do estudante é obrigatória")
        @PastOrPresent(message = "A data de nascimento do estudante não pode ser no futuro")
        LocalDate birthdate,

        @NotNull(message = "CPF do estudante é obrigatório")
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        @NotBlank(message = "Escola do estudante é obrigatória")
        String school,

        @NotBlank(message = "Contato do estudante é obrigatório")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", message = "Contato deve estar no formato (XX)XXXXX-XXXX")
        String contact,

        @NotBlank(message = "Email do estudante é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Endereço do estudante é obrigatório")
        @Valid
        CreateAddressDTO address,

        UUID parentId,

        @Valid
        CreateParentDTO parent
) {

    //TODO Essa validação também ocorre no service. Preciso verificar se a validação do service é realmente necessária.
    //Pode haver uma chance de grande refatoração para manter o service mais limpo e com funções mais organizadas.
    @AssertTrue(message = "Informe parentId ou parent")
    public boolean hasParentReference() {
        return parentId != null || parent != null;
    }

    @AssertTrue(message = "Informe somente um entre parentId e parent")
    public boolean isParentReferenceConsistent() {
        return parentId == null || parent == null;
    }
}
