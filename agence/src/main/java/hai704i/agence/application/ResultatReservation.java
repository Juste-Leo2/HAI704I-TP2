// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.agence.application;

/**
 * Issue d'une réservation : soit elle aboutit avec succès, soit elle est refusée avec une phrase affichable.
*/
public record ResultatReservation(boolean accepte, String referenceReservation, String message, String refus) 
{
    public static ResultatReservation ok(String referenceReservation, String message) 
    {
        return new ResultatReservation(true, referenceReservation, message, null);
    }

    public static ResultatReservation refus(String messageErreur) 
    {
        return new ResultatReservation(false, null, null, messageErreur);
    }
}

