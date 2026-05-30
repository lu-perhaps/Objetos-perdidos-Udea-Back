package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDate;

public class ReportePerdidaRequest {

    private String descripcionObjeto;
    private String correoUsuario;
    private Integer idLugarAproxPerdida;
    private LocalDate fechaAproxPerdida;

    public String getDescripcionObjeto() {
        return descripcionObjeto;
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