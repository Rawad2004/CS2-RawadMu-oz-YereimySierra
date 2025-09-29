package app.infrastructure.gui;

import app.application.dto.*;
import app.application.usecases.*;
import app.domain.model.ClinicalHistoryEntry;

import app.domain.model.Patient;
import app.domain.model.Staff;
import app.domain.model.vo.Gender;
import app.domain.model.vo.StaffRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class ClinicConsoleMenu implements CommandLineRunner {

    private final HHRRUseCase.CreateStaffUseCase createStaffUseCase;
    private final HHRRUseCase.DeleteStaffUseCase deleteStaffUseCase;

    private final AdministrativeUseCases.CreatePatientUseCase createPatientUseCase;

    private final NurseUseCases.RecordVitalSignsUseCase recordVitalSignsUseCase;

    private final DoctorUseCases.CreateClinicalHistoryEntryUseCase createClinicalHistoryEntryUseCase;
    private final DoctorUseCases.CreateOrderUseCase createOrderUseCase;

    private final SupportUseCases.CreateMedicationUseCase createMedicationUseCase;

    public ClinicConsoleMenu(
            HHRRUseCase.CreateStaffUseCase createStaffUseCase,
            HHRRUseCase.DeleteStaffUseCase deleteStaffUseCase,
            AdministrativeUseCases.CreatePatientUseCase createPatientUseCase,
            NurseUseCases.RecordVitalSignsUseCase recordVitalSignsUseCase,
            DoctorUseCases.CreateClinicalHistoryEntryUseCase createClinicalHistoryEntryUseCase,
            DoctorUseCases.CreateOrderUseCase createOrderUseCase,
            SupportUseCases.CreateMedicationUseCase createMedicationUseCase
    ) {
        this.createStaffUseCase = createStaffUseCase;
        this.deleteStaffUseCase = deleteStaffUseCase;
        this.createPatientUseCase = createPatientUseCase;
        this.recordVitalSignsUseCase = recordVitalSignsUseCase;
        this.createClinicalHistoryEntryUseCase = createClinicalHistoryEntryUseCase;
        this.createOrderUseCase = createOrderUseCase;
        this.createMedicationUseCase = createMedicationUseCase;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Recursos Humanos");
            System.out.println("2. Administrativo");
            System.out.println("3. Enfermería");
            System.out.println("4. Médico");
            System.out.println("5. Soporte");
            System.out.println("6. Salir");
            System.out.print("Seleccione opción: ");
            String opt = scanner.nextLine();
            switch (opt) {
                case "1" -> rrhhMenu(scanner);
                case "2" -> adminMenu(scanner);
                case "3" -> nurseMenu(scanner);
                case "4" -> doctorMenu(scanner);
                case "5" -> supportMenu(scanner);
                case "6" -> { System.out.println("Saliendo..."); return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void rrhhMenu(Scanner scanner) {
        System.out.println("\n--- RRHH ---");
        System.out.println("1. Crear empleado");
        System.out.println("2. Eliminar empleado");
        System.out.print("Seleccione: ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            try {
                System.out.print("Cédula: ");
                String nationalId = scanner.nextLine();

                System.out.print("Nombre completo: ");
                String fullName = scanner.nextLine();

                System.out.print("Correo: ");
                String email = scanner.nextLine();

                System.out.print("Teléfono: ");
                String phone = scanner.nextLine();

                System.out.print("Fecha nacimiento (dd/MM/yyyy): ");
                LocalDate birthDate = LocalDate.parse(
                        scanner.nextLine(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

                System.out.print("Dirección: ");
                String address = scanner.nextLine();

                System.out.print("Rol (HUMAN_RESOURCES,ADMINISTRATIVE_STAFF, INFORMATION_SUPPORT, NURSE,DOCTOR): ");
                StaffRole role = StaffRole.valueOf(scanner.nextLine().toUpperCase());

                System.out.print("Nombre de usuario (máx 15 caracteres): ");
                String username = scanner.nextLine();

                System.out.print("Contraseña: ");
                String password = scanner.nextLine();

                CreateStaffCommand cmd = new CreateStaffCommand(
                        nationalId,
                        fullName,
                        email,
                        phone,
                        birthDate,
                        address,
                        role,
                        username,
                        password
                );

                Staff staff = createStaffUseCase.createStaff(cmd);

                System.out.println("✅ Empleado creado con éxito: " + staff.getFullName());

            } catch (Exception e) {
                System.err.println("❌ Error al crear empleado: " + e.getMessage());
            }

        } else if ("2".equals(choice)) {
            System.out.print("Cédula del empleado a eliminar: ");
            String id = scanner.nextLine();
            try {
                deleteStaffUseCase.deleteStaff(id);
                System.out.println("✅ Empleado eliminado con éxito.");
            } catch (Exception e) {
                System.err.println("❌ Error al eliminar empleado: " + e.getMessage());
            }
        }
    }

    private void adminMenu(Scanner scanner) {
        System.out.println("\n--- Administrativo ---");
        System.out.println("1. Registrar paciente");
        System.out.print("Seleccione: ");
        String choice = scanner.nextLine();
        if ("1".equals(choice)) {
            try {
                System.out.print("Cédula: ");
                String nationalId = scanner.nextLine();

                System.out.print("Nombre completo: ");
                String fullName = scanner.nextLine();

                System.out.print("Fecha nacimiento (dd/MM/yyyy): ");
                LocalDate birthDate = LocalDate.parse(
                        scanner.nextLine(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

                System.out.print("Género (MALE, FEMALE, OTHER): ");
                Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());

                System.out.print("Dirección: ");
                String address = scanner.nextLine();

                System.out.print("Teléfono: ");
                String phone = scanner.nextLine();

                System.out.print("Correo: ");
                String email = scanner.nextLine();

                System.out.println("\n--- Contacto de emergencia ---");
                System.out.print("Nombre completo: ");
                String emergencyName = scanner.nextLine();
                System.out.print("Relación con el paciente: ");
                String relationship = scanner.nextLine();
                System.out.print("Teléfono: ");
                String emergencyPhone = scanner.nextLine();

                CreatePatientCommand.EmergencyContactData emergencyContact =
                        new CreatePatientCommand.EmergencyContactData(emergencyName, relationship, emergencyPhone);

                System.out.println("\n--- Seguro médico ---");
                System.out.print("Nombre compañía: ");
                String companyName = scanner.nextLine();
                System.out.print("Número de póliza: ");
                String policyNumber = scanner.nextLine();
                System.out.print("¿Activo? (true/false): ");
                boolean active = Boolean.parseBoolean(scanner.nextLine());
                System.out.print("Fecha de vigencia (dd/MM/yyyy): ");
                LocalDate expiryDate = LocalDate.parse(
                        scanner.nextLine(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

                CreatePatientCommand.InsurancePolicyData insurancePolicy =
                        new CreatePatientCommand.InsurancePolicyData(companyName, policyNumber, active, expiryDate);

                CreatePatientCommand cmd = new CreatePatientCommand(
                        nationalId,
                        fullName,
                        birthDate,
                        gender,
                        address,
                        phone,
                        email,
                        emergencyContact,
                        insurancePolicy
                );

                createPatientUseCase.createPatient(cmd);

                System.out.println("✅ Paciente registrado con éxito.");

            } catch (Exception e) {
                System.err.println("❌ Error al registrar paciente: " + e.getMessage());
            }
        }
    }


    private void nurseMenu(Scanner scanner) {
        System.out.println("\n--- Enfermería ---");
        System.out.println("1. Registrar signos vitales");
        System.out.print("Seleccione: ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            try {
                System.out.print("Cédula paciente: ");
                String patientNationalId = scanner.nextLine();

                System.out.print("Fecha visita (dd/MM/yyyy): ");
                LocalDate visitDate = LocalDate.parse(
                        scanner.nextLine(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

                System.out.print("Presión arterial (mmHg, ej: 120.0): ");
                Double bp = Double.parseDouble(scanner.nextLine());

                System.out.print("Temperatura (°C, ej: 36.5): ");
                Double temp = Double.parseDouble(scanner.nextLine());

                System.out.print("Pulso (lpm, ej: 80): ");
                Integer pulse = Integer.parseInt(scanner.nextLine());

                System.out.print("Oxígeno (%SpO2, ej: 98.5): ");
                Double ox = Double.parseDouble(scanner.nextLine());

                RecordVitalSignsCommand cmd = new RecordVitalSignsCommand(
                        bp,
                        temp,
                        pulse,
                        ox
                );

                Patient patient = recordVitalSignsUseCase.recordVitalSigns(patientNationalId, cmd);

                System.out.println("✅ Signos vitales registrados para " + patient.getFullName()
                        + " (" + patient.getNationalId().getValue() + ") en la fecha " + visitDate);

            } catch (Exception e) {
                System.err.println("❌ Error al registrar signos vitales: " + e.getMessage());
            }
        }
    }




    private void doctorMenu(Scanner scanner) {
        System.out.println("\n--- Médico ---");
        System.out.println("1. Registrar historia clínica");
        System.out.print("Seleccione: ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            try {
                System.out.print("Cédula paciente: ");
                String patientId = scanner.nextLine();

                System.out.print("Cédula médico: ");
                String doctorId = scanner.nextLine();

                System.out.print("Fecha visita (dd/MM/yyyy): ");
                LocalDate visitDate = LocalDate.parse(
                        scanner.nextLine(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

                System.out.print("Motivo consulta: ");
                String reasonForVisit = scanner.nextLine();

                System.out.print("Sintomatología: ");
                String symptomatology = scanner.nextLine();

                System.out.print("Diagnóstico: ");
                String diagnosis = scanner.nextLine();

                CreateClinicalHistoryEntryCommand cmd = new CreateClinicalHistoryEntryCommand(
                        patientId,
                        doctorId,
                        visitDate,
                        reasonForVisit,
                        symptomatology,
                        diagnosis
                );

                ClinicalHistoryEntry entry = createClinicalHistoryEntryUseCase.createClinicalHistoryEntry(cmd);

                System.out.println("✅ Historia clínica registrada para el paciente con cédula "
                        + entry.getPatientId().getValue()
                        + " en la fecha " + entry.getVisitDate());

            } catch (Exception e) {
                System.err.println("❌ Error al registrar historia clínica: " + e.getMessage());
            }
        }
    }


    private void supportMenu(Scanner scanner) {
        System.out.println("\n--- Soporte ---");
        System.out.println("1. Registrar medicamento");
        System.out.print("Seleccione: ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            try {
                System.out.print("Nombre medicamento: ");
                String name = scanner.nextLine();

                System.out.print("Costo (ej: 15000.50): ");
                BigDecimal cost = new BigDecimal(scanner.nextLine());

                CreateMedicationCommand cmd = new CreateMedicationCommand(name, cost);

                // Usa el método correcto del caso de uso
                createMedicationUseCase.createMedication(cmd);

                System.out.println("✅ Medicamento registrado en inventario.");

            } catch (Exception e) {
                System.err.println("❌ Error al registrar medicamento: " + e.getMessage());
            }
        }
    }

}
