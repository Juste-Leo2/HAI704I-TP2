// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Ce qu'un client envoie à l'hotel.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DemandeRecherche {

    private String ville;
    private String dateArrivee;
    private String dateDepart;
    private int nombrePersonnes;
    private int etoilesMin;
    private double prixMax;

    public DemandeRecherche() { }

    public DemandeRecherche(String ville, String dateArrivee, String dateDepart,
                           int nombrePersonnes, int etoilesMin, double prixMax) {
        this.ville = ville;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombrePersonnes = nombrePersonnes;
        this.etoilesMin = etoilesMin;
        this.prixMax = prixMax;
    }

    public String getVille() { return ville; }
    public void setVille(String v) { this.ville = v; }
    public String getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(String v) { this.dateArrivee = v; }
    public String getDateDepart() { return dateDepart; }
    public void setDateDepart(String v) { this.dateDepart = v; }
    public int getNombrePersonnes() { return nombrePersonnes; }
    public void setNombrePersonnes(int v) { this.nombrePersonnes = v; }
    public int getEtoilesMin() { return etoilesMin; }
    public void setEtoilesMin(int v) { this.etoilesMin = v; }
    public double getPrixMax() { return prixMax; }
    public void setPrixMax(double v) { this.prixMax = v; }
}
