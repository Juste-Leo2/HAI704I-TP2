// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat;

import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import hai704i.hotel.contrat.dto.DemandeRecherche;
import hai704i.hotel.contrat.dto.OffreDTO;


/**
 * Contrat du service Hotel : la seule chose que le client connaîtra.
 */
@WebService(name = "HotelPort",
            targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public interface HotelPort {

    @WebMethod
    @WebResult(name = "offre")
    List<OffreDTO> rechercherOffres(@WebParam(name = "demande") DemandeRecherche demande);

}
