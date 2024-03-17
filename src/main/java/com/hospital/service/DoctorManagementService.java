package com.hospital.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hospital.dto.DoctorAvailableDTO;
import com.hospital.entity.Availability;
import com.hospital.entity.Doctor;
import com.hospital.repository.AvailabilityRepository;
import com.hospital.repository.DoctorRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.Valid;

@Service
public class DoctorManagementService {

	@Autowired
	AvailabilityRepository availabilityRepository;

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	private EntityManager entityManager;

	private Map<String, String> getValidationErrors(BindingResult bindingResult) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : bindingResult.getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return errors;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(ex.getBindingResult()));
	}

	public List<Doctor> getDoctorswithNoAvailibility() {
		return doctorRepository.getDoctorswithNoAvailibility();
	}

	public List<Doctor> getAllDoctorswithAvailability() {
		return doctorRepository.getDoctorsWithAvailability();
	}

	public ResponseEntity<Object> saveAailability(int doctorId, List<Availability> availabilities,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(bindingResult));
		}

		availabilities.stream().forEach(i -> {
			i.setDoctorId(doctorId);
			i.setOpenStatus(1);
		});

		List<Integer> doctors = doctorRepository.findAll().stream().map(i -> i.getDoctorId())
				.collect(Collectors.toList());
		if (!doctors.contains(doctorId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("DoctorUnavailable", "Doctor not available"));
		}
		List<Availability> finalAvailability = new ArrayList<>();
		for (int i = 1; i <= 7; i++) {
			final int day = i;
			Availability existingObject = availabilities.stream().filter(obj -> obj.getDay() == day).findFirst()
					.orElse(null);

			if (existingObject != null) {
				LocalTime startTime = existingObject.getStartTime();
				LocalTime endTime = existingObject.getEndTime();
				if (startTime.compareTo(endTime) > 0) {
					if (startTime.compareTo(endTime) > 0) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(Map.of("TimeError", "Start time cannot be greater than End time"));
					}
				}
				finalAvailability.add(existingObject);
			} else {
				finalAvailability.add(new Availability(doctorId, day, 0, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT));
			}
		}

		availabilityRepository.saveAll(finalAvailability);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("Success", "Added Succesfully"));
	}

	public ResponseEntity<Object> updateAvailability(int doctorId, @Valid List<Availability> availabilities,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(bindingResult));
		}
		List<Integer> doctors = doctorRepository.findAll().stream().map(i -> i.getDoctorId())
				.collect(Collectors.toList());
		if (!doctors.contains(doctorId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("DoctorUnavailable", "Doctor not available"));
		}
		List<Availability> existingAvailabilities = availabilityRepository.getAvailabilities(doctorId);

		for (Availability availability : existingAvailabilities) {

			availability.setStartTime(LocalTime.MIDNIGHT); // 00:00:00
			availability.setEndTime(LocalTime.MIDNIGHT); // 00:00:00
			availability.setOpenStatus(0);
		}

		for (Availability updateRequest : availabilities) {
			int dayToUpdate = updateRequest.getDay();
			LocalTime startTime = updateRequest.getStartTime();
			LocalTime endTime = updateRequest.getEndTime();
			if (startTime.compareTo(endTime) > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("TimeError", "Start time cannot be greater than End time"));
			}

			for (Availability availability : existingAvailabilities) {
				if (availability.getDay() == dayToUpdate) {
					availability.setStartTime(startTime);
					availability.setEndTime(endTime);
					availability.setOpenStatus(1);
					break;
				}
			}
		}

		availabilityRepository.saveAll(existingAvailabilities);

		return ResponseEntity.status(HttpStatus.OK).body(Map.of("Success", "Updated succesfully"));
	}

	public ResponseEntity<Object> deleteAvailability(int doctorId) {
		availabilityRepository.deleteByDoctorId(doctorId);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("Success", "Deleted Succesfully"));

	}

	public List<DoctorAvailableDTO> searchDoctor(String doctorName, List<String> weekdays, String startTime,
			String endTime) {

		StringBuilder sb = new StringBuilder();

		sb.append(
				"select  d, a.timeId from Doctor d inner join Availability a on d.doctorId = a.doctorId where a.openStatus = 1");

		if (doctorName != null && !doctorName.isEmpty()) {
			sb.append("AND d.doctorName like :doctorName ");
		}

		if (weekdays != null && !weekdays.isEmpty()) {
			sb.append("AND a.day IN :days ");
		}

		if (startTime != null && endTime != null) {
			sb.append("AND a.startTime < :startTime  AND a.endTime > :endTime ");
		}

		System.out.println("Query is " + sb.toString());

		Query query = entityManager.createQuery(sb.toString());

		if (doctorName != null && !doctorName.isEmpty()) {
			String name = "%" + doctorName + "%";
			query.setParameter("doctorName",name);
		}

		if (weekdays != null && !weekdays.isEmpty()) {
			query.setParameter("days", weekdays);
		}

		if (startTime != null && endTime != null) {

			query.setParameter("startTime", startTime);
			query.setParameter("endTime", endTime);
		}

		List<Object[]> docs = query.getResultList();
		List<DoctorAvailableDTO> doctorsAvailable = new ArrayList<>();
		for (Object[] i : docs) {
			doctorsAvailable.add(new DoctorAvailableDTO((Doctor) i[0], (int) i[1]));
		}

		return doctorsAvailable;

	}

}
