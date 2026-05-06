package com.aprimorar.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de conteúdo ou categoria do atendimento/evento")
public enum EventContent {
    @Schema(description = "Aulas regulares")
    AULA,
    
    @Schema(description = "Sessão de mentoria")
    MENTORIA,
    
    @Schema(description = "Sessão de terapia")
    TERAPIA,
    
    @Schema(description = "Sessão de Orientação Vocacional (O.V)")
    ORIENTACAO_VOCACIONAL,
    
    @Schema(description = "Preparatório para o ENEM")
    ENEM,
    
    @Schema(description = "Preparatório para o PAS")
    PAS,
    
    @Schema(description = "Outros tipos de evento")
    OUTRO
}
