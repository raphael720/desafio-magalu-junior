package desafio.magalu.scheduling.domain;

import desafio.magalu.scheduling.domain.enums.SchedulingStatus;
import desafio.magalu.scheduling.domain.enums.SchedulingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Scheduling")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Scheduling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private SchedulingType type;

    @Enumerated(EnumType.STRING)
    private SchedulingStatus status;

    @Column(nullable = false)
    private LocalDateTime SendingDate;
}
