package daos;

import dtos.PacienteDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements ICrud<PacienteDTO, String> {

    private final String ARCHIVO = "pacientes.txt";

    @Override
    public List<PacienteDTO> obtenerRegistros() {
        List<PacienteDTO> pacientes = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return pacientes;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    PacienteDTO paciente = new PacienteDTO(partes[0], partes[1], partes[2], partes[3]);
                    pacientes.add(paciente);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de pacientes. ", e);
        }
        return pacientes;
    }

    public void guardarTodos(List<PacienteDTO> pacientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (PacienteDTO paciente : pacientes) {
                bw.write(paciente.getId() + ";" + 
                         paciente.getDni() + ";" + 
                         paciente.getNombre() + ";" + 
                         paciente.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de pacientes ", e);
        }
    }

    @Override
    public void agregar(PacienteDTO paciente) {
        List<PacienteDTO> lista = obtenerRegistros();
        lista.add(paciente);
        guardarTodos(lista);
    }

    @Override
    public PacienteDTO obtenerPorId(String id) {
        return obtenerRegistros().stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificar(PacienteDTO pacienteModificado) {
        List<PacienteDTO> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(pacienteModificado.getId())) {
                lista.set(i, pacienteModificado);
                guardarTodos(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(String id) {
        List<PacienteDTO> lista = obtenerRegistros();
        boolean eliminado = lista.removeIf(p -> p.getId().equalsIgnoreCase(id));

        if (eliminado) {
            guardarTodos(lista);
        }
    }
}