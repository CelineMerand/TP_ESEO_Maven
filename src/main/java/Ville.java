/**
 * @author Céline MERAND
 * Created on 28/11/2018
 */
public class Ville {

    private String codeCommune;
    private String nomCommune;
    private String codePostale;
    private String libelleAcheminement;
    private String ligne5;
    private String coordonnees;

    public Ville(String[] ville) {
        this.codeCommune = ville[0];
        this.nomCommune = ville[1];
        this.codePostale = ville[2];
        this.libelleAcheminement = ville[3];
        this.ligne5 = ville[4];
        this.coordonnees = ville[5];
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public String getLibelleAcheminement() {
        return libelleAcheminement;
    }

    public String getLigne5() {
        return ligne5;
    }

    public String getLatitude() {
        return coordonnees == null || coordonnees.split(",").length != 2 ? "0" : coordonnees
                .split(",")[0].replaceAll(" ", "");
    }

    public String getLongitude() {
        return coordonnees == null || coordonnees.split(",").length != 2 ? "0" : coordonnees
                .split(",")[1].replaceAll(" ", "");
    }
}
