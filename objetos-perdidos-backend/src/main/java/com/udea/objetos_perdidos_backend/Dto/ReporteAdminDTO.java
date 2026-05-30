package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReporteAdminDTO {

    private Integer id;
    private String descripcionObjeto;
    private LocalDateTime fechaReporte;
    private LocalDate fechaAproxPerdida;
    private Integer idEstado;
    private String correoUsuario;
    private String nombreUsuario;
    private String lugar;

    public ReporteAdminDTO(
            Integer id,
            String descripcionObjeto,
            LocalDateTime fechaReporte,
            LocalDate fechaAproxPerdida,
            Integer idEstado,
            String correoUsuario,
            String nombreUsuario,
            String lugar
    ) {
        this.id = id;
        this.descripcionObjeto = descripcionObjeto;
        this.fechaReporte = fechaReporte;
        this.fechaAproxPerdida = fechaAproxPerdida;
        this.idEstado = idEstado;
        this.correoUsuario = correoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.lugar = lugar;
    }

    public Integer getId() {
        return id;
    }

    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public LocalDate getFechaAproxPerdida() {
        return fechaAproxPerdida;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getLugar() {
        return lugar;
    }
}