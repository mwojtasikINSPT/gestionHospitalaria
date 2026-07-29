package controllers;

import daos.AsignacionDAO;
import daos.MedicoDAO;
import daos.PacienteDAO;
import dtos.AsignacionDTO;
import dtos.MedicoDTO;
import dtos.PacienteDTO;
import utils.Mensajes;
import java.util.List;

public class AsignacionController {
    private final AsignacionDAO asignacionDAO;
    private final MedicoDAO medicoDAO;
    private final PacienteDAO pacienteDAO;

    public AsignacionController() {
        this.asignacionDAO = new AsignacionDAO();
        this.medicoDAO = new MedicoDAO();
        this.pacienteDAO = new PacienteDAO();
    }

    public List<AsignacionDTO> listarAsignaciones() {
        return asignacionDAO.obtenerRegistros();
    }

    public void asignarMedicoAPaciente(String idMedico, String idPaciente) {
        // 1. Validar que el médico exista
        MedicoDTO medico = medicoDAO.obtenerPorId(idMedico);
        if (medico == null) {
            throw new IllegalArgumentException("No existe un médico con el ID: " + idMedico);
        }

        // 2. Validar que el paciente exista
        PacienteDTO paciente = pacienteDAO.obtenerPorId(idPaciente);
        if (paciente == null) {
            throw new IllegalArgumentException("No existe un paciente con el ID: " + idPaciente);
        }

        // 3. Validar que el paciente no tenga ya un médico asignado (un paciente no puede tener dos médicos)
        boolean pacienteYaTieneMedico = asignacionDAO.obtenerRegistros().stream()
                .anyMatch(a -> a.getIdPaciente().equalsIgnoreCase(idPaciente));
        if (pacienteYaTieneMedico) {
            throw new IllegalArgumentException("El paciente ya tiene un médico asignado.");
        }

        // 4. Crear la asignación
        AsignacionDTO asignacionDTO = new AsignacionDTO(idMedico, idPaciente);
        asignacionDAO.agregar(asignacionDTO);
    }

    public void modificarAsignacion(String idPaciente, String nuevoIdMedico) {
        AsignacionDTO asignacionExistente = asignacionDAO.obtenerPorPaciente(idPaciente);
        if (asignacionExistente == null) {
            throw new IllegalArgumentException("El paciente no tiene un médico asignado.");
        }

        MedicoDTO medico = medicoDAO.obtenerPorId(nuevoIdMedico);
        if (medico == null) {
            throw new IllegalArgumentException("No existe un médico con el ID: " + nuevoIdMedico);
        }

        asignacionExistente.setIdMedico(nuevoIdMedico);
        asignacionDAO.modificar(asignacionExistente);
    }

    public void cancelarAsignacion(String idPaciente) {
        // 1. Verificar que exista una asignación activa para ese paciente antes de eliminarla
        AsignacionDTO asignacion = asignacionDAO.obtenerPorPaciente(idPaciente);
        if (asignacion == null) {
            throw new IllegalArgumentException(Mensajes.ERROR_NO_ENCONTRADO);
        }

        // 2. Eliminar la asignación
        asignacionDAO.eliminar(idPaciente);
    }
}