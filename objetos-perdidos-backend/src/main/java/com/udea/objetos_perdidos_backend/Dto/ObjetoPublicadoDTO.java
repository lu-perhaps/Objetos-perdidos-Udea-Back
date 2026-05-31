package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDate;

public class ObjetoPublicadoDTO {

    private Integer id;
    private String nombre;
    private String descripcionGeneral;
    private String descripcionDetallada;
    private LocalDate fechaHallazgo;
    private String fotografia;
    private String categoria;
    private String lugarEncontrado;
    private String lugarActual;
    private Integer idEstado;
    private String estado;

    public ObjetoPublicadoDTO(
            Integer id,
            String nombre,
            String descripcionGeneral,
            String descripcionDetallada,
            LocalDate fechaHallazgo,
            String fotografia,
            String categoria,
            String lugarEncontrado,
            String lugarActual
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcionGeneral = descripcionGeneral;
        this.descripcionDetallada = descripcionDetallada;
        this.fechaHallazgo = fechaHallazgo;
        this.fotografia = fotografia;
        this.categoria = categoria;
        this.lugarEncontrado = lugarEncontrado;
        this.lugarActual = lugarActual;
    }

    public ObjetoPublicadoDTO(
            Integer id,
            String nombre,
            String descripcionGeneral,
            String descripcionDetallada,
            LocalDate fechaHallazgo,
            String fotografia,
            Integer idEstado,
            String estado,
            String categoria,
            String lugarEncontrado,
            String lugarActual
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcionGeneral = descripcionGeneral;
        this.descripcionDetallada = descripcionDetallada;
        this.fechaHallazgo = fechaHallazgo;
        this.fotografia = fotografia;
        this.idEstado = idEstado;
        this.estado = estado;
        this.categoria = categoria;
        this.lugarEncontrado = lugarEncontrado;
        this.lugarActual = lugarActual;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public String getDescripcionDetallada() {
        return descripcionDetallada;
    }

    public LocalDate getFechaHallazgo() {
        return fechaHallazgo;
    }

    public String getFotografia() {
        return fotografia;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getLugarEncontrado() {
        return lugarEncontrado;
    }

    public String getLugarActual() {
        return lugarActual;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public String getEstado() {
        return estado;
    }
}