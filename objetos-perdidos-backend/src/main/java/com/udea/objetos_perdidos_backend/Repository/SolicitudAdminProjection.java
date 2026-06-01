package com.udea.objetos_perdidos_backend.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SolicitudAdminProjection {

    Integer getId();

    String getDescripcion();

    LocalDateTime getFecha();

    LocalDate getFechaAproxPerdida();

    Integer getIdEstado();

    String getObjeto();

    String getCorreoUsuario();

    String getLugar();

    String getFotografia();

    String getDescripcionObjeto();
}