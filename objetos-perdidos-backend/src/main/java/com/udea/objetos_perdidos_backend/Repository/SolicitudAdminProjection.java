package com.udea.objetos_perdidos_backend.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SolicitudAdminProjection {

    Integer getId();

    String getDescripcion();

    LocalDateTime getFecha();

    LocalDate getFechaAproxPerdida();

    Integer getIdEstado();

    Integer getIdReporte();

    String getObjeto();

    String getFotografia();

    String getDescripcionObjeto();

    String getCorreoUsuario();

    String getLugar();
}