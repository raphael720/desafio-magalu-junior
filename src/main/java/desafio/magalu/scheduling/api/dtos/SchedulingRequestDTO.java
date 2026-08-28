package desafio.magalu.scheduling.api.dtos;

import desafio.magalu.scheduling.domain.enums.SchedulingType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SchedulingRequestDTO(
        @NotBlank(message = "Message cannot be blank")
        String message,

        @NotBlank(message = "Destination cannot be blank")
        String destination,

        @NotNull(message = "Type is required")
        SchedulingType type,

        @NotNull(message = "Sending date is required")
        @Future(message = "It cannot be a date in the past.")
        LocalDateTime sendingDate
) {
}
