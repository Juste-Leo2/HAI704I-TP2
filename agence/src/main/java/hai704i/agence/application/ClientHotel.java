// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.agence.application;

import java.util.List;

import jakarta.xml.ws.BindingProvider;

import hai704i.hotel.engendre.DemandeRecherche;
import hai704i.hotel.engendre.HotelPort;
import hai704i.hotel.engendre.HotelService;
import hai704i.hotel.engendre.OffreDTO;

public class ClientHotel 
{
    private final String adresse;

    public ClientHotel(String adresse) 
    {
        this.adresse = adresse;
    }
    
    // get
    public String getAdresse() 
    {
        return adresse;
    }

    private HotelPort creerProxy() 
    {
        HotelService service = new HotelService();
        HotelPort port = service.getHotelSoapPort();

        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.adresse);
        bp.getRequestContext().put("com.sun.xml.ws.connect.timeout", 3000);
        bp.getRequestContext().put("com.sun.xml.ws.request.timeout", 5000);

        return port;
    }

    public List<OffreDTO> rechercher(String idAgence, String ville, String dateArrivee, 
                                     String dateDepart, int nbPersonnes, int etoilesMin, double prixMax) throws Exception 
    {
        DemandeRecherche demande = new DemandeRecherche();
        demande.setIdentifiantAgence(idAgence);
        demande.setVille(ville);
        demande.setDateArrivee(dateArrivee);
        demande.setDateDepart(dateDepart);
        demande.setNombrePersonnes(nbPersonnes);
        demande.setEtoilesMin(etoilesMin);
        demande.setPrixMax(prixMax);

        HotelPort proxy = creerProxy();
        return proxy.rechercherOffres(demande);
    }
}
