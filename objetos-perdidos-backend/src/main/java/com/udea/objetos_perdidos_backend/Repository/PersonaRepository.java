package com.udea.objetos_perdidos_backend.Repository;

import com.udea.objetos_perdidos_backend.Model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    Optional<Persona> findByCorreo(String correo);

    @Query(value = """
            SELECT
                p.id AS id,
                p.nombre AS nombre,
                p.correo AS correo,
                p.celular AS celular,
                p.num_documento AS "numDocumento",
                p.id_rol AS "idRol",
                p.id_estado AS "idEstado",
                p.id_tipo_documento AS "idTipoDocumento",
                td.nombre AS "tipoDocumento"
            FROM tbl_persona p
            LEFT JOIN tbl_tipo_documento td ON td.id = p.id_tipo_documento
            WHERE p.id_rol = 1
            ORDER BY p.nombre ASC NULLS LAST, p.correo ASC
            """, nativeQuery = true)
    List<PersonaProjection> listarEstudiantes();
}