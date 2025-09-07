package app.infrastructure.gui;


import app.application.dto.CreateStaffCommand;
import app.application.services.CreateStaffService;


import app.domain.model.Staff;
import app.domain.model.vo.StaffRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class ClinicConsoleMenu implements CommandLineRunner {

    // --- CORRECCIÓN ---
    // Inyectamos la clase de servicio concreta, ya que es la que tienes
    private final CreateStaffService createStaffService;

    public ClinicConsoleMenu(CreateStaffService createStaffService) {
        this.createStaffService = createStaffService;
    }
    // --- FIN DE CORRECCIÓN ---

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menú de Prueba de la Clínica ---");
            System.out.println("1. Registrar Nuevo Empleado (Prueba)");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            String choice = scanner.nextLine();

            if ("1".equals(choice)) {
                registerNewStaff(scanner);
            } else if ("2".equals(choice)) {
                System.out.println("Saliendo de la aplicación...");
                System.exit(0);
                return;
            } else {
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }

    private void registerNewStaff(Scanner scanner) {

        try {
            System.out.println("\n--- Registro de Nuevo Empleado ---");
            System.out.println("Cédula: ");
            String nationalId = "1105672889";
            System.out.println("Nombre Completo: ");
            String fullName = "Rawad";
            System.out.println("Email (ej: test@test.com): ");
            String email = "Mrawadyecid@gmail.com";
            System.out.println("Número de Teléfono (1-10 dígitos): ");
            String phone = "3212739222";
            System.out.println("Fecha de Nacimiento (dd/MM/yyyy): ");
            LocalDate birthDate = LocalDate.parse("11/12/2004", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println("DEBUG [Menu]: La fecha parseada es: " + birthDate);
            System.out.println("Dirección: ");
            String address = "Manzana C Casa 1";
            System.out.println("Nombre de Usuario (letras/números, max 15): ");
            String username = "Rawad1004";
            System.out.println("Contraseña (min 8, 1 mayús, 1 num, 1 especial): ");
            String password = "Rawadmunoz2004@";
            StaffRole role = StaffRole.NURSE;
            System.out.println("Rol asignado por defecto: NURSE");

            CreateStaffCommand command = new CreateStaffCommand(
                    nationalId, fullName, email, phone, birthDate, address, role, username, password
            );

            // --- CORRECCIÓN ---
            // Llamamos al servicio a través de la variable con el nombre corregido
            Staff savedStaff = createStaffService.createStaff(command);

            System.out.println("\n¡ÉXITO! Empleado registrado en la base de datos.");
            System.out.println("ID Asignado: " + savedStaff.getId());
            System.out.println("Nombre: " + savedStaff.getFullName());

        } catch (Exception e) {
            System.err.println("\nERROR AL REGISTRAR: " + e.getMessage());
        }
    }
}