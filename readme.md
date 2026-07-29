# 🏥 Sistema de Gestión Hospitalaria

> Proyecto desarrollado para la materia Programacion II. Permite la administración de pacientes, médicos, camas y asignaciones mediante persistencia en archivos planos y arquitectura por capas (MVC / DAO).

---

## 📋 Funcionalidades del Sistema

- [x] Gestión de Pacientes (Alta, baja, modificación y listado).
- [x] Gestión de Médicos y Especialidades (Alta, baja, modificación y listado).
- [x] Gestión de Camas, Reservas y Asignaciones.
- [x] **Generación automática de IDs no reciclables** (evitando reutilizar códigos de registros eliminados mediante archivos históricos).
- [x] Búsquedas implementadas: Paciente con su cama asignada. Pacientes atendidos por un médico. Médico asignado a un paciente.

---

## 🛠️ Tecnologías Utilizadas
- **Java** (Programación Orientada a Objetos)
- **Arquitectura en Capas:** Vistas, Controladores, DAOs y DTOs.
- **Persistencia:** Archivos de texto plano (`.txt`).

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/tu-usuario/tu-repositorio.git](https://github.com/tu-usuario/tu-repositorio.git)
2. Abrir el proyecto en tu entorno de desarrollo o IDE favorito (NetBeans, IntelliJ IDEA, Eclipse, etc.).
3. Asegurarse de que el proyecto esté configurado con el JDK de Java correspondiente.
4. Ejecutar la clase principal (Main) del sistema para iniciar la interfaz por consola.