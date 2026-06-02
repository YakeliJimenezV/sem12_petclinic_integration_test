package com.tecsup.petclinic.webs;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    /**
     * GET /specialties — return all specialties
     */
    @GetMapping("/specialties")
    public ResponseEntity<List<Specialty>> findAllSpecialties() {
        List<Specialty> specialties = specialtyService.findAll();
        log.info("specialties: " + specialties);
        return ResponseEntity.ok(specialties);
    }

    /**
     * GET /specialties/{id} — find by id
     */
    @GetMapping("/specialties/{id}")
    ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(specialtyService.findById(id));
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /specialties — create
     */
    @PostMapping("/specialties")
    ResponseEntity<?> create(@RequestBody SpecialtyDTO dto) {
        try {
            SpecialtyDTO created = specialtyService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (InvalidScheduleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PUT /specialties/{id} — update
     */
    @PutMapping("/specialties/{id}")
    ResponseEntity<?> update(@RequestBody SpecialtyDTO dto,
                             @PathVariable Integer id) {
        try {
            SpecialtyDTO existing = specialtyService.findById(id);
            existing.setName(dto.getName());
            existing.setOffice(dto.getOffice());
            existing.setHOpen(dto.getHOpen());
            existing.setHClose(dto.getHClose());
            SpecialtyDTO updated = specialtyService.update(existing);
            return ResponseEntity.ok(updated);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidScheduleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * DELETE /specialties/{id} — delete
     */
    @DeleteMapping("/specialties/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok("Deleted ID: " + id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
