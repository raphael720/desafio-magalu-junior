package desafio.magalu.scheduling.application;

import desafio.magalu.scheduling.api.dtos.SchedulingRequestDTO;
import desafio.magalu.scheduling.domain.Scheduling;
import desafio.magalu.scheduling.domain.enums.SchedulingStatus;
import desafio.magalu.scheduling.domain.enums.SchedulingType;
import desafio.magalu.scheduling.infrastructure.SchedulingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private SchedulingRepository repository;

    @InjectMocks
    private SchedulingService service;

    @Test
    @DisplayName("Should created correctly a scheduling")
    void createSchedulingCorrectly() {

        SchedulingRequestDTO newRequest = new SchedulingRequestDTO(
                "Teste de agendamento",
                "cliente@example.com",
                SchedulingType.EMAIL,
                LocalDateTime.parse("2026-08-30T10:00:00")
        );

        Mockito.when(repository.save(Mockito.any(Scheduling.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var createdScheduling = service.createScheduling(newRequest);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Scheduling.class));

        assertNotNull(createdScheduling);
        assertEquals("Teste de agendamento", createdScheduling.message());
        assertEquals("cliente@example.com", createdScheduling.destination());
        assertEquals(SchedulingType.EMAIL, createdScheduling.type());
        assertEquals(SchedulingStatus.SCHEDULED, createdScheduling.status());
        assertEquals(
                LocalDateTime.parse("2026-08-30T10:00:00"),
                createdScheduling.sendingDate()
        );
    }

    @Test
    @DisplayName("Should find correctly a scheduling by id")
    void findSchedulingIdCorrectly() {
        var scheduling = new Scheduling(
                "Teste de agendamento",
                "cliente@example.com",
                SchedulingType.EMAIL,
                LocalDateTime.parse("2026-08-30T10:00:00")
        );
        scheduling.setId(1L);
        scheduling.setStatus(SchedulingStatus.SCHEDULED);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(scheduling));

        var responseService = service.findById(scheduling.getId());

        assertNotNull(responseService);
        assertEquals(1L, responseService.id());
        assertEquals("Teste de agendamento", responseService.message());
        assertEquals("cliente@example.com", responseService.destination());
        assertEquals(SchedulingType.EMAIL, responseService.type());
        assertEquals(SchedulingStatus.SCHEDULED, responseService.status());

        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should not found a scheduling correctly")
    void notFoundSchedulingByIdCorrectly() {
        Mockito.when(repository.findById(2L)).thenReturn(Optional.empty());

        var thrown = assertThrows(EntityNotFoundException.class, () -> {
            service.findById(2L);
        });

        assertEquals("Scheduling not found", thrown.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(2L);
    }

    @Test
    @DisplayName("Should delete a scheduling correctly")
    void deleteSchedulingByIdCorrectly() {
        Mockito.when(repository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(1L);

        service.deleteScheduling(1L);

        Mockito.verify(repository, Mockito.times(1)).existsById(1L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void notDeleteSchedulingByIdCorrectly() {
        Mockito.when(repository.existsById(1L)).thenReturn(false);

        var thrown = assertThrows(EntityNotFoundException.class, () -> {
           service.deleteScheduling(1L);
        });

        assertEquals("Appointment not found", thrown.getMessage());
        Mockito.verify(repository, Mockito.times(1)).existsById(1L);
        Mockito.verify(repository, Mockito.never()).deleteById(1L);
    }
}