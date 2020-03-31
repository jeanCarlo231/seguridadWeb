
package jsf.validadores;

import entidades.Usuario;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

public class UsuarioValidator {
    
    public static ValidatorResult validar(Usuario user) {
        
        //Validacion Campo usuario
        if(user.getUsuario() ==null || user.getUsuario().equals("")){
            return new ValidatorResult("El campo usuario no puede ser vacío", false);
        }
        if(user.getUsuario().length()<=3 || user.getUsuario().length()>=21){
            return new ValidatorResult("El campo usuario debe tener mínimo 4 caracteres y máximo 20", false);
        }
        boolean esAlfabetico = Pattern.matches("^[a-zA-Z]*$", user.getUsuario());
        if(!esAlfabetico){
            return new ValidatorResult("El campo usuario no debe contener numeros", false);
        }
        
        //Validacion campo contraseña
        
        if(user.getContraseña() ==null || user.getContraseña().equals("")){
            return new ValidatorResult("El campo contraseña no puede estar vacío", false);
        }
        if(user.getContraseña().length()<=8 || user.getContraseña().length()>=21){
            return new ValidatorResult("El campo contraseña debe tener mínimo 8 caracteres y máximo 20", false);
        }
        
 
               
        return new ValidatorResult(null, true);
    }
    
}
