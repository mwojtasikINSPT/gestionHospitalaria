package models;

public class Medico extends Persona {
    private String especialidad;

    public Medico(String id, String dni, String nombre, String apellido, String especialidad) {
        super(id, dni, nombre, apellido);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}