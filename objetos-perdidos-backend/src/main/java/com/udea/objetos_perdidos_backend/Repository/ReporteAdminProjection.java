package com.udea.objetos_perdidos_backend.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReporteAdminProjection {

    Integer getId();

    String getDescripcionObjeto();

    LocalDateTime getFechaReporte();

    LocalDate getFechaAproxPerdida();

    Integer getIdEstado();

    String getCorreoUsuario();

    String getNombreUsuario();

    String getLugar();
}