package com.udea.objetos_perdidos_backend.Dto;

public class CoincidenciaRequest {

    private Integer idReporte;
    private Integer idObjeto;
    private String mensajePersonalizado;

    public Integer getIdReporte() {
        return idReporte;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public String getMensajePersonalizado() {
        return mensajePersonalizado;
    }
}