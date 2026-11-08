// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.agence.application;

/**
 * Une offre d'hébergement telle qu'une interface a besoin de l'afficher.
 */
public record OffreVue(String identifiant, String nomHotel, int nombreEtoiles,
                       String descriptionChambre, double prixTotal, String adresseHotel) {}

