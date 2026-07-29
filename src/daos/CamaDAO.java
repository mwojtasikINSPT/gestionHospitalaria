package daos;

import dtos.CamaDTO;
import models.Estado;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CamaDAO implements ICrud<CamaDTO, String> {

    private final String ARCHIVO = "camas.txt";
    private final String ARCHIVO_HISTORICOS = "camasIdHistoricos.txt";

    @Override
    public List<CamaDTO> obtenerRegistros() {
        List<CamaDTO> camas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return camas;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3) {
                    CamaDTO cama = new CamaDTO(partes[0], Integer.parseInt(partes[1]), Estado.valueOf(partes[2]));
                    camas.add(cama);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de camas. ", e);
        }
        return camas;
    }

    public void guardarTodas(List<CamaDTO> camas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (CamaDTO cama : camas) {
                bw.write(cama.getCodigo() + "," +
                        cama.getPiso() + "," +
                        cama.getEstado());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de camas ", e);
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
    public void agregar(CamaDTO cama) {
        List<CamaDTO> lista = obtenerRegistros();
        lista.add(cama);
        guardarTodas(lista);
        guardarIdHistorico(cama.getCodigo());
    }

    @Override
    public CamaDTO obtenerPorId(String codigo) {
        return obtenerRegistros().stream()
                .filter(c -> c.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificar(CamaDTO camaModificada) {
        List<CamaDTO> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equalsIgnoreCase(camaModificada.getCodigo())) {
                lista.set(i, camaModificada);
                guardarTodas(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(String codigo) {
        List<CamaDTO> lista = obtenerRegistros();
        boolean eliminado = lista.removeIf(c -> c.getCodigo().equalsIgnoreCase(codigo));

        if (eliminado) {
            guardarTodas(lista);
        }
    }
}