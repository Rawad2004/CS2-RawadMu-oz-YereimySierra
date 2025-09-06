package app.application.services;

import app.application.usecases.SupportUseCases.CreateDiagnosticAidUseCase;
import app.application.dto.CreateDiagnosticAidCommand;
import app.domain.model.DiagnosticAid;
import app.domain.model.vo.Money;
import app.domain.repository.DiagnosticAidRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateDiagnosticAidService implements CreateDiagnosticAidUseCase {

    private final DiagnosticAidRepositoryPort diagnosticAidRepository;

    public CreateDiagnosticAidService(DiagnosticAidRepositoryPort diagnosticAidRepository) {
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    @Override
    public DiagnosticAid createDiagnosticAid(CreateDiagnosticAidCommand command) {
        if (diagnosticAidRepository.findByName(command.name()).isPresent()) {
            throw new IllegalStateException("Diagnostic aid with name already exists: " + command.name());
        }

        DiagnosticAid newDiagnosticAid = new DiagnosticAid(command.name(), new Money(command.cost()));
        return diagnosticAidRepository.save(newDiagnosticAid);
    }
}
