package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.Broker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrokerRepository extends JpaRepository<Broker, Long> {
    Optional<Broker> findFirstByEnabledIsTrue();
}
