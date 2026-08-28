package desafio.magalu.scheduling.api;

import desafio.magalu.scheduling.api.dtos.SchedulingRequestDTO;
import desafio.magalu.scheduling.api.dtos.SchedulingResponseDTO;
import desafio.magalu.scheduling.application.SchedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private  final SchedulingService service;

    @GetMapping("/{id}")
    public ResponseEntity<SchedulingResponseDTO> getScheduling(@PathVariable Long id) {
        var schedulingOfId = this.service.findById(id);
        return ResponseEntity.ok(schedulingOfId);
    }

    @PostMapping
    public ResponseEntity<SchedulingResponseDTO> createScheduling(@Valid @RequestBody SchedulingRequestDTO schedulingDTO) {
        var newScheduling = this.service.createScheduling(schedulingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newScheduling);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduling(@PathVariable Long id) {
        this.service.deleteScheduling(id);
        return ResponseEntity.noContent().build();
    }
}
