package dtos;

public class MedicoDTO {
    private final String id;
    private final String dni;
    private final String nombre;
    private final String apellido;
    private final String especialidad;

    public MedicoDTO(String id, String dni, String nombre, String apellido, String especialidad) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
    }

    public String getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
