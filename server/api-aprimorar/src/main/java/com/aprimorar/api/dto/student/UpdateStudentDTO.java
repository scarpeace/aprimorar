package com.aprimorar.api.dto.student;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.validation.ValidationPatterns;
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

        @Pattern(regexp = ValidationPatterns.CPF, message = ValidationPatterns.CPF_MESSAGE)
        String cpf,

        String school,

        @Pattern(regexp = ValidationPatterns.PHONE_BR, message = ValidationPatterns.PHONE_BR_MESSAGE)
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
