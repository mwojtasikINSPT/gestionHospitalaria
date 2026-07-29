package dtos;

public class AsignacionDTO {
    private String idMedico;
    private final String idPaciente;

    public AsignacionDTO(String idMedico, String idPaciente) {
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
}