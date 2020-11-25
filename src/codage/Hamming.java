package codage;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Hamming {

    public static int n = 12; // longueur d'un mot de code
    public static int k = 8; // longueur d'un mot d'information
    private static int quelconqueValeur = 9;

    public static void main(String args[]) throws UnsupportedEncodingException, CloneNotSupportedException {
        String m = "Codage";
        byte[] bytes = m.getBytes("UTF-8");
        
//        int motDeCode[] = encoder(bytes[0]);
        Resultat[] envois = new Resultat[bytes.length];
        System.out.println("========== Envoyé:");
        int i = 0;
        for (i = 0; i < bytes.length; i++) {
            Resultat envoi = encoderResultat(bytes[i]);
            System.out.println(envoi.getCaractere() + " | " + envoi.getBinaire() + " | " + envoi.getHamming());
            envois[i] = envoi;
        }
        System.out.println("========== Envoyé: " + getMotFinal(envois));

        Resultat[] receptions = new Resultat[bytes.length];
        System.out.println("========== Réception:");
        for (i = 0; i < envois.length; i++) {
            Resultat reception = new Resultat(envois[i]);
            int positions[] = new int[genererTailleErreur()];
            for(int j = 0; j < positions.length; j++) {
                positions[j] = genererPositionErreur();
            }
            insererErreur(reception.getMotDeCode(), positions);
            System.out.println(reception.getCaractere() + " | " + reception.getBinaire() + " | " + reception.getHamming());
            receptions[i] = reception;
        }
        System.out.println("========== Réception: " + getMotFinal(receptions));

        Resultat[] corrections = new Resultat[bytes.length];
        System.out.println("========== Correction:");
        for (i = 0; i < receptions.length; i++) {
            Resultat correction = new Resultat(receptions[i]);
            correction.setPositionErreur(trouverPositionErreur(correction.getMotDeCode()));
            corriger(correction.getMotDeCode());
            System.out.println(correction.getCaractere() + " | " + correction.getBinaire() + " | " + correction.getHamming() + " | " + correction.getPositionErreur());
            corrections[i] = correction;
        }
        System.out.println("========== Correction: " + getMotFinal(corrections));

    }
    
    public static int genererTailleErreur() {
        Random r = new Random();
        int low = 1;
        int high = 3;
        int result = r.nextInt(high-low) + low;
        return result;
    }
    
    public static int genererPositionErreur() {
        Random r = new Random();
        int low = 1;
        int high = 9;
        int result = r.nextInt(high-low) + low;
        return result;
    }

    public static String getMotFinal(Resultat[] resultats) {
        String resultat = new String();
        for (Resultat r : resultats) {
            resultat += r.getCaractere();
        }
        return resultat;
    }

    public static String getRepBinaire(int[] motDeCode) {
        String binaryString = new String();
        binaryString += motDeCode[2];
        binaryString += motDeCode[4];
        binaryString += motDeCode[5];
        binaryString += motDeCode[6];
        binaryString += motDeCode[8];
        binaryString += motDeCode[9];
        binaryString += motDeCode[10];
        binaryString += motDeCode[11];
        return binaryString;
    }

    public static char decoder(int[] motDeCode) {
        int b = Integer.parseInt(getRepBinaire(motDeCode), 2);
        return (char) b;
    }

    public static int[] corriger(int[] motDeCode) {
        int posErreur = trouverPositionErreur(motDeCode);
        if (posErreur != 0 && posErreur != -1) {
            motDeCode[posErreur - 1] = (motDeCode[posErreur - 1] + 1) % 2;
        }
        return motDeCode;
    }

    public static int trouverPositionErreur(int[] motDeCode) {
        boolean e1 = (motDeCode[0] + motDeCode[2] + motDeCode[4] + motDeCode[6] + motDeCode[8] + motDeCode[10]) % 2 == 0;
        boolean e2 = (motDeCode[1] + motDeCode[2] + motDeCode[5] + motDeCode[6] + motDeCode[9] + motDeCode[10]) % 2 == 0;
        boolean e3 = (motDeCode[3] + motDeCode[4] + motDeCode[5] + motDeCode[6] + motDeCode[11]) % 2 == 0;
        boolean e4 = (motDeCode[7] + motDeCode[8] + motDeCode[9] + motDeCode[10] + motDeCode[11]) % 2 == 0;

        if (e1 && e2 && e3 && e4) {
            return -1;
        }

        if (!e1 && e2 && e3 && e4) {
            return 0;
        } else if (!e2 && e1 && e3 && e4) {
            return 2;
        } else if (!e1 && !e2 && e3 && e4) {
            return 3;
        } else if (!e3 && e1 && e2 && e4) {
            return 4;
        } else if (!e1 && !e3 && e2 && e4) {
            return 5;
        } else if (!e2 && !e3 && e1 && e4) {
            return 6;
        } else if (!e1 && !e2 && !e3 && e4) {
            return 7;
        } else if (!e4 && e1 && e2 && e3) {
            return 8;
        } else if (!e1 && !e4 && e2 && e3) {
            return 9;
        } else if (!e2 && !e4 && e1 && e3) {
            return 10;
        } else if (!e1 && !e2 && !e4 && e3) {
            return 11;
        } else if (!e3 && !e4 && e1 && e2) {
            return 12;
        } else {
            return -1;
//            throw new RuntimeException("N'importe quoi");
        }
    }

    public static int[] insererErreur(int[] motDeCode, int[] positions) {
        for (int position : positions) {
            motDeCode[position - 1] = (motDeCode[position - 1] + 1) % 2;
        }
        return motDeCode;
    }

    public static Resultat encoderResultat(byte b) {
        Resultat resultat = new Resultat();
        int motDeCode[] = encoder(b);
        resultat.setMotDeCode(motDeCode);
        resultat.setBinaire(getRepBinaire(motDeCode));
        String hamming = new String();
        for (int i : motDeCode) {
            hamming += i;
        }
        resultat.setHamming(hamming);
        return resultat;
    }

    public static String getMotDeCode(int[] motDeCode) {
        String hamming = new String();
        for (int i : motDeCode) {
            hamming += i;
        }
        return hamming;
    }

    static int[] encoder(byte b) {
        int motInfo[] = convertirByteEnTabEntier(b);
//        System.out.println();
        int motDeCode[] = new int[n];
        int nbParite = 0;
        int positionMotInfo = 0;
        for (int position = 1; position <= n; position++) {
            if (Math.pow(2, nbParite) == position) {
                motDeCode[position - 1] = quelconqueValeur;
                nbParite++;
            } else {
                motDeCode[position - 1] = motInfo[positionMotInfo++];
            }
        }

        for (int indiceParite = 0; indiceParite < nbParite; indiceParite++) {
            motDeCode[(int) Math.pow(2, indiceParite) - 1] = obtenirParite(motInfo, indiceParite);
        }
//        afficherMot(motDeCode, n);
//        System.out.println();
        return motDeCode;
    }

    static int[] convertirByteEnTabEntier(byte b) {
        int motInfo[] = new int[k];
        String binaryRep = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
        for (int i = 0; i < k; i++) {
            motInfo[i] = Integer.parseInt(String.valueOf(binaryRep.charAt(i)));
        }
//        System.out.print((char) b + " = ");
//        afficherMot(motInfo, k);
        return motInfo;
    }

    static void afficherMot(int[] tableau, int taille) {
        for (int i = 0; i < taille; i++) {
            System.out.print(tableau[i]);
        }
    }

    private static int obtenirParite(int[] motInfo, int indiceParite) {
        int parite = 0;
        switch (indiceParite) {
            case 0:
                parite = motInfo[0] + motInfo[1] + motInfo[3] + motInfo[4] + motInfo[6];
                break;
            case 1:
                parite = motInfo[0] + motInfo[2] + motInfo[3] + motInfo[5] + motInfo[6];
                break;
            case 2:
                parite = motInfo[1] + motInfo[2] + motInfo[3] + motInfo[7];
                break;
            case 3:
                parite = motInfo[4] + motInfo[5] + motInfo[6] + motInfo[7];
                break;
            default:
                throw new RuntimeException("Indice parite incorrect:" + indiceParite);
        }
        return parite % 2;
    }
}
