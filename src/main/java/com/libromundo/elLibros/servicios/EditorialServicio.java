/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.servicios;

import com.libromundo.elLibros.entidades.Editorial;
import com.libromundo.elLibros.errores.ErrorServicio;
import com.libromundo.elLibros.repositorios.EditorialRepositorio;
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
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Editorial registrar(String nombre) throws ErrorServicio {

        validar(nombre);

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre.trim());
        editorial.setAlta(new Date());

        return editorialRepositorio.save(editorial);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Editorial modificar(String id, String nombre) throws ErrorServicio {

        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);

            return editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró una editorial con ese id.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void darBaja(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setBaja(new Date());
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró una editorial con ese id.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void darAlta(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setBaja(null);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró una editorial con ese id.");
        }
    }

    @Transactional(readOnly = true)
    public List<Editorial> listar() {
        return editorialRepositorio.listar();
    }
    
    @Transactional(readOnly = true)
    public List<Editorial> listarBaja() {
        return editorialRepositorio.listarBaja();
    }
    
    @Transactional
    public void borrar(String id) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorialRepositorio.delete(editorial);
        } else {
            throw new ErrorServicio("No se encontró una editorial con ese id.");
        }
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede ser nulo.");
        }
        if (editorialRepositorio.buscarPorNombre(nombre.trim()) != null) {
            throw new ErrorServicio("Ya hay una editorial con ese nombre.");
        }
    }

}
