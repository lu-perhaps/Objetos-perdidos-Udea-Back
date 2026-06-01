package com.udea.objetos_perdidos_backend.Repository;

public interface PersonaProjection {

    Integer getId();

    String getNombre();

    String getCorreo();

    String getCelular();

    String getNumDocumento();

    Integer getIdRol();

    Integer getIdEstado();

    Integer getIdTipoDocumento();

    String getTipoDocumento();
}