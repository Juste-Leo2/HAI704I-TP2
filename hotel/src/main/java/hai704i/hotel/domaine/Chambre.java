// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.domaine;

/**
 * La chambre vue par le métier interne de l'hôtel.
 * 
 * L'agence distante ne connaît pas cette classe :
 * elle ne voit que le devis final (OffreDTO) que l'hôtel lui propose.
 */

public record Chambre(String numero, int capacite, double prixParNuit, String description)
{
    public boolean peutAccueillir(int nbPersonnes) 
    {
        return this.capacite >= nbPersonnes;
    }
}
