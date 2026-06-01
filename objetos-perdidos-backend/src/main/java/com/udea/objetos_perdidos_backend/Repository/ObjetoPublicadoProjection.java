package com.udea.objetos_perdidos_backend.Repository;

import java.time.LocalDate;

public interface ObjetoPublicadoProjection {

    Integer getId();

    String getNombre();

    String getDescripcionGeneral();

    String getDescripcionDetallada();

    LocalDate getFechaHallazgo();

    String getFotografia();

    String getCategoria();

    String getLugarEncontrado();

    String getLugarActual();

    Integer getIdEstado();

    String getEstado();

    Integer getTiempoMaximoAlmacenamiento();
}