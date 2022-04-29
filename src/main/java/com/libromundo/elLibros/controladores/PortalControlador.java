/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libromundo.elLibros.controladores;


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
@RequestMapping("/") //google.com/...
public class PortalControlador {

    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }



    

    
    
}
