package HHTEC.history.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HHTEC.history.database.model.History;
import HHTEC.history.database.model.State;
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Optional<History> findByState(State state);
    
}
