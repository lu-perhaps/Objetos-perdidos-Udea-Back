package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_entrega_objeto")
public class EntregaObjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_objeto")
    private Integer idObjeto;

    @Column(name = "id_persona_recibe")
    private Integer idPersonaRecibe;

    @Column(name = "id_persona_entrega")
    private Integer idPersonaEntrega;

    @Column(name = "id_solicitud_reclamo")
    private Integer idSolicitudReclamo;

    private String observaciones;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    public Integer getId() {
        return id;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public Integer getIdPersonaRecibe() {
        return idPersonaRecibe;
    }

    public Integer getIdPersonaEntrega() {
        return idPersonaEntrega;
    }

    public Integer getIdSolicitudReclamo() {
        return idSolicitudReclamo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public void setIdPersonaRecibe(Integer idPersonaRecibe) {
        this.idPersonaRecibe = idPersonaRecibe;
    }

    public void setIdPersonaEntrega(Integer idPersonaEntrega) {
        this.idPersonaEntrega = idPersonaEntrega;
    }

    public void setIdSolicitudReclamo(Integer idSolicitudReclamo) {
        this.idSolicitudReclamo = idSolicitudReclamo;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
