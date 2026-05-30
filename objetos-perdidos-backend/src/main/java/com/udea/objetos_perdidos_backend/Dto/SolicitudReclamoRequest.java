package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDate;

public class SolicitudReclamoRequest {

    private String descripcion;
    private Integer idObjeto;
    private String correoUsuario;
    private Integer idLugarAproxPerdida;
    private LocalDate fechaAproxPerdida;

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public Integer getIdLugarAproxPerdida() {
        return idLugarAproxPerdida;
    }

    public LocalDate getFechaAproxPerdida() {
        return fechaAproxPerdida;
    }
}