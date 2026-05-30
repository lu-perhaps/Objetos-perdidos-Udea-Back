package com.udea.objetos_perdidos_backend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_publicacion")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_objeto")
    private Integer idObjeto;

    private LocalDateTime fecha;

    @Column(name = "id_persona_publica")
    private Integer idPersonaPublica;

    @Column(name = "id_estado")
    private Integer idEstado;

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setIdPersonaPublica(Integer idPersonaPublica) {
        this.idPersonaPublica = idPersonaPublica;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public Integer getIdEstado() {
    return idEstado;
}

}