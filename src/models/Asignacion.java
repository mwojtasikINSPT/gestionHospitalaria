package models;

public class Asignacion {
    private String idMedico;
    private String idPaciente;

    public Asignacion(String idMedico, String idPaciente) {
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
    }

    public String getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(String idMedico) {
        this.idMedico = idMedico;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }
}