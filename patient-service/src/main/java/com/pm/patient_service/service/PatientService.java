package com.pm.patient_service.service;

import com.pm.patient_service.Exception.EmailAlreadyExistsException;
import com.pm.patient_service.Exception.GlobalExceptionHandler;
import com.pm.patient_service.Exception.PatientNotFoundException;
import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatient(){

        List<Patient> patientList = patientRepository.findAll();
        List<PatientResponseDTO> responseDTOS = patientList.stream().map(PatientMapper::mapToResponse).toList();
        LOGGER.info("Retrieved {} patients from database",patientList.size());
        return responseDTOS;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(patientRepository.emailExists(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with same email address : "+patientRequestDTO.getEmail()+" already exists");
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        LOGGER.info("Successful in creating a new patient with email : {}",patient.getEmail());

        // an email address must be unique
        return PatientMapper.mapToResponse(patient);
    }

    public PatientResponseDTO updatePatient(UUID id,PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No patient found with the mentioned UUID"));

        if(patientRepository.emailExists(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with same email address : "+patientRequestDTO.getEmail()+" already exists");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));

        Patient updatedPatient = patientRepository.save(patient);
        LOGGER.info("Successful in updating patient entity");

        return PatientMapper.mapToResponse(updatedPatient);
    }

    public void deletePatientById(UUID id){
        patientRepository.deleteById(id);
        LOGGER.info("Successful in deletion of patient with ID : {}",id);
    }
}
