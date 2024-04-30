package com.userfront.Service.UserServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.Dao.AppointmentDao;
import com.userfront.Domain.Appointment;
import com.userfront.Service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentDao appointmentDao;
	
	@Override
	public Appointment createAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		return appointmentDao.save(appointment);
	}

	@Override
	public List<Appointment> findAll() {
		// TODO Auto-generated method stub
		return appointmentDao.findAll();
	}

	@Override
	public Object findAppointment(Long id) {
		// TODO Auto-generated method stub
		return appointmentDao.findById(id);
	}

	@Override
	public void confirmAppointment(Long id) {
		// TODO Auto-generated method stub
		Appointment appointment = (Appointment) findAppointment(id);
		appointment.setConfirmed(true);
		appointmentDao.save(appointment);

	}

}
