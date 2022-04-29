/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.controladores;

import com.libromundo.elLibros.entidades.Libro;
import com.libromundo.elLibros.errores.ErrorServicio;
import com.libromundo.elLibros.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Sanat
 */
@Controller
@RequestMapping("/AdministrarLibros")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/cargar")
    public String cargar() {
        return "cargar_libros.html";
    }

    @PostMapping("/registrar") // no deberia ser Post en vez de get?= no me deja poner post
    public String registrar(ModelMap modelo, @RequestParam String titulo, @RequestParam(required = false) Long isbn, @RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares, @RequestParam String autor, @RequestParam String editorial) {
        try { //error de que los valores numericos no me deja pasarlos en null, nunca llegan al validar

            libroServicio.registrar(titulo, isbn, anio, ejemplares, autor, editorial);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("isbn", isbn);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("autor", autor);
            modelo.put("editorial", editorial);
            return "/cargar_libros";
        }
        return "index.html";
    }

    @PostMapping("/darBaja")
    public String darBaja(ModelMap modelo, @RequestParam String id) {
        try {
            libroServicio.darBaja(id); //antes de saber de redirect:, se podia hacer asi pero quedaba el path antiguo (el de darBaja)
//            List<Libro> libros = libroServicio.listar();
//            modelo.addAttribute("libros", libros);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "redirect:/AdministrarLibros/listar";
        }
        return "redirect:/AdministrarLibros/listar";
    }

    @PostMapping("/darAlta")
    public String darAlta(ModelMap modelo, @RequestParam String id) {
        try {
            libroServicio.darAlta(id); //idem darBaja
//            List<Libro> libros = libroServicio.listarBaja();
//            modelo.addAttribute("librosBaja", libros);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "redirect:/AdministrarLibros/listarBaja";
    }

    @PostMapping("/borrar")
    public String borrar(ModelMap modelo, @RequestParam String id) {
        try {
            libroServicio.borrar(id);
            this.wait(200);
            return "redirect:/AdministrarLibros/listarBaja";
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "redirect:/AdministrarLibros/listarBaja";
//        } catch (InterruptedException ex) {
//            modelo.addAttribute("error", "error en el wait");
//            return "redirect:/AdministrarLibros/listarBaja";
        }
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroServicio.listar();
        modelo.addAttribute("libros", libros);
        return "listar_libros";
    }

    @GetMapping("/listarBaja")
    public String listarBaja(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarBaja();
        modelo.addAttribute("librosBaja", libros);
        return "listar_libros";
    }

}
