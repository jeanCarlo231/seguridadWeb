/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validadores;

/**
 *
 * @author jcami
 */
public class ValidatorResult {
    
    private String error;
    private boolean valid=true;

    public ValidatorResult(String error, boolean valid) {
        this.error = error;
        this.valid = valid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
}
