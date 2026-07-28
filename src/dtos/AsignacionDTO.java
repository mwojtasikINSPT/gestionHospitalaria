package dtos;

public class AsignacionDTO {
    private final String idMedico;
    private final String idPaciente;

    public AsignacionDTO(String idMedico, String idPaciente) {
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
    }

    public String getIdMedico() {
        return idMedico;
    }

    public String getIdPaciente() {
        return idPaciente;
    }
}