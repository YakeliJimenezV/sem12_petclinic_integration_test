package com.tecsup.petclinic.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpecialtyDTO {

    private Integer id;
    private String  name;
    private String  office;

    @JsonProperty("hOpen")
    private Integer hOpen;

    @JsonProperty("hClose")
    private Integer hClose;
}