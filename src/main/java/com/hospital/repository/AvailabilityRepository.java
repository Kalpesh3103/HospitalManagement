package com.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hospital.entity.Availability;

import jakarta.transaction.Transactional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {

	@Query(value = "SELECT * from availability where doctor_id = ?1", nativeQuery =  true)
	List<Availability> getAvailabilities(int doctorId);

    @Modifying
    @Transactional
	@Query(value = "DELETE from availability where doctor_id = ?1", nativeQuery =  true)
	void deleteByDoctorId(int doctorId);

}
