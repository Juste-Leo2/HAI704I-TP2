// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.domaine;

/**
 * Réservation vue par le métier interne de l'hôtel.
 * Règle stricte : Le CVC n'est JAMAIS stocké, et le numéro de carte est masqué.
 */
public record Reservation(String reference, String idDemande, String idOffre, 
                          String nomClient, double prixTotal, String carteMasquee) 
{
}
