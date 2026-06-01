package com.udea.objetos_perdidos_backend.Dto;

public class AuthMeDTO {

    private Integer id;
    private String nombre;
    private String correo;
    private Integer idRol;
    private Integer idEstado;
    private Boolean perfilCompleto;

    public AuthMeDTO(Integer id, String nombre, String correo, Integer idRol, Integer idEstado, Boolean perfilCompleto) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.idRol = idRol;
        this.idEstado = idEstado;
        this.perfilCompleto = perfilCompleto;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public Boolean getPerfilCompleto() {
        return perfilCompleto;
    }
}