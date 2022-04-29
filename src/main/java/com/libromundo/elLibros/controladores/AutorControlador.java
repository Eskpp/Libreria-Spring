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
@RequestMapping("/AdministrarAutores") //elLibros.libromundo.com/AdministrarAutor/{metodos}
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
            modelo.put("nombreAutor", nombreAutor.trim());
            modelo.put("error", ex.getMessage());
            return "cargar_autores.html";
        }
        return "index.html";
    }

    @PostMapping("/darBaja")
    public String darBaja(ModelMap modelo, @RequestParam String id) {
        try {
            autorServicio.darBaja(id);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "redirect:/AdministrarAutores/listar";
    }

    @PostMapping("/darAlta")
    public String darAlta(ModelMap modelo, @RequestParam String id) {
        try {
            autorServicio.darAlta(id);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "redirect:/AdministrarAutores/listarBaja";
    }

    @GetMapping("/borrar")
    public String borrar(ModelMap modelo, @RequestParam String id) {
        try {
            autorServicio.borrar(id);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "redirect:/AdministrarLibros/listarBaja";
        }
        return "redirect:/AdministrarLibros/listarBaja";
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listar();
        modelo.addAttribute("autores", autores);
        return "listar_autores";
    }

    @GetMapping("/listarBaja")
    public String listarBaja(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarBaja();
        modelo.addAttribute("autoresBaja", autores);
        return "listar_autores";
    }

}
