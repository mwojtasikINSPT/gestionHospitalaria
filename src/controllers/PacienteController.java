package controllers;

import daos.PacienteDAO;
import daos.ReservaDAO;
import dtos.PacienteDTO;
import utils.Mensajes;
import java.util.List;

public class PacienteController {

    private final PacienteDAO pacienteDAO;
    private final ReservaDAO reservaDAO;

    public PacienteController() {
        this.pacienteDAO = new PacienteDAO();
        this.reservaDAO = new ReservaDAO();
    }

    public void agregarPaciente(String id, String dni, String nombre, String apellido) {
        if (pacienteDAO.obtenerPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un paciente con el ID: " + id);
        }
        PacienteDTO pacienteDTO = new PacienteDTO(id, dni, nombre, apellido);
        pacienteDAO.agregar(pacienteDTO);
    }

    public List<PacienteDTO> listarPacientes() {
        return pacienteDAO.obtenerRegistros();
    }

    public PacienteDTO buscarPacientePorId(String id) {
        PacienteDTO paciente = pacienteDAO.obtenerPorId(id);
        if (paciente == null) {
            throw new IllegalArgumentException("No se encontró un paciente con el ID: " + id);
        }
        return paciente;
    }

    public void modificarPaciente(String id, String dni, String nombre, String apellido) {
        if (pacienteDAO.obtenerPorId(id) == null) {
            throw new IllegalArgumentException("No se puede modificar. No existe un paciente con el ID: " + id);
        }
        PacienteDTO pacienteDTO = new PacienteDTO(id, dni, nombre, apellido);
        pacienteDAO.modificar(pacienteDTO);
    }

    public void eliminarPaciente(String id) {
        if (pacienteDAO.obtenerPorId(id) == null) {
            throw new IllegalArgumentException("No se puede eliminar. No existe un paciente con el ID: " + id);
        }

        // Verificar que el paciente no tenga una reserva activa asignada
        boolean tieneReservaActiva = reservaDAO.obtenerRegistros().stream()
                .anyMatch(r -> r.getIdPaciente().equalsIgnoreCase(id));

        if (tieneReservaActiva) {
            throw new IllegalArgumentException(Mensajes.ERROR_ELIMINAR_EN_USO);
        }

        pacienteDAO.eliminar(id);
    }

    // Valido unicidad DNI
    public boolean existeDni(String dni) {
        for (PacienteDTO p : listarPacientes()) {
            if (p.getDni().equals(dni)) {
                return true; // Ya existe
            }
        }
        return false;
    }
}
