package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_objeto")
public class Objeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(name = "descripcion_general")
    private String descripcionGeneral;

    @Column(name = "descripcion_detallada")
    private String descripcionDetallada;

    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "fecha_hallazgo")
    private LocalDate fechaHallazgo;

    private String fotografia;

    @Column(name = "id_lugar_encontrado")
    private Integer idLugarEncontrado;

    @Column(name = "id_lugar_actual")
    private Integer idLugarActual;

    @Column(name = "id_estado")
    private Integer idEstado;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getIdLugarActual() {
        return idLugarActual;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }

    public void setDescripcionDetallada(String descripcionDetallada) {
        this.descripcionDetallada = descripcionDetallada;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setFechaHallazgo(LocalDate fechaHallazgo) {
        this.fechaHallazgo = fechaHallazgo;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public void setIdLugarEncontrado(Integer idLugarEncontrado) {
        this.idLugarEncontrado = idLugarEncontrado;
    }

    public void setIdLugarActual(Integer idLugarActual) {
        this.idLugarActual = idLugarActual;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
}