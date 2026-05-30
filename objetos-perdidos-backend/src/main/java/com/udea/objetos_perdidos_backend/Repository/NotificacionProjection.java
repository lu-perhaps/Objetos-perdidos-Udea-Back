package com.udea.objetos_perdidos_backend.Repository;

import java.time.LocalDateTime;

public interface NotificacionProjection {

    Integer getId();

    String getMensaje();

    LocalDateTime getFechaEnvio();

    Boolean getLeida();

    String getTipo();
}