package desafio.magalu.scheduling.api.dtos;

import desafio.magalu.scheduling.domain.enums.SchedulingStatus;
import desafio.magalu.scheduling.domain.enums.SchedulingType;

import java.time.LocalDateTime;

public record SchedulingResponseDTO(
        Long id,
        String destination,
        String message,
        SchedulingType type,
        SchedulingStatus status,
        LocalDateTime sendingDate
) {
}
