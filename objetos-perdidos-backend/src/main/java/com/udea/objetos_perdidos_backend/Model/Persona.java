package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private String correo;

    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "id_rol")
    private Integer idRol;

    private String celular;

    @Column(name = "num_documento")
    private String numDocumento;

    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    public Integer getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public String getCelular() {
        return celular;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }
}