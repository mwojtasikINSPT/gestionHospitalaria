package controllers;

import daos.MedicoDAO;
import dtos.MedicoDTO;
import java.util.List;

public class MedicoController {

    private final MedicoDAO medicoDAO;

    public MedicoController() {
        this.medicoDAO = new MedicoDAO();
    }

    public void agregarMedico(String id, String dni, String nombre, String apellido, String especialidad) {
        if (medicoDAO.obtenerPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un médico con el ID: " + id);
        }
        MedicoDTO medicoDTO = new MedicoDTO(id, dni, nombre, apellido, especialidad);
        medicoDAO.agregar(medicoDTO);
    }

    public List<MedicoDTO> listarMedicos() {
        return medicoDAO.obtenerRegistros();
    }

    public MedicoDTO buscarMedicoPorId(String id) {
        MedicoDTO medico = medicoDAO.obtenerPorId(id);
        if (medico == null) {
            throw new IllegalArgumentException("No se encontró un médico con el ID: " + id);
        }
        return medico;
    }

    public void modificarMedico(String id, String dni, String nombre, String apellido, String especialidad) {
        if (medicoDAO.obtenerPorId(id) == null) {
            throw new IllegalArgumentException("No se puede modificar. No existe un médico con el ID: " + id);
        }
        MedicoDTO medicoDTO = new MedicoDTO(id, dni, nombre, apellido, especialidad);
        medicoDAO.modificar(medicoDTO);
    }

    public void eliminarMedico(String id) {
        if (medicoDAO.obtenerPorId(id) == null) {
            throw new IllegalArgumentException("No se puede eliminar. No existe un médico con el ID: " + id);
        }
        medicoDAO.eliminar(id);
    }

    // Valido unicidad DNI
    public boolean existeDni(String dni) {
        for (MedicoDTO m : listarMedicos()) {
            if (m.getDni().equals(dni)) {
                return true; // Ya existe
            }
        }
        return false;
    }

    // Método para obtener los IDs históricos desde el DAO
    public List<String> obtenerIdsHistoricos() {
        return medicoDAO.obtenerIdsHistoricos();
    }
}
