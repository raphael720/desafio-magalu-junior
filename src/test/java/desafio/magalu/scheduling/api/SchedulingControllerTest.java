package desafio.magalu.scheduling.api;

import desafio.magalu.scheduling.api.dtos.SchedulingRequestDTO;
import desafio.magalu.scheduling.api.dtos.SchedulingResponseDTO;
import desafio.magalu.scheduling.application.SchedulingService;
import desafio.magalu.scheduling.domain.enums.SchedulingStatus;
import desafio.magalu.scheduling.domain.enums.SchedulingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Os testes no Controller são um pouco diferentes dos testes unitários do service,
// já que eu tenho chamadas da api

@WebMvcTest(SchedulingController.class)
class SchedulingControllerTest {

    // Perimite simular requisições GET, POST e DELETE
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SchedulingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a scheduling")
    void postShouldCreateScheduling() throws Exception {
        var requestDTO = new SchedulingRequestDTO(
                "Teste de agendamento",
                "cliente@example.com",
                SchedulingType.EMAIL,
                LocalDateTime.now().plusDays(1)
        );

        var responseDTO = new SchedulingResponseDTO(
                1L,
                requestDTO.destination(),
                requestDTO.message(),
                requestDTO.type(),
                SchedulingStatus.SCHEDULED,
                requestDTO.sendingDate()
        );

        Mockito.when(service.createScheduling(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/scheduling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.message").value("Teste de agendamento"))
        .andExpect(jsonPath("$.destination").value("cliente@example.com"))
        .andExpect(jsonPath("$.type").value("EMAIL"))
        .andExpect(jsonPath("$.status").value("SCHEDULED"));

        Mockito.verify(service, Mockito.times(1))
                .createScheduling(Mockito.any(SchedulingRequestDTO.class));
    }

    @Test
    @DisplayName("Should find a scheduling by id correctly")
    void getSchedulingByIdCorrectly() throws Exception{

        var responseDTO = new SchedulingResponseDTO(
                1L,
                "cliente@example.com",
                "Teste de agendamento",
                SchedulingType.EMAIL,
                SchedulingStatus.SCHEDULED,
                LocalDateTime.now().plusDays(1)
        );

        Mockito.when(service.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/scheduling/1"))
                .andExpect(status().isOk()
        )
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.message").value("Teste de agendamento"))
        .andExpect(jsonPath("$.destination").value("cliente@example.com"));

        Mockito.verify(service, Mockito.times(1)).findById(1L);
    }

    @Test
    void deleteSchedulingCorrectly() throws Exception{
        Mockito.doNothing().when(service).deleteScheduling(1L);

        mockMvc.perform(delete("/api/scheduling/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteScheduling(1L);
    }

    // Testes validation
    @Test
    @DisplayName("Should not create a scheduling with a blank message")
    void shouldNotCreateSchedulingWithBlankMessage() throws Exception{
        var requestDTO = new SchedulingRequestDTO(
                "",
                "cliente@example.com",
                SchedulingType.EMAIL,
                LocalDateTime.now().plusDays(1)
        );

        mockMvc.perform(post("/api/scheduling")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        )
        .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void shouldNotCreateSchedulingWithBlankDestination() throws Exception {
        var requestDTO = new SchedulingRequestDTO(
                "Teste de agendamento",
                "",
                SchedulingType.EMAIL,
                LocalDateTime.now().plusDays(1)
        );

        mockMvc.perform(post("/api/scheduling")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void shouldNotCreateSchedulingWithNullType() throws Exception {
        var requestDTO = new SchedulingRequestDTO(
                "Teste de agendamento",
                "cliente@example.com",
                null,
                LocalDateTime.now().plusDays(1)
        );

        mockMvc.perform(post("/api/scheduling")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void shouldNotCreateSchedulingWithPastData() throws Exception {
        var requestDTO = new SchedulingRequestDTO(
                "Teste de agendamento",
                "cliente@example.com",
                SchedulingType.EMAIL,
                LocalDateTime.now().minusDays(1)
        );

        mockMvc.perform(post("/api/scheduling")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }
}