package daos;

import dtos.CamaDTO;
import models.Estado;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CamaDAO implements ICrud<CamaDTO, String> {

    private final String ARCHIVO = "camas.txt";

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

    @Override
    public void agregar(CamaDTO cama) {
        List<CamaDTO> lista = obtenerRegistros();
        lista.add(cama);
        guardarTodas(lista);
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