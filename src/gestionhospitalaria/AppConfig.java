package gestionhospitalaria;

import views.CamaView;
import views.MedicoView;
import views.PacienteView;
import views.ReservaView;
import views.AsignacionView;
import utils.Mensajes;
import utils.Mostrar;
import java.util.Scanner;
import views.ConsultasView;

public class AppConfig {

    public static void iniciarSistema() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        PacienteView pacienteView = new PacienteView(scanner);
        MedicoView medicoView = new MedicoView(scanner);
        CamaView camaView = new CamaView(scanner);
        ReservaView reservaView = new ReservaView(scanner);
        AsignacionView asignacionView = new AsignacionView(scanner);
        ConsultasView consultasView = new ConsultasView(scanner);

        do {
            String menuPrincipal = """
                                   
                                   --- SISTEMA DE GESTION HOSPITALARIA ---
                                   1. Gestión de Pacientes
                                   2. Gestión de Médicos
                                   3. Gestión de Camas
                                   4. Gestión de Reservas
                                   5. Gestión de Asignaciones (Médico - Paciente)
                                   6. Búsquedas
                                   0. Salir""";

            opcion = Mostrar.Menu(menuPrincipal, scanner);

            switch (opcion) {
                case 1:
                    pacienteView.mostrarMenu();
                    break;
                case 2:
                    medicoView.mostrarMenu();
                    break;
                case 3:
                    camaView.mostrarMenu();
                    break;
                case 4:
                    reservaView.mostrarMenu();
                    break;
                case 5:
                    asignacionView.mostrarMenu();
                    break;
                case 6:
                    consultasView.mostrarMenu();
                    break;
                case 0:
                    Mostrar.Mensaje(Mensajes.SALIENDO);
                    break;
                case -1:
                default:
                    Mostrar.Mensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);

        scanner.close();
    }
}