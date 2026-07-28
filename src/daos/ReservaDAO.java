package daos;

import dtos.ReservaDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO implements ICrud<ReservaDTO, String> {

    private final String ARCHIVO = "reservas.txt";

    @Override
    public List<ReservaDTO> obtenerRegistros() {
        List<ReservaDTO> reservas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return reservas;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    ReservaDTO reserva = new ReservaDTO(partes[0], partes[1]);
                    reservas.add(reserva);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de reservas. ", e);
        }
        return reservas;
    }

    public void guardarTodas(List<ReservaDTO> reservas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (ReservaDTO reserva : reservas) {
                bw.write(reserva.getCodigoCama() + ";" + 
                         reserva.getIdPaciente());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de reservas ", e);
        }
    }

    @Override
    public void agregar(ReservaDTO reserva) {
        List<ReservaDTO> lista = obtenerRegistros();
        lista.add(reserva);
        guardarTodas(lista);
    }

    @Override
    public ReservaDTO obtenerPorId(String codigoCama) {
        return obtenerRegistros().stream()
                .filter(r -> r.getCodigoCama().equalsIgnoreCase(codigoCama))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificar(ReservaDTO reservaModificada) {
        List<ReservaDTO> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigoCama().equalsIgnoreCase(reservaModificada.getCodigoCama())) {
                lista.set(i, reservaModificada);
                guardarTodas(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(String codigoCama) {
        List<ReservaDTO> lista = obtenerRegistros();
        boolean eliminada = lista.removeIf(r -> r.getCodigoCama().equalsIgnoreCase(codigoCama));

        if (eliminada) {
            guardarTodas(lista);
        }
    }
}