package models;

public class Cama {
    private String codigo;
    private int piso;
    private Estado estado;

    public Cama(String codigo, int piso, Estado estado) {
        this.codigo = codigo;
        this.piso = piso;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}