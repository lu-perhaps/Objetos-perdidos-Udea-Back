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
}