package controllers;

import dtos.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultasController {

    private final PacienteController pacienteController;
    //private final CamaController camaController;
    private final ReservaController reservaController;
    private final AsignacionController asignacionController;
    private final MedicoController medicoController;

    public ConsultasController() {
        this.pacienteController = new PacienteController();
        //this.camaController = new CamaController();
        this.reservaController = new ReservaController();
        this.asignacionController = new AsignacionController();
        this.medicoController = new MedicoController();
    }

    // 1. Nombre del paciente con su cama asignada (usando Reservas)
    public List<String> obtenerPacientesConCama() {
        List<ReservaDTO> reservas = reservaController.listarReservas();
        List<PacienteDTO> pacientes = pacienteController.listarPacientes();

        // Transformo cada reserva mapeando su ID de paciente con   
        //el nombre y con el código de cama asignado
        return reservas.stream().map(reserva -> {
            PacienteDTO paciente = pacientes.stream()
                    .filter(p -> p.getId().equalsIgnoreCase(reserva.getIdPaciente()))
                    .findFirst()
                    .orElse(null);

            String nombrePaciente = (paciente != null) ? (paciente.getNombre() + " " + paciente.getApellido()) : "Desconocido";
            return "Paciente: " + nombrePaciente + " | Cama: " + reserva.getCodigoCama();
        }).collect(Collectors.toList());
    }

    // 2. Pacientes atendidos por un médico (usando Asignaciones)    
    public List<String> obtenerPacientesPorMedico(String idMedico) {

        List<MedicoDTO> medicos = medicoController.listarMedicos();
        // 1. Validamos de entrada si el médico existe; si no, lanzamos excepción
        MedicoDTO medicoBuscado = medicos.stream()
                .filter(m -> m.getId().equalsIgnoreCase(idMedico))
                .findFirst()
                .orElse(null);

        if (medicoBuscado == null) {
            throw new IllegalArgumentException("No se encontró ningún médico con el ID: " + idMedico);
        }

        List<AsignacionDTO> asignaciones = asignacionController.listarAsignaciones();
        List<PacienteDTO> pacientes = pacienteController.listarPacientes();
        // 2. Armamos la lista con el encabezado del médico y sus pacientes
        List<String> resultado = new ArrayList<>();
        resultado.add("--- Médico: " + medicoBuscado.getNombre() + " " + medicoBuscado.getApellido()
                + " | Especialidad: " + medicoBuscado.getEspecialidad() + " ---");

        // Filtro las asignaciones por el ID del médico y mapeo cada una buscando 
        //el nombre y apellido del paciente correspondiente
        List<String> pacientesDelMedico = asignaciones.stream()
                .filter(asignacion -> asignacion.getIdMedico().equalsIgnoreCase(idMedico))
                .map(asignacion -> {
                    PacienteDTO paciente = pacientes.stream()
                            .filter(p -> p.getId().equalsIgnoreCase(asignacion.getIdPaciente()))
                            .findFirst()
                            .orElse(null);

                    String nombrePaciente = (paciente != null) ? (paciente.getNombre() + " " + paciente.getApellido()) : "Desconocido";
                    return "Paciente: " + nombrePaciente + " (ID: " + asignacion.getIdPaciente() + ")";
                })
                .collect(Collectors.toList());

        if (pacientesDelMedico.isEmpty()) {
            resultado.add("  (Este médico aún no tiene pacientes asignados)");
        } else {
            resultado.addAll(pacientesDelMedico);
        }
        return resultado;
    }

    // 3. Médico asignado a un paciente y datos del paciente (Usando Asignaciones, Paciente y Medico)
    public String obtenerMedicoDePaciente(String idPaciente) {
        // 1. Buscamos los datos del paciente
        PacienteDTO paciente = pacienteController.listarPacientes().stream()
                .filter(p -> p.getId().equalsIgnoreCase(idPaciente))
                .findFirst()
                .orElse(null);

        if (paciente == null) {
            throw new IllegalArgumentException("No se encontró ningún paciente con el ID: " + idPaciente);
        }

        // 2. Busco la asignación que coincida con el ID del paciente
        AsignacionDTO asignacion = asignacionController.listarAsignaciones().stream()
                .filter(a -> a.getIdPaciente().equalsIgnoreCase(idPaciente))
                .findFirst()
                .orElse(null);

        if (asignacion == null) {
            return "Paciente: " + paciente.getNombre() + " " + paciente.getApellido() + " (ID: " + paciente.getId() + ") | No tiene ningún médico asignado.";
        }

        // 3. Con el ID del médico que está en la asignación, busco sus datos
        MedicoDTO medico = medicoController.listarMedicos().stream()
                .filter(m -> m.getId().equalsIgnoreCase(asignacion.getIdMedico()))
                .findFirst()
                .orElse(null);

        String infoMedico = (medico != null)
                ? ("Médico: " + medico.getNombre() + " " + medico.getApellido() + " | Especialidad: " + medico.getEspecialidad())
                : ("Médico asignado (ID: " + asignacion.getIdMedico() + "), pero no se encontraron sus datos en el sistema.");

        // 4. Retorno el texto formateado con la información de ambos
        return "Paciente: " + paciente.getNombre() + " " + paciente.getApellido() + " (ID: " + paciente.getId() + ") --> " + infoMedico;
    }
}
