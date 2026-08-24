package desafio.magalu.scheduling.infrastructure;

import desafio.magalu.scheduling.domain.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
}
