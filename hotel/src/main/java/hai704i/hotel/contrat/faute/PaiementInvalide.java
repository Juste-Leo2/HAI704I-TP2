// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.faute;

import jakarta.xml.ws.WebFault;

@WebFault(name = "PaiementInvalide",
          targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public class PaiementInvalide extends Exception 
{
    private static final long serialVersionUID = 1L;

    public PaiementInvalide(String message) 
    {
        super(message);
    }
}
