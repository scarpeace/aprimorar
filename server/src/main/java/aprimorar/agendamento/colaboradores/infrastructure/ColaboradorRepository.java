package aprimorar.agendamento.colaboradores.infrastructure;

import aprimorar.agendamento.colaboradores.domain.Colaborador;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID>, JpaSpecificationExecutor<Colaborador> {
}
