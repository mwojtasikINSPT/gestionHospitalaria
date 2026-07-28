package models;

public class Reserva {
    private String codigoCama;
    private String idPaciente;

    public Reserva(String codigoCama, String idPaciente) {
        this.codigoCama = codigoCama;
        this.idPaciente = idPaciente;
    }

    public String getCodigoCama() {
        return codigoCama;
    }

    public void setCodigoCama(String codigoCama) {
        this.codigoCama = codigoCama;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }
}