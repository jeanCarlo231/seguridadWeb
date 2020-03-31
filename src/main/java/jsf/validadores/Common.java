/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

import java.util.regex.Pattern;

/**
 *
 * @author jcami
 */
public class Common {
    
    private static final String regexAlphabetic = "^[a-zA-Z]*$";
    
    public static boolean isAlphabetic(String str){
        return Pattern.matches(regexAlphabetic, str);
    }
    
}
