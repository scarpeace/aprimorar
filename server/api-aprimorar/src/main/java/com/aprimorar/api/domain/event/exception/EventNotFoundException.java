package com.aprimorar.api.domain.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(){
        super("Evento não encontrado no banco de dados");
    }
}

