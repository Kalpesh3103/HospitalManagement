package com.hospital.dto;

import java.time.LocalTime;

import com.hospital.entity.Doctor;

public class DoctorAvailableDTO {

	Doctor doctor;
	
	Integer timeId;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public int getTimeId() {
		return timeId;
	}

	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}

	public DoctorAvailableDTO(Doctor doctor, int timeId) {
		super();
		this.doctor = doctor;
		this.timeId = timeId;
	}

	public DoctorAvailableDTO() {
		super();
	}

	@Override
	public String toString() {
		return "DoctorAvailableDTO [doctor=" + doctor + ", timeId=" + timeId + "]";
	}
	

	

}
