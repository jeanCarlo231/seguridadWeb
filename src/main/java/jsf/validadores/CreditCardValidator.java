/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *
 * @author jcami
 */
public class CreditCardValidator {
    
    HashMap<String, String> tarjetasValidas;
    private final String regexThreeDigits = "^\\d{3}$";
    private final String regexFourDigits = "^\\d{4}$";
    
    public CreditCardValidator(){
        tarjetasValidas = new HashMap<>();
        tarjetasValidas.put("visa", "^4[0-9]{12}(?:[0-9]{3})?$");
        tarjetasValidas.put("mastercard","^5[1-5][0-9]{14}$|^2(?:2(?:2[1-9]|[3-9][0-9])|[3-6][0-9][0-9]|7(?:[01][0-9]|20))[0-9]{12}$");
        tarjetasValidas.put("amex","^3[47][0-9]{13}$");
        tarjetasValidas.put("discover","^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$");
        tarjetasValidas.put("diners_club","^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
        tarjetasValidas.put("jcb","^(?:2131|1800|35[0-9]{3})[0-9]{11}$");
    }
    
    public boolean validateCard(String value){
        int sum=0;
        boolean shouldDouble = false;
        for(int i=value.length()-1; i>=0; i--){
            int digit = Character.getNumericValue(value.charAt(i));
            //System.out.println(digit);
            if (shouldDouble) {
                if ((digit *= 2) > 9) digit -= 9;
            }
            sum += digit;
            shouldDouble = !shouldDouble;
        }
        boolean valid = (sum % 10) == 0;
        boolean accepted = false;
        // loop through the keys (visa, mastercard, amex, etc.)
        for(String key: tarjetasValidas.keySet()){
            String regex = tarjetasValidas.get(key);
            if(Pattern.matches(regex, value)){
                accepted=true;
            }                
        }
        //System.out.println("Valid:"+valid+"\nAccepted:"+accepted);
        return valid && accepted;
    }
    
    public boolean validateCVV(String creditCard, String cvv){
        if(Pattern.matches(tarjetasValidas.get("amex"), creditCard)){
            if(Pattern.matches(regexThreeDigits, cvv) || Pattern.matches(regexFourDigits, cvv)){ //american express cvv has 3-4 digits
                return true;
            }
        }else if(Pattern.matches(regexThreeDigits, cvv)){
            return true;
        }
        return false;
    }
}


