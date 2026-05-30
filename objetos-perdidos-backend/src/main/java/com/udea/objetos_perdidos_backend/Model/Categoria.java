package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(name = "tiempo_maximo_almacenamiento")
    private Integer tiempoMaximoAlmacenamiento;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getTiempoMaximoAlmacenamiento() {
        return tiempoMaximoAlmacenamiento;
    }
}