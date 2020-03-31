/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

import entidades.CuentaBancaria;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author jcami
 */
public class Tester {
    
    public static void main(String args[]){
        System.out.println("--- Realizando pruebas de validación");
        //pruebaCreditCardValidator();
        pruebaCuentaBancariaValidator();
    }
    
    public static void pruebaCreditCardValidator(){
        System.out.println("\n\n--- Ejecutando pruebas unitarias del validador de Tarjetas Bancarias---\n");
        
        String master_card = "5351402137550168", CVV_master_card = "332";
        String visa = "4760048064179840", CVV_visa="393";
        String amex = "347554891725556", CVV_amex="3483";
        String discover="6011917547864412", CVV_discover="454";
        String JCB="3568945896385212", CVV_JCB="308";
        
        CreditCardValidator validator = new CreditCardValidator();
        
        System.out.println("Validando tarjeta MASTER CARD...\n número:"+master_card+" CVV:"+CVV_master_card);
        System.out.println("Número de tarjeta is "+(validator.validateCard(master_card)?"valid":"not valid"));
        System.out.println("CVV is "+(validator.validateCVV(master_card, CVV_master_card)?"valid": "not valid"));
        System.out.println();
        assert(validator.validateCard(master_card));
        assert(validator.validateCVV(master_card, CVV_master_card));
        
        System.out.println("Validando tarjeta VISA...\n número:"+visa+" CVV:"+CVV_visa);
        System.out.println("Número de tarjeta is "+(validator.validateCard(visa)?"valid":"not valid"));
        System.out.println("CVV is "+(validator.validateCVV(visa, CVV_visa)?"valid": "not valid"));
        System.out.println();
        assert(validator.validateCard(visa));
        assert(validator.validateCVV(visa, CVV_visa));
        
        System.out.println("Validando tarjeta AMERICAN EXPRESS...\n número:"+amex+" CVV:"+CVV_amex);
        System.out.println("Número de tarjeta is "+(validator.validateCard(amex)?"valid":"not valid"));
        System.out.println("CVV is "+(validator.validateCVV(amex, CVV_amex)?"valid": "not valid"));
        System.out.println();
        assert(validator.validateCard(amex));
        assert(validator.validateCVV(amex, CVV_amex));
        
        System.out.println("Validando tarjeta DISCOVER...\n número:"+discover+" CVV:"+CVV_discover);
        System.out.println("Número de tarjeta "+(validator.validateCard(discover)?"valid":"not valid"));
        System.out.println("CVV is "+(validator.validateCVV(discover, CVV_discover)?"valid": "not valid"));
        System.out.println();
        assert(validator.validateCard(discover));
        assert(validator.validateCVV(discover, CVV_discover));
        
        System.out.println("Validando tarjeta JCB...\n número:"+JCB+" CVV:"+CVV_JCB);
        System.out.println("Número de tarjeta is "+(validator.validateCard(JCB)?"valid":"not valid"));
        System.out.println("CVV is "+(validator.validateCVV(JCB, CVV_JCB)?"valid": "not valid"));
        System.out.println();
        assert(validator.validateCard(JCB));
        assert(validator.validateCVV(JCB, CVV_JCB));
        
        System.out.println("Prueba de validación de tarjeta de crédito terminada");
    }
    
    private static void pruebaCuentaBancariaValidator(){
        int anio = 2020, mes = 12, dia=15;
        Date fechaExp = new GregorianCalendar(anio, mes-1, dia).getTime();
        Long numeroTarjeta = Long.parseLong("4760048064179840");
        CuentaBancaria cuenta = new CuentaBancaria(1, "BANAMEX", numeroTarjeta, 343, fechaExp);
        System.out.println("Validando cuenta bancaria...");
        System.out.println(cuenta);
        ValidatorResult result = CuentaBancariaValidator.validar(cuenta);
        if(result.isValid() && result.getError()==null){
            System.out.println("La cuenta bancaria es válida");
        }else{
            System.out.println(result.getError());
        }
        System.out.println("Validación terminada\n");
        
        int anio2 = 2020, mes2 = 04, dia2=01;
        Date fechaExp2 = new GregorianCalendar(anio2, mes2-1, dia2).getTime();
        Long numeroTarjeta2 = Long.parseLong("6011917547864412");
        CuentaBancaria cuenta2 = new CuentaBancaria(1, "BANAMEX", numeroTarjeta2, 343, fechaExp2);
        System.out.println("Validando cuenta bancaria...");
        System.out.println(cuenta2);
        ValidatorResult result2 = CuentaBancariaValidator.validar(cuenta2);
        if(result2.isValid()){
            System.out.println("La cuenta bancaria es válida");
        }else{
            System.out.println(result2.getError());
        }
        System.out.println("Validación terminada");
    }
    
}
