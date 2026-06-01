package com.udea.objetos_perdidos_backend.Repository;

import com.udea.objetos_perdidos_backend.Model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    @Query(value = """
            SELECT
                n.id AS id,
                n.mensaje AS mensaje,
                n.fecha_envio AS "fechaEnvio",
                n.leida AS leida,
                tn.nombre AS tipo
            FROM tbl_notificacion n
            INNER JOIN tbl_persona p ON p.id = n.id_persona_recibe
            INNER JOIN tbl_tipo_notificacion tn ON tn.id = n.id_tipo_notificacion
            WHERE lower(p.correo) = lower(:correo)
            ORDER BY n.fecha_envio DESC
            """, nativeQuery = true)
    List<NotificacionProjection> listarPorCorreo(String correo);

    List<Notificacion> findByIdPersonaRecibe(Integer idPersonaRecibe);

    void deleteByIdPersonaRecibe(Integer idPersonaRecibe);
}