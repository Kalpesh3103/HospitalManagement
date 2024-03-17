package com.hospital.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.DoctorAvailableDTO;
import com.hospital.entity.Availability;
import com.hospital.entity.Doctor;
import com.hospital.service.DoctorManagementService;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/home")
public class DoctorManagementController {

	@Autowired
	DoctorManagementService doctorManagementService;

	@GetMapping("/addAvailability")
	public List<Doctor> addAvailability() {

		return doctorManagementService.getDoctorswithNoAvailibility();

	}

	@PostMapping("/addAvailability/{doctorId}")
	public ResponseEntity<Object> saveAvailiblity(@PathVariable("doctorId") int doctorId,
			@Valid @RequestBody List<Availability> availabilities, BindingResult bindingResult) {
		List<Integer> doctorsWithAvailability =  doctorManagementService.getAllDoctorswithAvailability().stream().map(i -> i.getDoctorId()).collect(Collectors.toList());
		if(doctorsWithAvailability.contains(doctorId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("DoctorAvailable", "Doctor already available.."));
		}
		return doctorManagementService.saveAailability(doctorId, availabilities, bindingResult);
	}

	@GetMapping("/editAvailability")
	public List<Doctor> editAvailability() {
		return doctorManagementService.getAllDoctorswithAvailability();
	}

	@PostMapping("/editAvailability/{doctorId}")
	public ResponseEntity<Object> updateAvailability(@PathVariable("doctorId") int doctorId,
			@Valid @RequestBody List<Availability> availabilities, BindingResult bindingResult) {
		List<Integer> doctorsWithNoAvailability = doctorManagementService.getDoctorswithNoAvailibility().stream().map(i -> i.getDoctorId()).collect(Collectors.toList());
		if(doctorsWithNoAvailability.contains(doctorId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("DoctorUnavailable", "Please add availability first.."));
		}
		return doctorManagementService.updateAvailability(doctorId, availabilities, bindingResult);
	}

	@GetMapping("/deleteAvailability/{doctorId}")
	public ResponseEntity<Object> deleteAvailability(@PathVariable("doctorId") int doctorId) {

		return doctorManagementService.deleteAvailability(doctorId);
	}

	@GetMapping("/searchDoctor")
	public ResponseEntity<List<DoctorAvailableDTO>> searchDoctor(
			@RequestParam(name = "doctorName", required = false) String doctorName,
			@RequestParam(name = "weekday", required = false) List<String> weekdays,
			@RequestParam(name = "startTime", required = false) String startTime,
			@RequestParam(name = "endTime", required = false) String endTime) {

		List<DoctorAvailableDTO> doctors = doctorManagementService.searchDoctor(doctorName, weekdays, startTime, endTime);
		return new ResponseEntity<>(doctors, HttpStatus.OK);
	}


}
