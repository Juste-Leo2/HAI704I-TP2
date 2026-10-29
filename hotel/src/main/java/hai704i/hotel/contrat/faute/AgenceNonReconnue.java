// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.faute;

import jakarta.xml.ws.WebFault;

/**
 * Aucune agence avec l'ID fourni n'a été trouvé.
 */
@WebFault(name = "AgenceNonReconnue",
          targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public class AgenceNonReconnue extends Exception  {

    private static final long serialVersionUID = 1L;

    public AgenceNonReconnue(String message)  { super(message); }
}
