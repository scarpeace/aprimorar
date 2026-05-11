package aprimorar.appointment.api.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(){
        super("Evento não encontrado no banco de dados");
    }
}

