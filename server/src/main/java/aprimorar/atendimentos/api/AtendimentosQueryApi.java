package aprimorar.atendimentos.api;

import aprimorar.atendimentos.api.dto.AtendimentosAlunosKpisDTO;
import aprimorar.atendimentos.api.dto.AtendimentosAlunoResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentosKpisDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentosColaboradorResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentosColaboradorKpisDTO;
import aprimorar.shared.PageDTO;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AtendimentosQueryApi {

    PageDTO<AtendimentoResponseDTO> getAtendimentos(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    );

    AtendimentoResponseDTO findAtendimentoById(UUID id);

    AtendimentosColaboradorResponseDTO getAtendimentosByColaborador(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    );

    AtendimentosAlunoResponseDTO getAtendimentosByAluno(
        Pageable pageable,
        UUID studentId,
        Instant startDate,
        Instant endDate,
        Boolean charged
    );
    AtendimentosKpisDTO getKpisAtendimentos(Instant startDate, Instant endDate);
    PageDTO<AtendimentosColaboradorKpisDTO> getKpisAtendimentosColaboradores(
        Pageable pageable,
        Instant startDate,
        Instant endDate
    );
    PageDTO<AtendimentosAlunosKpisDTO> getKpisAtendimentosAlunos(
        Pageable pageable,
        Instant startDate,
        Instant endDate
    );

    boolean alunoHasPendingCharges(UUID alunoId);
    boolean colaboradorHasPendingPayment(UUID colaboradorId);
}
