/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.repositorios;

import com.libromundo.elLibros.entidades.Editorial;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 *
 * @author Sanat
 */
@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
    
    @Query("SELECT e from Editorial e WHERE e.nombre LIKE :nombre")
    public Editorial buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT e FROM Editorial e WHERE e.baja IS NULL")
    public List<Editorial> listar();
    
    @Query("SELECT e FROM Editorial e WHERE e.baja IS NOT NULL")
    public List<Editorial> listarBaja();
    
}
