package com.udea.objetos_perdidos_backend.Repository;

import com.udea.objetos_perdidos_backend.Model.SolicitudReclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface SolicitudReclamoRepository extends JpaRepository<SolicitudReclamo, Integer> {

    Optional<SolicitudReclamo> findByIdReporte(Integer idReporte);

    @Query(value = """
            SELECT
                s.id AS id,
                s.descripcion AS descripcion,
                s.fecha AS fecha,
                s.fecha_aprox_perdida AS "fechaAproxPerdida",
                s.id_estado AS "idEstado",
                s.id_reporte AS "idReporte",
                o.nombre AS objeto,
                o.fotografia AS fotografia,
                o.descripcion_general AS "descripcionObjeto",
                p.correo AS "correoUsuario",
                la.nombre AS lugar
            FROM tbl_solicitud_reclamo s
            INNER JOIN tbl_objeto o ON o.id = s.id_objeto
            INNER JOIN tbl_persona p ON p.id = s.id_persona
            LEFT JOIN tbl_lugar la ON la.id = o.id_lugar_actual
            ORDER BY s.fecha DESC
            """, nativeQuery = true)
    List<SolicitudAdminProjection> listarSolicitudesAdmin();

    @Query(value = """
            SELECT
                s.id AS id,
                s.descripcion AS descripcion,
                s.fecha AS fecha,
                s.fecha_aprox_perdida AS "fechaAproxPerdida",
                s.id_estado AS "idEstado",
                s.id_reporte AS "idReporte",
                o.nombre AS objeto,
                o.fotografia AS fotografia,
                o.descripcion_general AS "descripcionObjeto",
                p.correo AS "correoUsuario",
                la.nombre AS lugar
            FROM tbl_solicitud_reclamo s
            INNER JOIN tbl_objeto o ON o.id = s.id_objeto
            INNER JOIN tbl_persona p ON p.id = s.id_persona
            LEFT JOIN tbl_lugar la ON la.id = o.id_lugar_actual
            WHERE LOWER(p.correo) = LOWER(?1)
            ORDER BY s.fecha DESC
            """, nativeQuery = true)
    List<SolicitudAdminProjection> listarSolicitudesUsuario(String correo);
}