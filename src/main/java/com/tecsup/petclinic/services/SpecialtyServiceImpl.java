package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mapper.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper     specialtyMapper;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository,
                                SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper     = specialtyMapper;
    }

    // ─── Validaciones ──────────────────────────────────────────────────────

    private void validateSchedule(SpecialtyDTO dto) throws InvalidScheduleException {

        // Regla 1: h_open debe ser menor que h_close
        if (dto.getHOpen() != null && dto.getHClose() != null
                && dto.getHOpen() >= dto.getHClose()) {
            throw new InvalidScheduleException(
                    "h_open (" + dto.getHOpen() + ") must be less than h_close ("
                            + dto.getHClose() + ")");
        }

        // Regla 2: horas deben estar en rango 0-23
        if (dto.getHOpen()  != null && (dto.getHOpen()  < 0 || dto.getHOpen()  > 23)) {
            throw new InvalidScheduleException(
                    "h_open (" + dto.getHOpen() + ") must be between 0 and 23");
        }
        if (dto.getHClose() != null && (dto.getHClose() < 0 || dto.getHClose() > 23)) {
            throw new InvalidScheduleException(
                    "h_close (" + dto.getHClose() + ") must be between 0 and 23");
        }
    }

    // ─── CRUD ───────────────────────────────────────────────────────────────

    @Override
    public SpecialtyDTO create(SpecialtyDTO dto) throws InvalidScheduleException {
        validateSchedule(dto);
        Specialty saved = specialtyRepository.save(specialtyMapper.mapToEntity(dto));
        return specialtyMapper.mapToDto(saved);
    }

    @Override
    public SpecialtyDTO update(SpecialtyDTO dto) throws InvalidScheduleException {
        validateSchedule(dto);
        Specialty saved = specialtyRepository.save(specialtyMapper.mapToEntity(dto));
        return specialtyMapper.mapToDto(saved);
    }

    @Override
    public void delete(Integer id) throws SpecialtyNotFoundException {
        SpecialtyDTO dto = findById(id);
        specialtyRepository.delete(specialtyMapper.mapToEntity(dto));
    }

    // ─── Queries ────────────────────────────────────────────────────────────

    @Override
    public SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException {
        Optional<Specialty> specialty = specialtyRepository.findById(id);
        if (!specialty.isPresent()) {
            throw new SpecialtyNotFoundException("Specialty not found with id: " + id);
        }
        return specialtyMapper.mapToDto(specialty.get());
    }

    @Override
    public List<SpecialtyDTO> findByName(String name) {
        List<Specialty> list = specialtyRepository.findByName(name);
        list.forEach(s -> log.info("" + s));
        return list.stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecialtyDTO> findByOffice(String office) {
        List<Specialty> list = specialtyRepository.findByOffice(office);
        list.forEach(s -> log.info("" + s));
        return list.stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
    }

    @Override
    public List<SpecialtyDTO> findAllDTO() {
        return specialtyRepository.findAll()
                .stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }
}