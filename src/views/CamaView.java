package views;

import controllers.CamaController;
import dtos.CamaDTO;
import models.Estado;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CamaView {
    private final CamaController camaController;
    private final Scanner scanner;

    public CamaView(Scanner scanner) {
        this.camaController = new CamaController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE CAMAS ---\n" +
                    "1. Agregar Cama\n" +
                    "2. Listar Camas\n" +
                    "3. Buscar Cama por Código\n" +
                    "4. Modificar Cama\n" +
                    "5. Eliminar Cama\n" +
                    "0. Volver al Menú Principal";

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
        Mostrar.Titulo("Agregar Cama");

        List<String> codigosExistentes = camaController.listarCamas().stream()
                .map(CamaDTO::getCodigo)
                .collect(Collectors.toList());
        String codigoGenerado = Validaciones.generarSiguienteId(codigosExistentes, "C");
        mostrarTexto("Código asignado automáticamente: " + codigoGenerado);

        String pisoStr;
        do {
            System.out.print(Mensajes.PEDIR_DATO + "Piso (número positivo): ");
            pisoStr = scanner.nextLine();
            if (!Validaciones.esNumeroPositivo(pisoStr)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esNumeroPositivo(pisoStr));
        int piso = Integer.parseInt(pisoStr.trim());

        Estado estado = null;
        do {
            System.out.print(Mensajes.PEDIR_DATO + "Estado (LIBRE, OCUPADA, RESERVADA): ");
            String estadoStr = scanner.nextLine();
            try {
                estado = Estado.valueOf(estadoStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (estado == null);

        try {
            camaController.agregarCama(codigoGenerado, piso, estado);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Camas");
        List<CamaDTO> camas = camaController.listarCamas();
        
        if (camas.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (CamaDTO c : camas) {
            mostrarTexto("Código: " + c.getCodigo() + " | Piso: " + c.getPiso() + " | Estado: " + c.getEstado());
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Cama");
        System.out.print(Mensajes.PEDIR_DATO + "Código de la cama: ");
        String codigo = scanner.nextLine();

        try {
            CamaDTO c = camaController.buscarCamaPorId(codigo);
            if (c != null) {
                mostrarTexto("Encontrado -> Código: " + c.getCodigo() + " | Piso: " + c.getPiso() + " | Estado: " + c.getEstado());
            } else {
                Mostrar.ErrorNoEncontrado("Cama", codigo);
            }
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Cama");
        System.out.print(Mensajes.PEDIR_DATO + "Código de la cama a modificar: ");
        String codigo = scanner.nextLine();

        CamaDTO existente = camaController.buscarCamaPorId(codigo);
        if (existente == null) {
            Mostrar.ErrorNoEncontrado("Cama", codigo);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_NUEVOS_DATOS);

        String pisoStr;
        do {
            System.out.print("Nuevo Piso: ");
            pisoStr = scanner.nextLine();
            if (!Validaciones.esNumeroPositivo(pisoStr)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esNumeroPositivo(pisoStr));
        int piso = Integer.parseInt(pisoStr.trim());

        Estado estado = null;
        do {
            System.out.print("Nuevo Estado (LIBRE, OCUPADA): ");
            String estadoStr = scanner.nextLine();
            try {
                estado = Estado.valueOf(estadoStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (estado == null);

        try {
            camaController.modificarCama(codigo, piso, estado);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Cama");
        System.out.print(Mensajes.PEDIR_DATO + "Código de la cama a eliminar: ");
        String codigo = scanner.nextLine();

        try {
            camaController.eliminarCama(codigo);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}