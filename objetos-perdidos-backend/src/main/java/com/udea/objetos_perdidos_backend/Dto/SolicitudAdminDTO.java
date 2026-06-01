package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SolicitudAdminDTO {

    private Integer id;
    private String descripcion;
    private LocalDateTime fecha;
    private LocalDate fechaAproxPerdida;
    private Integer idEstado;
    private String objeto;
    private String correoUsuario;
    private String lugar;
    private String fotografia;
    private String descripcionObjeto;

    public SolicitudAdminDTO(
            Integer id,
            String descripcion,
            LocalDateTime fecha,
            LocalDate fechaAproxPerdida,
            Integer idEstado,
            String objeto,
            String correoUsuario,
            String lugar
    ) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.fechaAproxPerdida = fechaAproxPerdida;
        this.idEstado = idEstado;
        this.objeto = objeto;
        this.correoUsuario = correoUsuario;
        this.lugar = lugar;
    }

    public SolicitudAdminDTO(
            Integer id,
            String descripcion,
            LocalDateTime fecha,
            LocalDate fechaAproxPerdida,
            Integer idEstado,
            String objeto,
            String correoUsuario,
            String lugar,
            String fotografia,
            String descripcionObjeto
    ) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.fechaAproxPerdida = fechaAproxPerdida;
        this.idEstado = idEstado;
        this.objeto = objeto;
        this.correoUsuario = correoUsuario;
        this.lugar = lugar;
        this.fotografia = fotografia;
        this.descripcionObjeto = descripcionObjeto;
    }

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public LocalDate getFechaAproxPerdida() {
        return fechaAproxPerdida;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public String getObjeto() {
        return objeto;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFotografia() {
        return fotografia;
    }

    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }
}