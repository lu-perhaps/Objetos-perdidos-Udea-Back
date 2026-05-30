package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDate;

public class ActualizarObjetoRequest {

    private String nombre;
    private String descripcionGeneral;
    private String descripcionDetallada;
    private Integer idCategoria;
    private LocalDate fechaHallazgo;
    private String fotografia;
    private Integer idLugarEncontrado;
    private Integer idLugarActual;

    public String getNombre() {
        return nombre;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public String getDescripcionDetallada() {
        return descripcionDetallada;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public LocalDate getFechaHallazgo() {
        return fechaHallazgo;
    }

    public String getFotografia() {
        return fotografia;
    }

    public Integer getIdLugarEncontrado() {
        return idLugarEncontrado;
    }

    public Integer getIdLugarActual() {
        return idLugarActual;
    }
}