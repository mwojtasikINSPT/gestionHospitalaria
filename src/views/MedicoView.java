package views;

import controllers.MedicoController;
import dtos.MedicoDTO;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MedicoView {

    private final MedicoController medicoController;
    private final Scanner scanner;

    public MedicoView(Scanner scanner) {
        this.medicoController = new MedicoController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE MÉDICOS ---\n"
                    + "1. Agregar Médico\n"
                    + "2. Listar Médicos\n"
                    + "3. Buscar Médico por ID\n"
                    + "4. Modificar Médico\n"
                    + "5. Eliminar Médico\n"
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
        System.out.println("\n---> " + texto);
    }

    private void agregar() {
        Mostrar.Titulo("Agregar Médico");

        List<String> idsExistentes = medicoController.listarMedicos().stream()
                .map(MedicoDTO::getId)
                .collect(Collectors.toList());
        String idGenerado = Validaciones.generarSiguienteId(idsExistentes, "M");
        mostrarTexto("ID asignado automáticamente: " + idGenerado);

        boolean dniRepetido;

        mostrarTexto(Mensajes.PEDIR_DATO + "DNI (8 dígitos): ");
        String dni = scanner.nextLine();
        dniRepetido = medicoController.existeDni(dni);

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

        String especialidad;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Especialidad: ");
            especialidad = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(especialidad)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(especialidad));

        try {
            medicoController.agregarMedico(idGenerado, dni, nombre, apellido, especialidad);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Médicos");
        List<MedicoDTO> medicos = medicoController.listarMedicos();

        if (medicos.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (MedicoDTO m : medicos) {
            mostrarTexto("ID: " + m.getId() + " | DNI: " + m.getDni() + " | " + m.getNombre() + " " + m.getApellido() + " | Especialidad: " + m.getEspecialidad());
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Médico");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del médico: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            MedicoDTO m = medicoController.buscarMedicoPorId(id);
            if (m != null) {
                mostrarTexto("Encontrado -> ID: " + m.getId() + " | DNI: " + m.getDni() + " | " + m.getNombre() + " " + m.getApellido() + " | Especialidad: " + m.getEspecialidad());
            } else {
                Mostrar.ErrorNoEncontrado("Médico", id);
            }
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Médico");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del médico a modificar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        MedicoDTO existente = medicoController.buscarMedicoPorId(id);

        if (existente == null) {
            Mostrar.ErrorNoEncontrado("Médico", id);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_NUEVOS_DATOS);

        boolean dniRepetido;
        mostrarTexto("Nuevo DNI (8 dígitos): ");
        String dni = scanner.nextLine();
        dniRepetido = medicoController.existeDni(dni);
        if (!Validaciones.esDniValido(dni)) {
            mostrarTexto(Mensajes.ERROR_DATO);
        } else if (dniRepetido) {
            mostrarTexto(Mensajes.DATO_DUPLICADO);
            return;
        }

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

        String especialidad;
        do {
            mostrarTexto("Nueva Especialidad: ");
            especialidad = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(especialidad)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(especialidad));

        try {
            medicoController.modificarMedico(id, dni, nombre, apellido, especialidad);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Médico");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del médico a eliminar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            medicoController.eliminarMedico(id);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
