package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Model.Lugar;
import com.udea.objetos_perdidos_backend.Repository.LugarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LugarService {

    private final LugarRepository lugarRepository;

    public LugarService(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public List<Lugar> listarLugares() {
        return lugarRepository.findAllByOrderByIdAsc();
    }
}