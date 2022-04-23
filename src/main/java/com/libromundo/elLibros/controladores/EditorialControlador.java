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
            return "/cargar_editoriales.html";
        }
        return "index.html";
    }
         
    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listar();
        modelo.addAttribute("editoriales", editoriales);
        return "listar_editoriales.html";
    }
}
