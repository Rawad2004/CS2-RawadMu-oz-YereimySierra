package app.application.usecase;

import app.application.port.in.CreateProcedureUseCase;
import app.application.port.in.CreateProcedureCommand;
import app.domain.model.Procedure;
import app.domain.model.vo.Money;
import app.domain.repository.ProcedureRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateProcedureService implements CreateProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public CreateProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public Procedure createProcedure(CreateProcedureCommand command) {
        if (procedureRepository.findByName(command.name()).isPresent()) {
            throw new IllegalStateException("Procedure with name already exists: " + command.name());
        }

        Procedure newProcedure = new Procedure(command.name(), new Money(command.cost()));
        return procedureRepository.save(newProcedure);
    }
}
