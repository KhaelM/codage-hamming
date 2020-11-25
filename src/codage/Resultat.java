/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codage;

/**
 *
 * @author miker
 */
public class Resultat {
    char caractere;
    String binaire;
    String hamming;
    int positionErreur;
    int[] motDeCode;
    
    public Resultat() {}
    
    public Resultat(Resultat autre) {
        this.caractere = autre.caractere;
        this.binaire = autre.binaire;
        this.hamming = autre.hamming;
        this.positionErreur = autre.positionErreur;
        this.motDeCode = new int[Hamming.n];
        for(int i = 0; i < autre.getMotDeCode().length; i++) {
            this.motDeCode[i] = autre.getMotDeCode()[i];
        }
    }

    public int[] getMotDeCode() {
        return motDeCode;
    }

    public void setMotDeCode(int[] motDeCode) {
        this.motDeCode = motDeCode;
    }

    public char getCaractere() {
        return Hamming.decoder(motDeCode);
    }

    public void setCaractere(char caractere) {
        this.caractere = caractere;
    }

    public String getBinaire() {
        return Hamming.getRepBinaire(motDeCode);
    }

    public void setBinaire(String binaire) {
        this.binaire = binaire;
    }

    public String getHamming() {
        return Hamming.getMotDeCode(motDeCode);
    }

    public void setHamming(String hamming) {
        this.hamming = hamming;
    }

    public int getPositionErreur() {
        return positionErreur;
    }

    public void setPositionErreur(int positionErreur) {
        this.positionErreur = positionErreur;
    }
}
