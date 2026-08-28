package desafio.magalu.scheduling.application;

import desafio.magalu.scheduling.api.dtos.SchedulingRequestDTO;
import desafio.magalu.scheduling.api.dtos.SchedulingResponseDTO;
import desafio.magalu.scheduling.domain.Scheduling;
import desafio.magalu.scheduling.domain.enums.SchedulingStatus;
import desafio.magalu.scheduling.infrastructure.SchedulingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final SchedulingRepository repository;

    public SchedulingResponseDTO createScheduling(SchedulingRequestDTO schedulingDTO) {
        Scheduling newScheduling = new Scheduling(
                schedulingDTO.message(),
                schedulingDTO.destination(),
                schedulingDTO.type(),
                schedulingDTO.sendingDate()
        );

        newScheduling.setStatus(SchedulingStatus.SCHEDULED);

        var entity = this.repository.save(newScheduling);

        return this.toResponse(entity);
    }

    public SchedulingResponseDTO findById(Long id) {
        var scheduled = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Scheduling not found"));

        return this.toResponse(scheduled);
    }

    public void deleteScheduling(Long id) {
        if (!this.repository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found");
        }
        this.repository.deleteById(id);
    }

    private SchedulingResponseDTO toResponse(Scheduling entity) {
        return new SchedulingResponseDTO(
                entity.getId(),
                entity.getDestination(),
                entity.getMessage(),
                entity.getType(),
                entity.getStatus(),
                entity.getSendingDate()
        );
    }

}
