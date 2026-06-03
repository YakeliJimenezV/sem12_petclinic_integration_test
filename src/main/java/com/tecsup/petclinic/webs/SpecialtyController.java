package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST controller for Specialty endpoints.
 * @author jgomezm
 */
@RestController
@Slf4j
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    /**
     * GET /specialties              → all specialties
     * GET /specialties?name=X       → filter by name
     * GET /specialties?office=X     → filter by office
     */
    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyDTO>> findSpecialties(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String office) {

        if (name != null) {
            List<SpecialtyDTO> list = specialtyService.findByName(name);
            log.info("findByName({}) -> {}", name, list);
            return ResponseEntity.ok(list);
        }
        if (office != null) {
            List<SpecialtyDTO> list = specialtyService.findByOffice(office);
            log.info("findByOffice({}) -> {}", office, list);
            return ResponseEntity.ok(list);
        }
        List<SpecialtyDTO> list = specialtyService.findAllDTO();
        log.info("findAll -> {}", list);
        return ResponseEntity.ok(list);
    }

    /**
     * GET /specialties/{id}
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
     * POST /specialties
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
     * PUT /specialties/{id}
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
            return ResponseEntity.ok(specialtyService.update(existing));
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidScheduleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * DELETE /specialties/{id}
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