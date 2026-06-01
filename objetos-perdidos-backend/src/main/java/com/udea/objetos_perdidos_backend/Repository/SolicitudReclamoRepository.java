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
                l.nombre AS lugar
            FROM tbl_solicitud_reclamo s
            INNER JOIN tbl_objeto o ON o.id = s.id_objeto
            INNER JOIN tbl_persona p ON p.id = s.id_persona
            LEFT JOIN tbl_lugar l ON l.id = s.id_lugar_aprox_perdida
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
                l.nombre AS lugar
            FROM tbl_solicitud_reclamo s
            INNER JOIN tbl_objeto o ON o.id = s.id_objeto
            INNER JOIN tbl_persona p ON p.id = s.id_persona
            LEFT JOIN tbl_lugar l ON l.id = s.id_lugar_aprox_perdida
            WHERE LOWER(p.correo) = LOWER(?1)
            ORDER BY s.fecha DESC
            """, nativeQuery = true)
    List<SolicitudAdminProjection> listarSolicitudesUsuario(String correo);
}