package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_solicitud_reclamo")
public class SolicitudReclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    private LocalDateTime fecha;

    @Column(name = "id_objeto")
    private Integer idObjeto;

    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(name = "id_lugar_aprox_perdida")
    private Integer idLugarAproxPerdida;

    @Column(name = "fecha_aprox_perdida")
    private LocalDate fechaAproxPerdida;

    @Column(name = "id_estado")
    private Integer idEstado;

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public Integer getIdReporte() {
        return idReporte;
    }

    public Integer getIdLugarAproxPerdida() {
        return idLugarAproxPerdida;
    }

    public LocalDate getFechaAproxPerdida() {
        return fechaAproxPerdida;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public void setIdLugarAproxPerdida(Integer idLugarAproxPerdida) {
        this.idLugarAproxPerdida = idLugarAproxPerdida;
    }

    public void setFechaAproxPerdida(LocalDate fechaAproxPerdida) {
        this.fechaAproxPerdida = fechaAproxPerdida;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
}