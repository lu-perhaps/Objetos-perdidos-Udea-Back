package com.udea.objetos_perdidos_backend.Dto;

import java.time.LocalDateTime;

public class NotificacionDTO {

    private Integer id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private Boolean leida;
    private String tipo;

    public NotificacionDTO(
            Integer id,
            String mensaje,
            LocalDateTime fechaEnvio,
            Boolean leida,
            String tipo
    ) {
        this.id = id;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.leida = leida;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public Boolean getLeida() {
        return leida;
    }

    public String getTipo() {
        return tipo;
    }
}