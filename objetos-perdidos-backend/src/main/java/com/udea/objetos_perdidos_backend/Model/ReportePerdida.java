package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_reporte_perdida")
public class ReportePerdida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion_objeto")
    private String descripcionObjeto;

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_aprox_perdida")
    private LocalDate fechaAproxPerdida;

    @Column(name = "id_lugar_aprox_perdida")
    private Integer idLugarAproxPerdida;

    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "id_estado")
    private Integer idEstado;

    public Integer getId() {
        return id;
    }

    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }

    public LocalDate getFechaAproxPerdida() {
        return fechaAproxPerdida;
    }

    public Integer getIdLugarAproxPerdida() {
        return idLugarAproxPerdida;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setDescripcionObjeto(String descripcionObjeto) {
        this.descripcionObjeto = descripcionObjeto;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public void setFechaAproxPerdida(LocalDate fechaAproxPerdida) {
        this.fechaAproxPerdida = fechaAproxPerdida;
    }

    public void setIdLugarAproxPerdida(Integer idLugarAproxPerdida) {
        this.idLugarAproxPerdida = idLugarAproxPerdida;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
}