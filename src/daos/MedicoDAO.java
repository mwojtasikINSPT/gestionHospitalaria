package daos;

import dtos.MedicoDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO implements ICrud<MedicoDTO, String> {

    private final String ARCHIVO = "medicos.txt";

    @Override
    public List<MedicoDTO> obtenerRegistros() {
        List<MedicoDTO> medicos = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return medicos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 5) {
                    MedicoDTO medico = new MedicoDTO(partes[0], partes[1], partes[2], partes[3], partes[4]);
                    medicos.add(medico);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de medicos. ", e);
        }
        return medicos;
    }

    public void guardarTodos(List<MedicoDTO> medicos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (MedicoDTO medico : medicos) {
                bw.write(medico.getId() + "," + 
                         medico.getDni() + "," + 
                         medico.getNombre() + "," + 
                         medico.getApellido() + "," + 
                         medico.getEspecialidad());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de medicos ", e);
        }
    }

    @Override
    public void agregar(MedicoDTO medico) {
        List<MedicoDTO> lista = obtenerRegistros();
        lista.add(medico);
        guardarTodos(lista);
    }

    @Override
    public MedicoDTO obtenerPorId(String id) {
        return obtenerRegistros().stream()
                .filter(m -> m.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificar(MedicoDTO medicoModificado) {
        List<MedicoDTO> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(medicoModificado.getId())) {
                lista.set(i, medicoModificado);
                guardarTodos(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(String id) {
        List<MedicoDTO> lista = obtenerRegistros();
        boolean eliminado = lista.removeIf(m -> m.getId().equalsIgnoreCase(id));

        if (eliminado) {
            guardarTodos(lista);
        }
    }
}