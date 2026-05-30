package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "id_persona_recibe")
    private Integer idPersonaRecibe;

    @Column(name = "id_persona_envia")
    private Integer idPersonaEnvia;

    @Column(name = "id_tipo_notificacion")
    private Integer idTipoNotificacion;

    private Boolean leida;

    public Integer getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public Integer getIdPersonaRecibe() {
        return idPersonaRecibe;
    }

    public Integer getIdPersonaEnvia() {
        return idPersonaEnvia;
    }

    public Integer getIdTipoNotificacion() {
        return idTipoNotificacion;
    }

    public Boolean getLeida() {
        return leida;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public void setIdPersonaRecibe(Integer idPersonaRecibe) {
        this.idPersonaRecibe = idPersonaRecibe;
    }

    public void setIdPersonaEnvia(Integer idPersonaEnvia) {
        this.idPersonaEnvia = idPersonaEnvia;
    }

    public void setIdTipoNotificacion(Integer idTipoNotificacion) {
        this.idTipoNotificacion = idTipoNotificacion;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }
}