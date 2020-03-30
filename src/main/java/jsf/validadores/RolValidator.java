/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

import entidades.Rol;
import java.util.regex.Pattern;

/**
 *
 * @author jcami
 */
public class RolValidator {
    
    public static void validate(Rol rol) throws Exception{
        if(rol.getIdRol()==null){
            throw new Exception("El id no puede ser nulo");
        }else if(rol.getNombre()==null){
            throw new Exception("El nombre no puede ser nulo");
        }else if(rol.getNombre().equals("")){
            throw new Exception("El nombre no puede ser vacío");
        }
        boolean esAlfabetico = Pattern.matches("^[a-zA-Z]*$", rol.getNombre());
        if(!esAlfabetico){
            throw new Exception("El nombre debe ser alfabetico");
        }
    }
    
}
