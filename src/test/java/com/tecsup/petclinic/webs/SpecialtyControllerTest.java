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

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class SpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * POST con h_open >= h_close → 400 Bad Request
     */
    @Test
    public void testCreateSpecialty_InvalidSchedule() throws Exception {

        SpecialtyDTO invalid = SpecialtyDTO.builder()
                .name("cardiology")
                .office("MainHall")
                .hOpen(10)
                .hClose(10)   // igual → inválido
                .build();

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(invalid))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * POST con horas fuera de 0-23 → 400 Bad Request
     */
    @Test
    public void testCreateSpecialty_InvalidHourRange() throws Exception {

        SpecialtyDTO outOfRange = SpecialtyDTO.builder()
                .name("neurology")
                .office("WestWing")
                .hOpen(25)    // fuera de rango
                .hClose(30)
                .build();

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(outOfRange))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * POST para crear + DELETE → 200 OK
     * Auto-contenida: crea su propio dato antes de borrarlo
     */
    @Test
    public void testDeleteSpecialty() throws Exception {

        SpecialtyDTO newSpecialty = SpecialtyDTO.builder()
                .name("oncology_test")
                .office("EastBlock")
                .hOpen(7)
                .hClose(15)
                .build();

        // CREAR
        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("oncology_test")));

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // BORRAR
        mockMvc.perform(delete("/specialties/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }
}