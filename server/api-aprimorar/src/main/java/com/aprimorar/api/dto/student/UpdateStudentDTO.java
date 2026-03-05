package com.aprimorar.api.dto.student;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateStudentDTO(
        String name,

        @PastOrPresent(message = "A data de nascimento do estudante não pode ser futura")
        LocalDate birthdate,

        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        String school,

        @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", message = "Contato deve estar no formato (XX)XXXXX-XXXX")
        String contact,

        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @Valid
        CreateAddressDTO address,

        UUID parentId,

        @Valid
        CreateParentDTO parent
) {

    @AssertTrue(message = "Pelo menos um campo deve ser informado para atualização")
    public boolean hasAnyFieldToUpdate() {
        return name != null
                || birthdate != null
                || cpf != null
                || school != null
                || contact != null
                || email != null
                || address != null
                || parentId != null
                || parent != null;
    }

    @AssertTrue(message = "Informe somente um entre parentId e parent")
    public boolean isParentReferenceConsistent() {
        return parentId == null || parent == null;
    }
}
