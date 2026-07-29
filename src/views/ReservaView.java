package views;

import controllers.ReservaController;
import dtos.ReservaDTO;
import utils.Mensajes;
import utils.Mostrar;
import java.util.List;
import java.util.Scanner;

public class ReservaView {
    private final ReservaController reservaController;
    private final Scanner scanner;

    public ReservaView(Scanner scanner) {
        this.reservaController = new ReservaController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE RESERVAS ---\n" +
                    "1. Reservar Cama\n" +
                    "2. Listar Reservas\n" +
                    "3. Cancelar Reserva\n" +
                    "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    reservar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    cancelar();
                    break;
                case 0:
                    mostrarTexto(Mensajes.VOLVIENDO);
                    break;
                case -1:
                default:
                    mostrarTexto(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void mostrarTexto(String texto) {
        System.out.println("\n---> " + texto);
    }

    private void reservar() {
        Mostrar.Titulo("Reservar Cama");

        mostrarTexto(Mensajes.PEDIR_DATO + "Código de la cama: ");
        String codigoCama = scanner.nextLine();

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del paciente: ");
        String idPaciente = scanner.nextLine();

        try {
            reservaController.reservarCama(codigoCama, idPaciente);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Reservas");
        List<ReservaDTO> reservas = reservaController.listarReservas();
        
        if (reservas.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (ReservaDTO r : reservas) {
            mostrarTexto("Código Cama: " + r.getCodigoCama() + " | ID Paciente: " + r.getIdPaciente());
        }
    }

    private void cancelar() {
        Mostrar.Titulo("Cancelar Reserva");
        mostrarTexto(Mensajes.PEDIR_DATO + "Código de la cama de la reserva a cancelar: ");
        String codigoCama = scanner.nextLine();

        try {
            reservaController.cancelarReserva(codigoCama);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}