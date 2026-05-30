package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_lugar")
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}