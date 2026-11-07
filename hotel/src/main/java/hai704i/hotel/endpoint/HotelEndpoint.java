// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.endpoint;

import java.util.ArrayList;
import java.util.List;

import jakarta.jws.WebService;

import hai704i.hotel.contrat.HotelPort;
import hai704i.hotel.contrat.dto.ConfirmationReservationDTO;
import hai704i.hotel.contrat.dto.DemandeRecherche;
import hai704i.hotel.contrat.dto.DemandeReservation;
import hai704i.hotel.contrat.dto.OffreDTO;
import hai704i.hotel.contrat.faute.AgenceNonReconnue;
import hai704i.hotel.contrat.faute.AuthentificationEchouee;
import hai704i.hotel.contrat.faute.OffreIndisponible;
import hai704i.hotel.contrat.faute.PaiementInvalide;
import hai704i.hotel.domaine.Offre;
import hai704i.hotel.domaine.Reservation;
import hai704i.hotel.service.ServiceHotel;

/**
 * Adaptateur entre le contrat SOAP et le métier.
 */
@WebService(endpointInterface = "hai704i.hotel.contrat.HotelPort",
            serviceName = "HotelService",
            portName = "HotelSoapPort",
            targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public class HotelEndpoint implements HotelPort 
{
    private final ServiceHotel service;

    public HotelEndpoint(ServiceHotel service) 
    {
        this.service = service;
    }

    @Override
    public List<OffreDTO> rechercherOffres(DemandeRecherche demande) throws AgenceNonReconnue 
    {
        if (!service.estPartenaire(demande.getIdentifiantAgence())) 
        {
            throw new AgenceNonReconnue("L'agence '" + demande.getIdentifiantAgence() + "' n'est pas partenaire de cet hôtel.");
        }

        List<OffreDTO> resultats = new ArrayList<>();
        List<Offre> offresMetier = service.rechercher(
            demande.getIdentifiantAgence(),
            demande.getVille(),
            demande.getDateArrivee(),
            demande.getDateDepart(),
            demande.getNombrePersonnes(),
            demande.getEtoilesMin(),
            demande.getPrixMax()
        );

        for (int i = 0; i < offresMetier.size(); i++) 
        {
            Offre o = offresMetier.get(i);
            resultats.add(new OffreDTO(
                o.identifiant(),
                service.getNomHotel(),
                service.getEtoiles(),
                o.chambre().description(),
                o.prixTotal()
            ));
        }
        return resultats;
    }

    @Override
    public ConfirmationReservationDTO reserver(DemandeReservation demande) 
        throws AuthentificationEchouee, OffreIndisponible, PaiementInvalide 
    {
        String nomClient = demande.getClient() != null ? demande.getClient().getNom() : "";
        String prenomClient = demande.getClient() != null ? demande.getClient().getPrenom() : "";

        String numCarte = demande.getCarte() != null ? demande.getCarte().getNumeroCarte() : "";
        int moisExp = demande.getCarte() != null ? demande.getCarte().getMoisExpiration() : 0;
        int anneeExp = demande.getCarte() != null ? demande.getCarte().getAnneeExpiration() : 0;
        String cvc = demande.getCarte() != null ? demande.getCarte().getCvc() : "";

        Reservation res = service.reserver(
            demande.getIdDemande(),
            demande.getIdentifiantAgence(),
            demande.getSecretAgence(),
            demande.getIdOffre(),
            nomClient,
            prenomClient,
            numCarte,
            moisExp,
            anneeExp,
            cvc
        );

        return new ConfirmationReservationDTO(
            res.reference(),
            res.idDemande(),
            service.getNomHotel(),
            res.prixTotal(),
            "Reservation confirmee pour " + res.nomClient() + " (Carte: " + res.carteMasquee() + ")"
        );
    }
}
