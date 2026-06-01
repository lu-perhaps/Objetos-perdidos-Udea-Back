package com.udea.objetos_perdidos_backend.Repository;

import com.udea.objetos_perdidos_backend.Model.ReportePerdida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportePerdidaRepository extends JpaRepository<ReportePerdida, Integer> {

    @Query(value = """
            SELECT
                r.id AS id,
                r.descripcion_objeto AS "descripcionObjeto",
                r.fecha_reporte AS "fechaReporte",
                r.fecha_aprox_perdida AS "fechaAproxPerdida",
                r.id_estado AS "idEstado",
                p.correo AS "correoUsuario",
                p.nombre AS "nombreUsuario",
                l.nombre AS lugar
            FROM tbl_reporte_perdida r
            INNER JOIN tbl_persona p ON p.id = r.id_persona
            LEFT JOIN tbl_lugar l ON l.id = r.id_lugar_aprox_perdida
            ORDER BY r.fecha_reporte DESC
            """, nativeQuery = true)
    List<ReporteAdminProjection> listarReportesAdmin();

    @Query(value = """
            SELECT
                r.id AS id,
                r.descripcion_objeto AS "descripcionObjeto",
                r.fecha_reporte AS "fechaReporte",
                r.fecha_aprox_perdida AS "fechaAproxPerdida",
                r.id_estado AS "idEstado",
                p.correo AS "correoUsuario",
                p.nombre AS "nombreUsuario",
                l.nombre AS lugar
            FROM tbl_reporte_perdida r
            INNER JOIN tbl_persona p ON p.id = r.id_persona
            LEFT JOIN tbl_lugar l ON l.id = r.id_lugar_aprox_perdida
            WHERE LOWER(p.correo) = LOWER(?1)
            ORDER BY r.fecha_reporte DESC
            """, nativeQuery = true)
    List<ReporteAdminProjection> listarReportesUsuario(String correo);
}