package controllers;

import daos.CamaDAO;
import daos.ReservaDAO;
import dtos.CamaDTO;
import dtos.ReservaDTO;
import models.Estado;
import utils.Mensajes;
import java.util.List;

public class CamaController {
    private final CamaDAO camaDAO;
    private final ReservaDAO reservaDAO;

    public CamaController() {
        this.camaDAO = new CamaDAO();
        this.reservaDAO = new ReservaDAO();
    }

    public void agregarCama(String codigo, int piso, Estado estado) {
        if (camaDAO.obtenerPorId(codigo) != null) {
            throw new IllegalArgumentException("Ya existe una cama con el código: " + codigo);
        }
        CamaDTO camaDTO = new CamaDTO(codigo, piso, estado);
        camaDAO.agregar(camaDTO);
    }

    public List<CamaDTO> listarCamas() {
        return camaDAO.obtenerRegistros();
    }

    public CamaDTO buscarCamaPorId(String codigo) {
        CamaDTO cama = camaDAO.obtenerPorId(codigo);
        if (cama == null) {
            throw new IllegalArgumentException("No se encontró una cama con el código: " + codigo);
        }
        return cama;
    }

    public void modificarCama(String codigo, int piso, Estado estado) {
        if (camaDAO.obtenerPorId(codigo) == null) {
            throw new IllegalArgumentException("No se puede modificar. No existe una cama con el código: " + codigo);
        }
        CamaDTO camaDTO = new CamaDTO(codigo, piso, estado);
        camaDAO.modificar(camaDTO);
    }

    public void eliminarCama(String codigo) {
        if (camaDAO.obtenerPorId(codigo) == null) {
            throw new IllegalArgumentException("No se puede eliminar. No existe una cama con el código: " + codigo);
        }
        
        // Verificar que la cama no esté reservada/en uso
        ReservaDTO reserva = reservaDAO.obtenerPorId(codigo);
        if (reserva != null) {
            throw new IllegalArgumentException(Mensajes.ERROR_ELIMINAR_EN_USO);
        }

        camaDAO.eliminar(codigo);
    }

    // Método para obtener los IDs históricos desde el DAO
    public List<String> obtenerIdsHistoricos() {
        return camaDAO.obtenerIdsHistoricos();
    }
}