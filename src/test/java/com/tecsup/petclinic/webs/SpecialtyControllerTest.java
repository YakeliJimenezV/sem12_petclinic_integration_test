package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();

    // =========================================================================
    // INTEGRANTE A - Listado y consulta
    // =========================================================================

    @Test
    public void testFindAllSpecialties() throws Exception {

        String FIRST_NAME = "radiology";

        this.mockMvc
                .perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$[0].name", is(FIRST_NAME)));
    }

    @Test
    public void testFindSpecialtyOK() throws Exception {

        int ID = 1;
        String NAME = "radiology";
        String OFFICE = "Farewell";
        int H_OPEN = 8;
        int H_CLOSE = 18;

        this.mockMvc
                .perform(get("/specialties/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.office", is(OFFICE)))
                .andExpect(jsonPath("$.hOpen", is(H_OPEN)))
                .andExpect(jsonPath("$.hClose", is(H_CLOSE)));
    }

    @Test
    public void testFindSpecialtyKO() throws Exception {

        this.mockMvc
                .perform(get("/specialties/666"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
// =========================================================================
// INTEGRANTE B — Creación y búsqueda
// =========================================================================


    @Test
    public void testCreateSpecialty() throws Exception {


        String NAME    = "oncology";
        String OFFICE  = "BuildingA";
        int    H_OPEN  = 9;
        int    H_CLOSE = 17;


        SpecialtyDTO newSpecialty = SpecialtyDTO.builder()
                .name(NAME)
                .office(OFFICE)
                .hOpen(H_OPEN)
                .hClose(H_CLOSE)
                .build();


        this.mockMvc
                .perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",   is(NAME)))
                .andExpect(jsonPath("$.office", is(OFFICE)))
                .andExpect(jsonPath("$.hOpen",  is(H_OPEN)))
                .andExpect(jsonPath("$.hClose", is(H_CLOSE)));
    }


    @Test
    public void testFindByName() throws Exception {


        String NAME            = "surgery";
        String OFFICE_EXPECTED = "Maryland";


        this.mockMvc
                .perform(get("/specialties").param("name", NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$[0].name",   is(NAME)))
                .andExpect(jsonPath("$[0].office", is(OFFICE_EXPECTED)));
    }


    @Test
    public void testFindByOffice() throws Exception {


        String OFFICE        = "Farewell";
        String NAME_EXPECTED = "radiology";


        this.mockMvc
                .perform(get("/specialties").param("office", OFFICE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$[0].name",   is(NAME_EXPECTED)))
                .andExpect(jsonPath("$[0].office", is(OFFICE)));
    }

    // =========================================================================
    // INTEGRANTE C - Validaciones y borrado
    // =========================================================================

    @Test
    public void testCreateSpecialty_InvalidSchedule() throws Exception {

        SpecialtyDTO invalid = SpecialtyDTO.builder()
                .name("cardiology")
                .office("MainHall")
                .hOpen(10)
                .hClose(10)
                .build();

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(invalid))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateSpecialty_InvalidHourRange() throws Exception {

        SpecialtyDTO outOfRange = SpecialtyDTO.builder()
                .name("neurology")
                .office("WestWing")
                .hOpen(25)
                .hClose(30)
                .build();

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(outOfRange))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteSpecialty() throws Exception {

        SpecialtyDTO newSpecialty = SpecialtyDTO.builder()
                .name("oncology_test")
                .office("EastBlock")
                .hOpen(7)
                .hClose(15)
                .build();

        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("oncology_test")));

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/specialties/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }
}