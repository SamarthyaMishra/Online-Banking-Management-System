package com.userfront.Service;

import java.util.List;

import com.userfront.Domain.Appointment;

public interface AppointmentService {

	Appointment createAppointment(Appointment appointment);

	List<Appointment> findAll();

	Object findAppointment(Long id);

	void confirmAppointment(Long id);

}
