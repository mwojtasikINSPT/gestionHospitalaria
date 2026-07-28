package controllers;

import daos.CamaDAO;
import daos.PacienteDAO;
import daos.ReservaDAO;
import dtos.CamaDTO;
import dtos.PacienteDTO;
import dtos.ReservaDTO;
import models.Estado;
import java.util.List;

public class ReservaController {
    private final ReservaDAO reservaDAO;
    private final CamaDAO camaDAO;
    private final PacienteDAO pacienteDAO;

    public ReservaController() {
        this.reservaDAO = new ReservaDAO();
        this.camaDAO = new CamaDAO();
        this.pacienteDAO = new PacienteDAO();
    }

    public List<ReservaDTO> listarReservas() {
        return reservaDAO.obtenerRegistros();
    }

    public void reservarCama(String codigoCama, String idPaciente) {
        // 1. Validar que la cama exista
        CamaDTO cama = camaDAO.obtenerPorId(codigoCama);
        if (cama == null) {
            throw new IllegalArgumentException("No existe una cama con el código: " + codigoCama);
        }

        // 2. Validar que la cama esté LIBRE
        if (cama.getEstado() != Estado.LIBRE) {
            throw new IllegalArgumentException("La cama seleccionada no se encuentra libre (Estado actual: " + cama.getEstado() + ")");
        }

        // 3. Validar que el paciente exista
        PacienteDTO paciente = pacienteDAO.obtenerPorId(idPaciente);
        if (paciente == null) {
            throw new IllegalArgumentException("No existe un paciente con el ID: " + idPaciente);
        }

        // 4. Validar que el paciente no tenga ya una reserva activa
        boolean pacienteYaTieneReserva = reservaDAO.obtenerRegistros().stream()
                .anyMatch(r -> r.getIdPaciente().equalsIgnoreCase(idPaciente));
        if (pacienteYaTieneReserva) {
            throw new IllegalArgumentException("El paciente ya tiene una cama reservada.");
        }

        // 5. Crear la reserva y actualizar el estado de la cama a RESERVADA
        ReservaDTO reservaDTO = new ReservaDTO(codigoCama, idPaciente);
        reservaDAO.agregar(reservaDTO);

        cama.setEstado(Estado.OCUPADA);
        camaDAO.modificar(cama);
    }

    public void cancelarReserva(String codigoCama) {
        // 1. Verificar que realmente haya una reserva activa para esa cama
        ReservaDTO reserva = reservaDAO.obtenerPorId(codigoCama);
        if (reserva == null) {
            throw new IllegalArgumentException("No existe una reserva activa para la cama con código: " + codigoCama);
        }

        // 2. Eliminar la reserva
        reservaDAO.eliminar(codigoCama);

        // 3. Liberar la cama nuevamente
        CamaDTO cama = camaDAO.obtenerPorId(codigoCama);
        if (cama != null) {
            cama.setEstado(Estado.LIBRE);
            camaDAO.modificar(cama);
        }
    }

    public boolean verificarCamaEnUso(String codigoCama) {
        // Método de apoyo para validar antes de eliminar una cama en CamaController/CamaDAO
        ReservaDTO reserva = reservaDAO.obtenerPorId(codigoCama);
        return reserva != null;
    }
}