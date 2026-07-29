package views;

import controllers.PacienteController;
import dtos.PacienteDTO;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PacienteView {

    private final PacienteController pacienteController;
    private final Scanner scanner;

    public PacienteView(Scanner scanner) {
        this.pacienteController = new PacienteController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE PACIENTES ---\n"
                    + "1. Agregar Paciente\n"
                    + "2. Listar Pacientes\n"
                    + "3. Buscar Paciente por ID\n"
                    + "4. Modificar Paciente\n"
                    + "5. Eliminar Paciente\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    agregar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    buscarPorId();
                    break;
                case 4:
                    modificar();
                    break;
                case 5:
                    eliminar();
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
        System.out.println("\n--->" + texto);
    }

    private void agregar() {
        Mostrar.Titulo("Agregar Paciente");
        List<String> idsExistentes = pacienteController.listarPacientes().stream()
                .map(PacienteDTO::getId)
                .collect(Collectors.toList());
        String idGenerado = Validaciones.generarSiguienteId(idsExistentes, "P");
        mostrarTexto("ID asignado automáticamente: " + idGenerado);

        boolean dniRepetido;

        mostrarTexto(Mensajes.PEDIR_DATO + "DNI (8 dígitos): ");
        String dni = scanner.nextLine();
        dniRepetido = pacienteController.existeDni(dni);

        if (!Validaciones.esDniValido(dni)) {
            mostrarTexto(Mensajes.ERROR_DATO);
            return;
        } else if (dniRepetido) {
            mostrarTexto(Mensajes.DATO_DUPLICADO);
            return;
        }

        String nombre;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Nombre: ");
            nombre = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(nombre)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Apellido: ");
            apellido = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(apellido)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        try {
            pacienteController.agregarPaciente(idGenerado, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Pacientes");
        List<PacienteDTO> pacientes = pacienteController.listarPacientes();

        if (pacientes.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (PacienteDTO p : pacientes) {
            mostrarTexto("ID: " + p.getId() + " | DNI: " + p.getDni() + " | " + p.getNombre() + " " + p.getApellido());
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Paciente");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del paciente: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            PacienteDTO p = pacienteController.buscarPacientePorId(id);
            if (p != null) {
                mostrarTexto("Encontrado -> ID: " + p.getId() + " | DNI: " + p.getDni() + " | " + p.getNombre() + " " + p.getApellido());
            } else {
                Mostrar.ErrorNoEncontrado("Paciente", id);
            }
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Paciente");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del paciente a modificar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        PacienteDTO existente = pacienteController.buscarPacientePorId(id);
        if (existente == null) {
            Mostrar.ErrorNoEncontrado("Paciente", id);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_NUEVOS_DATOS);

        String dni;
        boolean dniRepetido;

        do {
            mostrarTexto("Nuevo DNI (8 dígitos): ");
            dni = scanner.nextLine();
            dniRepetido = pacienteController.existeDni(dni);
            if (!Validaciones.esDniValido(dni)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            } else if (dniRepetido) {
                mostrarTexto("Ya existe un paciente registrado con ese DNI.");
            }
        } while (!Validaciones.esDniValido(dni));

        String nombre;
        do {
            mostrarTexto("Nuevo Nombre: ");
            nombre = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(nombre)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            mostrarTexto("Nuevo Apellido: ");
            apellido = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(apellido)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        try {
            pacienteController.modificarPaciente(id, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Paciente");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del paciente a eliminar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            pacienteController.eliminarPaciente(id);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
