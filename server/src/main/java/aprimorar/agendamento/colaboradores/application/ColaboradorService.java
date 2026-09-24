package aprimorar.agendamento.colaboradores.application;

import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorPossuiRepassePendenteException;
import aprimorar.agendamento.colaboradores.infrastructure.ColaboradorRepository;
import aprimorar.agendamento.colaboradores.infrastructure.ColaboradorSpecifications;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradorFiltroRequest;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradorResponse;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradorOptionResponse;
import aprimorar.financeiro.repasses_colaboradores.api.PagamentosApi;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepo;
    private final PagamentosApi repasseApi;

    public ColaboradorService(ColaboradorRepository colaboradorRepo, PagamentosApi repasseApi) {
        this.colaboradorRepo = colaboradorRepo;
        this.repasseApi = repasseApi;
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorResponse> getColaboradores(ColaboradorFiltroRequest filtro, Pageable pageable) {
        Specification<Colaborador> spec = ColaboradorSpecifications.comFiltros(filtro);
        return colaboradorRepo.findAll(spec, pageable).map(ColaboradorResponse::toDto);
    }

    @Transactional(readOnly = true)
    public ColaboradorResponse findColaboradorById(UUID colaboradorId) {
        Colaborador colaborador = findColaboradorOrThrow(colaboradorId);
        return ColaboradorResponse.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    public List<ColaboradorOptionResponse> listColaboradoresOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        return colaboradorRepo
            .findAll(ColaboradorSpecifications.isActive(), sort)
            .stream()
            .map(ColaboradorOptionResponse::toDto)
            .toList();
    }

    @Transactional
    public UUID createColaborador(Colaborador colaborador) {
        Colaborador savedColaborador = colaboradorRepo.save(colaborador);
        return savedColaborador.getId();
    }

    @Transactional
    public void updateColaborador(UUID colaboradorId, Colaborador requestedColaborador) {
        Colaborador colaborador = findColaboradorOrThrow(colaboradorId);

        colaborador.update(
            requestedColaborador.getNome(),
            requestedColaborador.getDataNascimento(),
            requestedColaborador.getPix(),
            requestedColaborador.getTelefone(),
            requestedColaborador.getCpf(),
            requestedColaborador.getEmail(),
            requestedColaborador.getFuncao(),
            requestedColaborador.getEndereco()
        );

    }

    @Transactional
    public void deactivateColaborador(UUID colaboradorId) {
        Colaborador colaborador = findColaboradorOrThrow(colaboradorId);

        if (repasseApi.possuiRepassePendente(colaboradorId)) {
            throw new ColaboradorPossuiRepassePendenteException();
        }

        colaborador.deactivate();
    }

    @Transactional
    public void activateColaborador(UUID colaboradorId) {
        Colaborador colaborador = findColaboradorOrThrow(colaboradorId);
        colaborador.activate();
    }

    public Colaborador findColaboradorOrThrow(UUID colaboradorId) {
        return colaboradorRepo.findById(colaboradorId)
            .orElseThrow(() -> new ColaboradorNaoEncontradoException("Colaborador não encontrado no banco de dados"));
    }

}
