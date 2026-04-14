package HHTEC.ALMOXARIFADO.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HHTEC.ALMOXARIFADO.database.model.MovimentType;
import HHTEC.ALMOXARIFADO.database.model.Warehouse;
import ch.qos.logback.core.status.Status;


@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse>  findByName(String name); 
    Optional<Warehouse> findByTypeofmoviment(MovimentType typeofmoviment); 

    Optional<Warehouse> findByStatus(Status status); 
}
