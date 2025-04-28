package com.minegocio.minegocio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDireccionDTO {

    private Long id;
    private String provincia;
    private String ciudad;
    private String direccion;
    private Boolean esMatriz;
}
