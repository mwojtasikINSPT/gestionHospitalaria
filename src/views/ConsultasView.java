package views;

import controllers.ConsultasController;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;

public class ConsultasView {

    private final ConsultasController consultasController;
    private final Scanner scanner;

    public ConsultasView(Scanner scanner) {
        this.consultasController = new ConsultasController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- MENÚ DE CONSULTAS ---\n"
                    + "1. Ver pacientes con su cama asignada\n"
                    + "2. Ver pacientes atendidos por un médico\n"
                    + "3. Ver médico asignado a un paciente\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    consultarPacientesConCama();
                    break;
                case 2:
                    consultarPacientesPorMedico();
                    break;
                case 3:
                    consultarMedicoDePaciente();
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

    private void consultarPacientesConCama() {
        Mostrar.Titulo("Pacientes y sus Camas");
        List<String> resultados = consultasController.obtenerPacientesConCama();

        if (resultados.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
        } else {
            for (String linea : resultados) {
                mostrarTexto(linea);
            }
        }
    }

    private void consultarPacientesPorMedico() {
        Mostrar.Titulo("Pacientes por Médico");
        System.out.print(Mensajes.PEDIR_DATO + "ID del médico: ");
        String idMedico = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            List<String> resultados = consultasController.obtenerPacientesPorMedico(idMedico);
            for (String linea : resultados) {
                mostrarTexto(linea);
            }
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void consultarMedicoDePaciente() {
        Mostrar.Titulo("Médico Asignado a Paciente");
        System.out.print(Mensajes.PEDIR_DATO + "ID del paciente: ");
        String idPaciente = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            String resultado = consultasController.obtenerMedicoDePaciente(idPaciente);
            mostrarTexto(resultado);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}