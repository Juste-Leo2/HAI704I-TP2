// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat;

import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import hai704i.hotel.contrat.dto.ConfirmationReservationDTO;
import hai704i.hotel.contrat.dto.DemandeRecherche;
import hai704i.hotel.contrat.dto.DemandeReservation;
import hai704i.hotel.contrat.dto.OffreDTO;
import hai704i.hotel.contrat.faute.AgenceNonReconnue;
import hai704i.hotel.contrat.faute.AuthentificationEchouee;
import hai704i.hotel.contrat.faute.OffreIndisponible;
import hai704i.hotel.contrat.faute.PaiementInvalide;

/**
 * Contrat du service Hotel : la seule chose que le client connaîtra.
 */
@WebService(name = "HotelPort",
            targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public interface HotelPort 
{
    @WebMethod
    @WebResult(name = "offre")
    List<OffreDTO> rechercherOffres(@WebParam(name = "demande") DemandeRecherche demande)
        throws AgenceNonReconnue;

    @WebMethod
    @WebResult(name = "confirmation")
    ConfirmationReservationDTO reserver(@WebParam(name = "demande") DemandeReservation demande)
        throws AuthentificationEchouee, OffreIndisponible, PaiementInvalide;
}
