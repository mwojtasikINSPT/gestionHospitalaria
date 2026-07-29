package views;

import controllers.AsignacionController;
import dtos.AsignacionDTO;
import utils.Mensajes;
import utils.Mostrar;
import java.util.List;
import java.util.Scanner;

public class AsignacionView {
    private final AsignacionController asignacionController;
    private final Scanner scanner;

    public AsignacionView(Scanner scanner) {
        this.asignacionController = new AsignacionController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE ASIGNACIONES (MÉDICO - PACIENTE) ---\n" +
                    "1. Asignar Médico a Paciente\n" +
                    "2. Listar Asignaciones\n" +
                    "3. Cancelar Asignación\n" +
                    "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    asignar();
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

    private void asignar() {
        Mostrar.Titulo("Asignar Médico a Paciente");

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del médico: ");
        String idMedico = scanner.nextLine();

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del paciente: ");
        String idPaciente = scanner.nextLine();

        try {
            asignacionController.asignarMedicoAPaciente(idMedico, idPaciente);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Asignaciones");
        List<AsignacionDTO> asignaciones = asignacionController.listarAsignaciones();
        
        if (asignaciones.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (AsignacionDTO a : asignaciones) {
            mostrarTexto("ID Médico: " + a.getIdMedico() + " | ID Paciente: " + a.getIdPaciente());
        }
    }

    private void cancelar() {
        Mostrar.Titulo("Cancelar Asignación");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del paciente de la asignación a cancelar: ");
        String idPaciente = scanner.nextLine();

        try {
            asignacionController.cancelarAsignacion(idPaciente);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}