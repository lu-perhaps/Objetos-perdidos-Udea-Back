package com.udea.objetos_perdidos_backend.Dto;

public class PersonaDTO {

    private Integer id;
    private String nombre;
    private String correo;
    private String celular;
    private String numDocumento;
    private Integer idRol;
    private Integer idEstado;
    private Integer idTipoDocumento;
    private String tipoDocumento;

    public PersonaDTO(
            Integer id,
            String nombre,
            String correo,
            String celular,
            String numDocumento,
            Integer idRol,
            Integer idEstado,
            Integer idTipoDocumento,
            String tipoDocumento
    ) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
        this.numDocumento = numDocumento;
        this.idRol = idRol;
        this.idEstado = idEstado;
        this.idTipoDocumento = idTipoDocumento;
        this.tipoDocumento = tipoDocumento;
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

    public String getCelular() {
        return celular;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }
}