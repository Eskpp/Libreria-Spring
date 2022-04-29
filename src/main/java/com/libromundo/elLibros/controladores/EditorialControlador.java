/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.controladores;

import com.libromundo.elLibros.entidades.Editorial;
import com.libromundo.elLibros.errores.ErrorServicio;
import com.libromundo.elLibros.servicios.EditorialServicio;
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

/**
 *
 * @author Sanat
 */
@Controller
@RequestMapping("/AdministrarEditoriales")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
 
    @GetMapping("/cargar")
    public String cargar(){
        return "cargar_editoriales.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String nombreEditorial){
        try {
            editorialServicio.registrar(nombreEditorial);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombreEditorial", nombreEditorial.trim());
            return "/cargar_editoriales.html";
        }
        return "index.html";
    }
    
    @PostMapping("/darBaja")
    public String darBaja(ModelMap modelo, @RequestParam String id){
        try {
            editorialServicio.darBaja(id);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "redirect:/AdministrarEditoriales/listar";
    }
    
    @PostMapping("/darAlta")
    public String darAlta(ModelMap modelo, @RequestParam String id){
        try {
            editorialServicio.darAlta(id);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "redirect:/AdministrarEditoriales/listarBaja";
    }
    
    @GetMapping("/borrar")
    public String borrar(ModelMap modelo, @RequestParam String id){
        try {
            editorialServicio.borrar(id);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "redirect:/AdministrarLibros/listarBaja";
        } 
        return "redirect:/AdministrarLibros/listarBaja";
    }
         
    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listar();
        modelo.addAttribute("editoriales", editoriales);
        return "listar_editoriales.html";
    }
    
    @GetMapping("/listarBaja")
    public String listarBaja(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarBaja();
        modelo.addAttribute("editorialesBaja", editoriales);
        return "listar_editoriales.html";
    }
}
