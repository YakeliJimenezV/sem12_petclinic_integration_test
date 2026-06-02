package com.tecsup.petclinic.dtos;
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

    private String name;

    private String office;

    private Integer hOpen;

    private Integer hClose;

}
