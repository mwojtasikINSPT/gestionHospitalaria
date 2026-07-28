package dtos;

import models.Estado;

public class CamaDTO {
    private final String codigo;
    private final int piso;
    private Estado estado;

    public CamaDTO(String codigo, int piso, Estado estado) {
        this.codigo = codigo;
        this.piso = piso;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getPiso() {
        return piso;
    }

    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
    this.estado = estado;
}
}