package com.hospital.entity;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Availability {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int timeId;

	private int doctorId;

	@NotNull(message = "Day cannot be null")
	private Integer day;

	private int openStatus;

	@NotNull(message = "Start time cannot be null")
	private LocalTime startTime;

	@NotNull(message = "End time cannot be null")
	private LocalTime endTime;

	public int getTimeId() {
		return timeId;
	}

	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int i) {
		this.openStatus = i;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public Availability() {
		super();
	}

	public Availability(int doctorId, @NotNull(message = "Day cannot be null") Integer day, int openStatus,
			@NotNull(message = "Start time cannot be null") LocalTime startTime,
			@NotNull(message = "End time cannot be null") LocalTime endTime) {
		super();
		this.doctorId = doctorId;
		this.day = day;
		this.openStatus = openStatus;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Availability(int timeId, int doctorId, @NotNull(message = "Day cannot be null") Integer day, int openStatus,
			@NotNull(message = "Start time cannot be null") LocalTime startTime,
			@NotNull(message = "End time cannot be null") LocalTime endTime) {
		super();
		this.timeId = timeId;
		this.doctorId = doctorId;
		this.day = day;
		this.openStatus = openStatus;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Availability [timeId=" + timeId + ", doctorId=" + doctorId + ", day=" + day + ", openStatus="
				+ openStatus + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
