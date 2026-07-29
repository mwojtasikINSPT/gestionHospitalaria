package daos;

import dtos.AsignacionDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO implements ICrud<AsignacionDTO, String> {

    private final String ARCHIVO = "asignaciones.txt";

    @Override
    public List<AsignacionDTO> obtenerRegistros() {
        List<AsignacionDTO> asignaciones = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return asignaciones;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    AsignacionDTO asignacion = new AsignacionDTO(partes[0], partes[1]);
                    asignaciones.add(asignacion);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de asignaciones. ", e);
        }
        return asignaciones;
    }

    public void guardarTodas(List<AsignacionDTO> asignaciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (AsignacionDTO asignacion : asignaciones) {
                bw.write(asignacion.getIdMedico() + "," + 
                         asignacion.getIdPaciente());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de asignaciones ", e);
        }
    }

    @Override
    public void agregar(AsignacionDTO asignacion) {
        List<AsignacionDTO> lista = obtenerRegistros();
        lista.add(asignacion);
        guardarTodas(lista);
    }

    public AsignacionDTO obtenerPorPaciente(String idPaciente) {
        return obtenerRegistros().stream()
                .filter(a -> a.getIdPaciente().equalsIgnoreCase(idPaciente))
                .findFirst()
                .orElse(null);
    }

    @Override
    public AsignacionDTO obtenerPorId(String idPaciente) {
        return obtenerPorPaciente(idPaciente);
    }

    @Override
    public void modificar(AsignacionDTO asignacionModificada) {
        List<AsignacionDTO> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdPaciente().equalsIgnoreCase(asignacionModificada.getIdPaciente())) {
                lista.set(i, asignacionModificada);
                guardarTodas(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(String idPaciente) {
        List<AsignacionDTO> lista = obtenerRegistros();
        boolean eliminada = lista.removeIf(a -> a.getIdPaciente().equalsIgnoreCase(idPaciente));

        if (eliminada) {
            guardarTodas(lista);
        }
    }
}