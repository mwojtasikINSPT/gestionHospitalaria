package daos;

import dtos.MedicoDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO implements ICrud<MedicoDTO, String> {

    private final String ARCHIVO = "medicos.txt";    
    private final String ARCHIVO_HISTORICOS = "medicosIdHistoricos.txt";

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

    // Método para leer todos los IDs que alguna vez existieron
    public List<String> obtenerIdsHistoricos() {
        List<String> ids = new ArrayList<>();
        File file = new File(ARCHIVO_HISTORICOS);
        if (!file.exists()) {
            return ids;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    ids.add(linea.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de IDs históricos. ", e);
        }
        return ids;
    }

    // Método para agregar un ID al archivo histórico (usando append = true)
    private void guardarIdHistorico(String id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_HISTORICOS, true))) {
            bw.write(id);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el ID histórico. ", e);
        }
    }

    @Override
    public void agregar(MedicoDTO medico) {
        List<MedicoDTO> lista = obtenerRegistros();
        lista.add(medico);
        guardarTodos(lista);

        // Cada vez que se crea un medico, registramos su ID en el histórico
        guardarIdHistorico(medico.getId());
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