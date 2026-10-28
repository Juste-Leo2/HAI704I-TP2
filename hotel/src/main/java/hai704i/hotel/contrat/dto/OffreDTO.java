// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Une offre d'agence tel qu'il traverse le réseau.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OffreDTO {

    private String identifiant;
    private String nomHotel;
    private int nombreEtoiles;
    private String descriptionChambre;
    private double prixTotal;

    /** Exigé par JAXB, qui construit l'objet vide avant de le remplir. */
    public OffreDTO() { }

    public OffreDTO(String identifiant, String nomHotel, int nombreEtoiles,
                   String descriptionChambre, double prixTotal) {
        this.identifiant = identifiant;
        this.nomHotel = nomHotel;
        this.nombreEtoiles = nombreEtoiles;
        this.descriptionChambre = descriptionChambre;
        this.prixTotal = prixTotal;
    }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String v) { this.identifiant = v; }
    public String getNomHotel() { return nomHotel; }
    public void setNomHotel(String v) { this.nomHotel = v; }
    public int getNombreEtoiles() { return nombreEtoiles; }
    public void setNombreEtoiles(int v) { this.nombreEtoiles = v; }
    public String getDescriptionChambre() { return descriptionChambre; }
    public void setDescriptionChambre(String v) { this.descriptionChambre = v; }
    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double v) { this.prixTotal = v; }
}
