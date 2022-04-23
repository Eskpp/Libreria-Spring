/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.controladores;

import com.libromundo.elLibros.entidades.Autor;
import com.libromundo.elLibros.errores.ErrorServicio;
import com.libromundo.elLibros.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Sanat
 */
@Controller
@RequestMapping("/AdministrarAutores") //elLibros.libromundo.com/AdministrarAutor/cargar_autores
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/cargar")
    public String cargar() {
        return "cargar_autores.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String nombreAutor) {
        try {
            autorServicio.registrar(nombreAutor);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "cargar_autores.html";
        }
        return "index.html";
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listar();
        modelo.addAttribute("autores", autores);
        return "listar_autores";
    }

}
