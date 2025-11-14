// File: src/main/java/app/application/services/RecordMedicationAdministrationService.java
package app.application.services;

import app.application.port.in.RecordMedicationAdministrationCommand;
import app.application.usecases.NurseUseCases.RecordMedicationAdministrationUseCase;
import app.domain.model.*;
import app.domain.model.order.MedicationOrderItem;
import app.domain.model.vo.Dose;
import app.domain.model.vo.NationalId;
import app.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RecordMedicationAdministrationService implements RecordMedicationAdministrationUseCase {

    private final PatientRepositoryPort patientRepository;
    private final MedicationRepositoryPort medicationRepository; // ✅ CORREGIDO
    private final OrderRepositoryPort orderRepository;

    public RecordMedicationAdministrationService(PatientRepositoryPort patientRepository,
                                                 MedicationRepositoryPort medicationRepository,
                                                 OrderRepositoryPort orderRepository) {
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Patient recordMedicationAdministration(String patientNationalId, RecordMedicationAdministrationCommand command) {
        // 1. Validar que el paciente existe
        Patient patient = patientRepository.findByNationalId(new NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con cédula: " + patientNationalId));

        // 2. Validar que el medicamento existe
        Medication medication = medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado con ID: " + command.medicationId()));

        // 3. Validar que la hora de administración no sea futura
        if (command.administrationTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La hora de administración no puede ser futura");
        }

        // 4. Validar que existe una orden activa con este medicamento para el paciente
        validateActiveMedicationOrder(patientNationalId, command.medicationId(), command.dose());

        // 5. Crear Value Objects con validaciones
        Dose dose = new Dose(command.dose());

        // 6. Registrar la administración (en una implementación real, crearíamos una entidad MedicationAdministration)
        System.out.println("✅ Administración registrada - Paciente: " + patientNationalId +
                ", Medicamento: " + medication.getName() +
                ", Dosis: " + dose.getValue() +
                ", Hora: " + command.administrationTime());

        return patient;
    }

    private void validateActiveMedicationOrder(String patientNationalId, Long medicationId, String dose) {
        // Buscar todas las órdenes del paciente
        List<Order> patientOrders = orderRepository.findByPatientId(new NationalId(patientNationalId));

        boolean hasActiveOrder = patientOrders.stream()
                .filter(order -> !order.getCreationDate().isBefore(LocalDateTime.now().minusMonths(1).toLocalDate())) // Órdenes de último mes
                .anyMatch(order -> order.getItems().stream()
                        .anyMatch(item -> {
                            if (item instanceof MedicationOrderItem) {
                                MedicationOrderItem medItem = (MedicationOrderItem) item;
                                return medItem.getMedicationId().equals(medicationId) &&
                                        medItem.getDose().equals(dose);
                            }
                            return false;
                        }));

        if (!hasActiveOrder) {
            throw new IllegalStateException(
                    "No existe una orden activa que autorice la administración de este medicamento " +
                            "con la dosis especificada para el paciente: " + patientNationalId
            );
        }
    }
}