package com.tecsup.petclinic.webs;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();

    // =========================================================================
    // INTEGRANTE Sheyla Chuco:  Listado y consulta Sheyla Chuco
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

        int    ID      = 1;
        String NAME    = "radiology";
        String OFFICE  = "Farewell";
        int    H_OPEN  = 8;
        int    H_CLOSE = 18;

        this.mockMvc
                .perform(get("/specialties/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.id",     is(ID)))
                .andExpect(jsonPath("$.name",   is(NAME)))
                .andExpect(jsonPath("$.office", is(OFFICE)))
                .andExpect(jsonPath("$.hOpen",  is(H_OPEN)))
                .andExpect(jsonPath("$.hClose", is(H_CLOSE)));
    }

    @Test
    public void testFindSpecialtyKO() throws Exception {

        this.mockMvc
                .perform(get("/specialties/666"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}