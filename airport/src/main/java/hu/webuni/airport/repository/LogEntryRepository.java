package hu.webuni.airport.repository;

import hu.webuni.airport.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {


}
