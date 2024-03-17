package com.hospital.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

	@Query(value = "SELECT * from doctor where doctor_id not in (select DISTINCT doctor_id from availability)", nativeQuery = true)
	public List<Doctor> getDoctorswithNoAvailibility();

	@Query(value = "SELECT * from doctor where doctor_id in (select DISTINCT doctor_id from availability)", nativeQuery = true)
	public List<Doctor> getDoctorsWithAvailability();

	@Query(value = "select d.* from doctor d inner join availability a on d.doctor_id = a.doctor_id where d.doctor_name = ?1 and d.doctor_id = a.doctor_id and a.open_status =1", nativeQuery = true)
	public List<Doctor> findByDoctorName(String doctorName);

	@Query(value = "select * from doctor d inner join availability a on d.doctor_id = a.doctor_id where a.day in (?1) and a.open_status = 1", nativeQuery = true)
	public List<Doctor> findByWeekdaysIn(List<String> weekdays);

	@Query(value = "select * from doctor d inner join availability a on d.doctor_id = a.doctor_id where a.open_status = 1 and a.start_time < ?1 and a.end_time > ?2", nativeQuery = true)
	public List<Doctor> findByAvailabilityStartTimeAndEndTime(String startTime, String endTime);

	
	
}
