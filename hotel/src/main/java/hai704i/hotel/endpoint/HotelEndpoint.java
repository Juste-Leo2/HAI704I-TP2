// Fichier adapté du projet employe-demo (Bachar Rima)

package hai704i.hotel.endpoint;

import java.util.ArrayList;
import java.util.List;

import jakarta.jws.WebService;

import hai704i.hotel.contrat.HotelPort;
import hai704i.hotel.contrat.dto.DemandeRecherche;
import hai704i.hotel.contrat.dto.OffreDTO;
import hai704i.hotel.contrat.faute.AgenceNonReconnue;
import hai704i.hotel.domaine.Offre;
import hai704i.hotel.service.ServiceHotel;


/**
 * Adaptateur entre le contrat SOAP et le métier.
 *
 * <p>Il fait trois choses :
 * (i) traduire les types du contrat vers le domaine, 
 * (ii) déléguer au service, 
 * (iii) puis traduire les refus métier en fautes déclarées.
 * 
 * C'est la raison d'être de cette couche.
 * Aucune règle de gestion, aucun accès aux données.
 *
 * <p>C'est la seule classe du serveur à jeter
 * le jour où l'on exposerait la même application en REST.
 * Le service, le domaine et les dépôts sont inchangés.
 *
 * <p>Cette classe porte {@code serviceName} et {@code portName} explicitement.
 * Sans eux, CXF les déduirait du nom de la classe d'implémentation, et
 * renommer {@code EmployeEndpoint} changerait le contrat publié.
 */
@WebService(endpointInterface = "hai704i.hotel.contrat.HotelPort",
            serviceName = "HotelService",
            portName = "HotelSoapPort",
            targetNamespace = "http://hotel.hai704i.umontpellier.fr/")
public class HotelEndpoint implements HotelPort {

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
            // on lève une erreur si l'agence n'est pas partenaire
            throw new AgenceNonReconnue("L'agence '" + demande.getIdentifiantAgence() + "' n'est pas partenaire de cet hôtel.");
        }



        List<OffreDTO> resultats = new ArrayList<>();
        List<Offre> offresMetier = service.rechercher(
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

}
