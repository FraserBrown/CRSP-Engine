package org.crspengine;

public class main {
    /***
     * Instantiate CRSP-Engine and UI
     * @param args
     */
    public static void main(String [] args){
        CRSPEngine cre = new CRSPEngine();
        UserInterface ui = new UserInterface(cre);
    }
}
