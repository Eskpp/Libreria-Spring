/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.repositorios;

import com.libromundo.elLibros.entidades.Autor;
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
public interface AutorRepositorio extends JpaRepository<Autor, String>{
    
    @Query("SELECT a FROM Autor a WHERE a.nombre LIKE :tito")
    public Autor buscarPorNombre(@Param("tito") String nombre);
    
//    @Query("SELECT a.id FROM Autor a WHERE a.nombre LIKE :nombre")
//    public String buscarIdPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT a FROM Autor a WHERE a.baja IS NULL")
    public List<Autor> listar();
    
    @Query("SELECT a FROM Autor a WHERE a.baja IS NOT NULL")
    public List<Autor> listarBaja();
     
}
