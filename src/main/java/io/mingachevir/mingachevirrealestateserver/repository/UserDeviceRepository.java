package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    List<UserDevice> findByUsername(String username);
    Optional<UserDevice> findByToken(String token);
}
