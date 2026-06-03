package com.tecsup.petclinic.services;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;

import java.util.List;

public interface SpecialtyService {
    /**
     * Create a new specialty.
     * @throws InvalidScheduleException if hOpen >= hClose
     */
    SpecialtyDTO create(SpecialtyDTO dto) throws InvalidScheduleException;

    /**
     * Update an existing specialty.
     * @throws InvalidScheduleException if hOpen >= hClose
     */
    SpecialtyDTO update(SpecialtyDTO dto) throws InvalidScheduleException;

    /**
     * Delete a specialty by id.
     */
    void delete(Integer id) throws SpecialtyNotFoundException;

    /**
     * Find a specialty by id.
     */
    SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException;

    /**
     * Find specialties by name.
     */
    List<SpecialtyDTO> findByName(String name);

    /**
     * Find specialties by office.
     */
    List<SpecialtyDTO> findByOffice(String office);

    /**
     * Return all specialties as entity list (internal use).
     */
    List<Specialty> findAll();

    /**
     * Return all specialties as DTO list (for REST responses).
     */
    List<SpecialtyDTO> findAllDTO();

}
