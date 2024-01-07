package com.example.crud.service;

import com.example.crud.entity.Doctor;
import com.example.crud.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public String[] fetchAllDoctors() {
        final String[] res = {""};

        List<Doctor> allDoctors = doctorRepository.findAll();
        allDoctors.forEach(doctor -> {

            res[0] +=  String.format("""
                <tr>
                    <td> %s </td>
                    <td> %s </td>
                    <td> %s </td>
                    <td> %s </td>
                    <td><center>
                        <button onclick="showWindow('edit', %s)"><img src='edit.png' alt='Edit'></button>
                        <button hx-confirm="Are you sure you wish to delete this doctor ?" hx-delete="/api/docteur/%s"><img src='delete.png' alt='Delete'></button>
                    </center></td>
                </tr>""", doctor.getDoctorId(), doctor.getDoctorCin(), doctor.getDoctorName(), doctor.getDoctorSpecialty(), doctor.getDoctorId(), doctor.getDoctorId());
        });
        return res;
    }

    @Override
    public Doctor getDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            return doctor.get();
        }
        return null;
    }

    @Override
    public Doctor updateDoctorById(Long id, Doctor doctor) {
        Optional<Doctor> doctor1 = doctorRepository.findById(id);

        if (doctor1.isPresent()) {
            Doctor originalDoctor = doctor1.get();

            if (Objects.nonNull(doctor.getDoctorName()) && !"".equalsIgnoreCase(doctor.getDoctorName())) {
                originalDoctor.setDoctorName(doctor.getDoctorName());
            }
            if (Objects.nonNull(doctor.getDoctorCin()) && doctor.getDoctorCin() != 0) {
                originalDoctor.setDoctorCin(doctor.getDoctorCin());
            }
            return doctorRepository.save(originalDoctor);
        }
        return null;
    }

    @Override
    public String deleteDepartmentById(Long id) {
        if (doctorRepository.findById(id).isPresent()) {
            doctorRepository.deleteById(id);
            return "<img src='delete.png' alt='Delete'>";
        }
        return "No such doctor in the database";
    }
}
