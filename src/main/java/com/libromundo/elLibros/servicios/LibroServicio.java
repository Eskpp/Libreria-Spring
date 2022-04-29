/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.servicios;

import com.libromundo.elLibros.entidades.Autor;
import com.libromundo.elLibros.entidades.Editorial;
import com.libromundo.elLibros.entidades.Libro;
import com.libromundo.elLibros.errores.ErrorServicio;
import com.libromundo.elLibros.repositorios.AutorRepositorio;
import com.libromundo.elLibros.repositorios.EditorialRepositorio;
import com.libromundo.elLibros.repositorios.LibroRepositorio;
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
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private EditorialServicio editorialServicio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro registrar(String titulo, Long isbn, Integer anio, Integer ejemplares, String autorNombre, String editorialNombre) throws ErrorServicio {

        validar(titulo, isbn, anio, ejemplares, autorNombre, editorialNombre);

        Autor autor = autorRepositorio.buscarPorNombre(autorNombre);
        if (autor == null) { //crea autor si no lo encuentra en la linea anterior
            autor = autorServicio.registrar(autorNombre);
        }

        Editorial editorial = editorialRepositorio.buscarPorNombre(editorialNombre);
        if (editorial == null) {
            editorial = editorialServicio.registrar(editorialNombre);
        }

        Libro libro = new Libro();

        libro.setTitulo(titulo);
        libro.setIsbn(isbn);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAlta(new Date());

        return libroRepositorio.save(libro);
    }

    /*
    para libro puedo utilizar un metodo parecido al de guardar foto de la mascota para los autores y editoriales,
    el tema está en que estos deben tener nombre y apellido y no se como voy a crearlos dentro de libro, onda
    si con un selector o escribiendo nombre y apellido y fijarse si estos corresponden con un id ya creado y reemplazando
    o como hacerlo. asi que de momento voy a hacer como que a libro ya le paso un autor y una editorial no nulas.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Libro modificar(String id, String titulo, Long isbn, Integer anio, Integer ejemplares, String autorNombre, String editorialNombre) throws ErrorServicio {

        validar(titulo, isbn, anio, ejemplares, autorNombre, editorialNombre);

        Autor autor = autorRepositorio.buscarPorNombre(autorNombre.trim());
        if (autor == null) {
            autor = autorServicio.registrar(autorNombre.trim());
        }

        Editorial editorial = editorialRepositorio.buscarPorNombre(editorialNombre.trim());
        if (editorial == null) {
            editorial = editorialServicio.registrar(editorialNombre.trim());
        }

        //considerar hacer un metodo aparte solo para modificar ejemplares totales Ó metodo que actualice prestamos
        //si se hace un prestamo o si cambia el total de libros(igual seguro que entra en servicio prestamos)
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();

            //corroboro que ejemplares nuevo sea menor que ejemplares prestados actualmente
            if (libro.getEjemplaresPrestados() > ejemplares) {
                throw new ErrorServicio("No puede haber mas ejemplares prestados que ejemplares totales.");
            }

            libro.setTitulo(titulo);
            libro.setIsbn(isbn);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresRestantes(ejemplares - libro.getEjemplaresPrestados());
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            return libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró un libro con ese id.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void darBaja(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setBaja(new Date());
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró un libro con ese id.");
        }
    }

    @Transactional
    public void darAlta(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setBaja(null);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró un libro con ese id.");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Libro> listar(){
        return libroRepositorio.listar();
    }
    
    @Transactional(readOnly = true)
    public List<Libro> listarBaja(){
        return libroRepositorio.listarBaja();
    }
    
    @Transactional
    public void borrar(String id) throws ErrorServicio{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libroRepositorio.delete(libro);
        } else {
            throw new ErrorServicio("No se encontró un libro con ese id.");
        }
    }
    
    public String trimNombre(String nombre){
        return nombre.trim();
    }

    public void validar(String titulo, Long isbn, Integer anio, Integer ejemplares, String autor, String editorial) throws ErrorServicio {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorServicio("El título del libro no puede ser nulo.");
        }
        
        if (isbn == null) {
            throw new ErrorServicio("El isbn no puede ser nulo");
        }

        if (anio == null) {
            throw new ErrorServicio("El año de emisión no puede ser nulo.");
        }
        
        if (ejemplares == null) {
            throw new ErrorServicio("La cantidad de ejemplares no puede ser nula.");
        }

        if (autor == null || autor.trim().isEmpty()) {
            throw new ErrorServicio("El autor no puede ser nulo.");
        } //validar si existe o no un autor con ese nombre <--- puedo hacer esto o lo que hice arriba.

        if (editorial == null || editorial.trim().isEmpty()) {
            throw new ErrorServicio("La editorial no puede ser nula.");
        } //validar si existe o no un editoial. con ese nombre <--- puedo hacer esto o lo que hice arriba.
        
        if (libroRepositorio.buscarPorIsbn(isbn) != null) {
            throw new ErrorServicio("Ya existe un libro con ese ISBN");
        }
    }
}
