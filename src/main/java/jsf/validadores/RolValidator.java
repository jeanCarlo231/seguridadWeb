/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

import entidades.Rol;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author jcami
 */
public class RolValidator {
    
    public static ValidatorResult validar(Rol rol) {
        if(rol.getNombre() ==null || rol.getNombre().equals("")){
            return new ValidatorResult("El nombre del rol no puede ser vacío", false);
        }
        if(rol.getNombre().length()<=3 || rol.getNombre().length()>=21){
            return new ValidatorResult("El rol debe tener mínimo 4 caracteres y máximo 20", false);
        }
        boolean esAlfabetico = Pattern.matches("^[a-zA-Z]*$", rol.getNombre());
        if(!esAlfabetico){
            return new ValidatorResult("El nombre debe ser alfabetico", false);
        }
        return new ValidatorResult(null, true);
    }
    
}
