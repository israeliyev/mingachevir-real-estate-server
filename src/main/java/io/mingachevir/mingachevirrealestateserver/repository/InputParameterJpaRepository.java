package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.model.entity.HouseRequest;
import io.mingachevir.mingachevirrealestateserver.model.entity.InputParameterValue;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InputParameterJpaRepository extends JpaRepository<InputParameterValue, Long> {

    @Modifying
    @Query("DELETE FROM InputParameterValue hi WHERE hi.house = :house")
    void deleteByHouse(@Param("house") House house);

    @Modifying
    @Query("DELETE FROM InputParameterValue ip WHERE ip.houseRequest = :houseRequest")
    void deleteByHouseRequest(@Param("houseRequest") HouseRequest houseRequest);
}
