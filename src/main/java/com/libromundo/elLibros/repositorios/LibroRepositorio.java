/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.repositorios;

import com.libromundo.elLibros.entidades.Libro;
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
public interface LibroRepositorio extends JpaRepository<Libro, String>{
    
    @Query("SELECT l FROM Libro l WHERE l.baja IS NULL")
    public List<Libro> listar();
    
    @Query("SELECT l FROM Libro l WHERE l.baja IS NOT NULL")
    public List<Libro> listarBaja();
    
    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro buscarPorIsbn(@Param("isbn") Long isbn);
    
}
