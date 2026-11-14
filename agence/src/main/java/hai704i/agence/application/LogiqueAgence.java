// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.agence.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.xml.ws.WebServiceException;

import hai704i.hotel.engendre.AuthentificationEchouee_Exception;
import hai704i.hotel.engendre.CarteBancaireDTO;
import hai704i.hotel.engendre.ClientDTO;
import hai704i.hotel.engendre.ConfirmationReservationDTO;
import hai704i.hotel.engendre.DemandeReservation;
import hai704i.hotel.engendre.OffreDTO;
import hai704i.hotel.engendre.OffreIndisponible_Exception;
import hai704i.hotel.engendre.PaiementInvalide_Exception;

/**
 * La logique de l'agence : la seule classe qui connaisse SOAP.
 */
public class LogiqueAgence 
{
    private final List<String> adressesHotels = new ArrayList<>();
    private final String idAgence;
    private final String secretAgence;
    private final List<String> derniersAvertissements = new ArrayList<>();
    private final List<OffreVue> dernieresOffres = new ArrayList<>();

    public LogiqueAgence() 
    {
        this("agence_lille", "secret_lille_123");
    }

    public LogiqueAgence(String idAgence, String secretAgence) 
    {
        this.idAgence = idAgence;
        this.secretAgence = secretAgence;

        // Configuration par défaut des hôtels partenaires
        adressesHotels.add("http://localhost:8081/hotel");
        adressesHotels.add("http://localhost:8082/hotel");
    }

    public String getIdAgence() 
    {
        return idAgence;
    }

    public void ajouterHotel(String adresse) 
    {
        if (!adressesHotels.contains(adresse)) 
        {
            adressesHotels.add(adresse);
        }
    }

    public List<String> getDerniersAvertissements() 
    {
        return new ArrayList<>(derniersAvertissements);
    }

    public List<OffreVue> getDernieresOffres() 
    {
        return new ArrayList<>(dernieresOffres);
    }

    /**
     * Recherche les offres auprès de tous les hôtels partenaires connus,
     * gère la défaillance partielle, agrège et trie par prix croissant.
     */
    public List<OffreVue> rechercherOffres(String ville, String dateArrivee, String dateDepart, 
                                          int nbPersonnes, int etoilesMin, double prixMax) 
    {
        derniersAvertissements.clear();
        dernieresOffres.clear();

        for (int i = 0; i < adressesHotels.size(); i++) 
        {
            String adresse = adressesHotels.get(i);
            ClientHotel client = new ClientHotel(adresse);

            try 
            {
                List<OffreDTO> recues = client.rechercher(idAgence, ville, dateArrivee, dateDepart, nbPersonnes, etoilesMin, prixMax);
                for (int j = 0; j < recues.size(); j++) 
                {
                    OffreDTO o = recues.get(j);
                    dernieresOffres.add(new OffreVue(
                        o.getIdentifiant(),
                        o.getNomHotel(),
                        o.getNombreEtoiles(),
                        o.getDescriptionChambre(),
                        o.getPrixTotal(),
                        adresse
                    ));
                }
            } 
            catch (WebServiceException e) 
            {
                derniersAvertissements.add("Hôtel à l'adresse " + adresse + " injoignable.");
            } 
            catch (Exception e) 
            {
                derniersAvertissements.add("Erreur avec l'hôtel à l'adresse " + adresse + " : " + e.getMessage());
            }
        }

        // Tri des offres par prix croissant
        dernieresOffres.sort((o1, o2) -> Double.compare(o1.prixTotal(), o2.prixTotal()));

        return new ArrayList<>(dernieresOffres);
    }

    /**
     * Réserve une offre choisie en transmettant les informations requises à l'hôtel correspondant.
     */
    public ResultatReservation reserver(OffreVue offre, String nomClient, String prenomClient, 
                                        String numeroCarte, int moisExp, int anneeExp, String cvc) 
    {
        if (offre == null) 
        {
            return ResultatReservation.refus("Aucune offre sélectionnée.");
        }

        ClientHotel client = new ClientHotel(offre.adresseHotel());

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setNom(nomClient);
        clientDTO.setPrenom(prenomClient);

        CarteBancaireDTO carteDTO = new CarteBancaireDTO();
        carteDTO.setNumeroCarte(numeroCarte);
        carteDTO.setMoisExpiration(moisExp);
        carteDTO.setAnneeExpiration(anneeExp);
        carteDTO.setCvc(cvc);

        DemandeReservation demande = new DemandeReservation();
        demande.setIdDemande("DEM-" + UUID.randomUUID().toString().substring(0, 8));
        demande.setIdentifiantAgence(idAgence);
        demande.setSecretAgence(secretAgence);
        demande.setIdOffre(offre.identifiant());
        demande.setClient(clientDTO);
        demande.setCarte(carteDTO);

        try 
        {
            ConfirmationReservationDTO conf = client.reserver(demande);
            return ResultatReservation.ok(conf.getReferenceReservation(), conf.getMessage());
        } 
        catch (AuthentificationEchouee_Exception e) 
        {
            return ResultatReservation.refus("Authentification refusée : " + e.getMessage());
        } 
        catch (OffreIndisponible_Exception e) 
        {
            return ResultatReservation.refus("Offre indisponible : " + e.getMessage());
        } 
        catch (PaiementInvalide_Exception e) 
        {
            return ResultatReservation.refus("Paiement refusé : " + e.getMessage());
        } 
        catch (WebServiceException e) 
        {
            return ResultatReservation.refus("Hôtel injoignable lors de la réservation : " + e.getMessage());
        } 
        catch (Exception e) 
        {
            return ResultatReservation.refus("Erreur imprévue : " + e.getMessage());
        }
    }
}

