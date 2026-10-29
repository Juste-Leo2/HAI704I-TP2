// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.domaine;

/**
 * Offre vue par le métier interne de l'hôtel.
 */

public record Offre(String identifiant, Chambre chambre, double prixTotal, int nombreNuits) {}
