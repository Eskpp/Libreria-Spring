/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.servicios;

import com.libromundo.elLibros.entidades.Autor;
import com.libromundo.elLibros.errores.ErrorServicio;
import com.libromundo.elLibros.repositorios.AutorRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sanat
 */
@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor registrar(String nombre) throws ErrorServicio {

        validar(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(new Date());
        
        return autorRepositorio.save(autor);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor modificar(String id, String nombre) throws ErrorServicio {

        validar(nombre);

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            return autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró un autor con ese id.");
        }

    }

    @Transactional(propagation = Propagation.NESTED)
    public void darBaja(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setBaja(new Date());
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró un autor con ese id.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void darAlta(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setBaja(null);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró un autor con ese id.");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Autor> listar(){
        return autorRepositorio.listar();
    }
    
    public void eliminar(String id) throws ErrorServicio{
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autorRepositorio.delete(autor);
        } else {
            throw new ErrorServicio("No se encontró un autor con ese id.");
        }
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede ser nulo.");
        }
    }

}
