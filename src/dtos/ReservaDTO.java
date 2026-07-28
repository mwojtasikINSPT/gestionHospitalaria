package dtos;

public class ReservaDTO {
    private final String codigoCama;
    private final String idPaciente;

    public ReservaDTO(String codigoCama, String idPaciente) {
        this.codigoCama = codigoCama;
        this.idPaciente = idPaciente;
    }

    public String getCodigoCama() {
        return codigoCama;
    }

    public String getIdPaciente() {
        return idPaciente;
    }
}