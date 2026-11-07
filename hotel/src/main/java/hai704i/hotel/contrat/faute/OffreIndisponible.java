// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.faute;

import jakarta.xml.ws.WebFault;

@WebFault(name = "OffreIndisponible",
          targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public class OffreIndisponible extends Exception 
{
    private static final long serialVersionUID = 1L;

    public OffreIndisponible(String message) 
    {
        super(message);
    }
}
