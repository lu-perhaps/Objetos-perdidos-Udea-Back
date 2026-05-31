package com.udea.objetos_perdidos_backend.Repository;

import com.udea.objetos_perdidos_backend.Model.Objeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ObjetoRepository extends JpaRepository<Objeto, Integer> {

    @Query(value = """
            SELECT
                o.id AS id,
                o.nombre AS nombre,
                o.descripcion_general AS descripcionGeneral,
                o.descripcion_detallada AS descripcionDetallada,
                o.fecha_hallazgo AS fechaHallazgo,
                o.fotografia AS fotografia,
                c.nombre AS categoria,
                le.nombre AS lugarEncontrado,
                la.nombre AS lugarActual
            FROM tbl_publicacion p
            INNER JOIN tbl_objeto o ON o.id = p.id_objeto
            LEFT JOIN tbl_categoria c ON c.id = o.id_categoria
            LEFT JOIN tbl_lugar le ON le.id = o.id_lugar_encontrado
            LEFT JOIN tbl_lugar la ON la.id = o.id_lugar_actual
            WHERE p.id_estado = 11
              AND o.id_estado = 3
            ORDER BY p.fecha DESC
            """, nativeQuery = true)
    List<ObjetoPublicadoProjection> listarObjetosPublicados();

    @Query(value = """
            SELECT
              o.id AS id,
              o.nombre AS nombre,
              o.descripcion_general AS descripcionGeneral,
              o.descripcion_detallada AS descripcionDetallada,
              o.fecha_hallazgo AS fechaHallazgo,
              o.fotografia AS fotografia,
              o.id_estado AS idEstado,
              e.nombre AS estado,
              c.nombre AS categoria,
              le.nombre AS lugarEncontrado,
              la.nombre AS lugarActual
            FROM tbl_objeto o
            LEFT JOIN tbl_estado e ON e.id = o.id_estado
            LEFT JOIN tbl_categoria c ON c.id = o.id_categoria
            LEFT JOIN tbl_lugar le ON le.id = o.id_lugar_encontrado
            LEFT JOIN tbl_lugar la ON la.id = o.id_lugar_actual
            ORDER BY o.id DESC
            """, nativeQuery = true)
    List<ObjetoPublicadoProjection> listarObjetosAdmin();
}