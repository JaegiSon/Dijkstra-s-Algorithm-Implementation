/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jae_S
 */
public class NumberOutOfRangeException extends Exception{
    private String message = "NEGATIVE NUMBER not allowed, please re-enter value";
    public NumberOutOfRangeException() {
        super();
    }
    
    public String getMessage(){
        return this.message;
    }
    
}
