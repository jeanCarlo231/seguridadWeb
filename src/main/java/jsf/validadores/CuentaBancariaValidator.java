/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

import entidades.CuentaBancaria;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author jcami
 */
public class CuentaBancariaValidator {
    
    public static ValidatorResult validar(CuentaBancaria cuentaBancaria){
        try{
            validarNumeroTarjeta(cuentaBancaria.getNumeroTarjeta());
            validarFechaExp(cuentaBancaria.getFechaExpiracion());
            validarCVC(cuentaBancaria.getNumeroTarjeta(), cuentaBancaria.getCvc());
            validarBanco(cuentaBancaria.getNombreBanco());
            return new ValidatorResult(null, true);
        }catch(ValidatorException ve){
            return new ValidatorResult(ve.getMessage(),false);
        }
    }
    
    private static void validarNumeroTarjeta(long numeroTarjeta)throws ValidatorException{
        System.out.println("Validando tarjeta: "+numeroTarjeta);
        boolean isValid = new CreditCardValidator().validateCard(String.valueOf(numeroTarjeta));
        if(!isValid){
            throw new ValidatorException(new FacesMessage("El número de tarjeta es inválido"));
        }
    }
    
    private static void validarFechaExp(Date fecha) throws ValidatorException{
        Date fechaActual = new Date();
        if(fecha.compareTo(fechaActual)<0){ //fecha es antes de la fechaActual. Se expiró la tarjeta
            throw new ValidatorException(new FacesMessage("La tarjeta ya expiró"));            
        }
    }
    
    private static void validarCVC(long numeroTarjeta, int cvc){
        boolean isValid = new CreditCardValidator().validateCVV(String.valueOf(numeroTarjeta), String.valueOf(cvc));
        if(!isValid){
            throw new ValidatorException(new FacesMessage("El número CVC es inválido."));
        }
    }
    
    private static void validarBanco(String nombreBanco) throws ValidatorException{
        if(nombreBanco == null){
            throw new ValidatorException(new FacesMessage("El nombre del banco no puede ser nulo"));
        }
        if(nombreBanco.equals("")){
            throw new ValidatorException(new FacesMessage("El nombre del banco no puede ser vacío"));
        }
        if(!Common.isAlphabetic(nombreBanco)){
            throw new ValidatorException(new FacesMessage("El nombre del banco debe ser alfabético"));
        }
    }
    
    /**
     * @param numero
     * @return suma de los digítos de las posiciones pares del número
     */
    private static int sumOfDoubleEvenPlace(long numero) {
        int sum = 0; 
        String num = numero + ""; 
        for (int i = getSize(numero) - 2; i >= 0; i -= 2)  
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);
        return sum; 
    } 
  
    /**
     * @param number
     * @return retorna el número si es un sólo dígito, sino retorna la suma de los dos dígitos
     */
    private static int getDigit(int number){
        if (number < 9)
            return number; 
        return number / 10 + number % 10; 
    }
    
    /**
     * @param number
     * @return suma de los digítos de las posiciones impares del número
     */
    private static int sumOfOddPlace(long number){ 
        int sum = 0; 
        String num = number + ""; 
        for (int i = getSize(number) - 1; i >= 0; i -= 2)  
            sum += Integer.parseInt(num.charAt(i) + "");         
        return sum; 
    } 
  
    /**
     * Retorna true si el digito d es un prefiero del numero
    */
    private static boolean prefixMatched(long numero, int d){ 
        return getPrefix(numero, getSize(d)) == d; 
    } 
  
    /**
     * @param d
     * @return número de digitos del número d
     */
    private static int getSize(long d){ 
        String num = d + ""; 
        return num.length(); 
    } 
    
    /**
     * @param numero
     * @param k
     * @return Retorna los primeros k dígitos del número. Si el número es menos que k retorna el número
     */
    private static long getPrefix(long numero, int k){
        if (getSize(numero) > k) {
            String num = numero + ""; 
            return Long.parseLong(num.substring(0, k)); 
        } 
        return numero;
    } 
    
}
