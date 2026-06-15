package aprimorar.atendimentos.repository.projections;


public interface RelatorioMensalAtendimentosProjection {

    Long getTotalAulas();
    Long getTotalMentoria();
    Long getTotalTerapia();
    Long getTotalOV();
    Long getTotalENEM();
    Long getTotalPAS();
    Long getTotalOutros();
}
