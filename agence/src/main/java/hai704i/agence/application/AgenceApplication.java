// Fichier inspiré du projet date-demo (Bachar Rima)

package hai704i.agence.application;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.ws.WebServiceException;

import hai704i.hotel.engendre.CarteBancaireDTO;
import hai704i.hotel.engendre.ClientDTO;
import hai704i.hotel.engendre.ConfirmationReservationDTO;
import hai704i.hotel.engendre.DemandeReservation;
import hai704i.hotel.engendre.OffreDTO;

public class AgenceApplication 
{
    public static void main(String[] args) throws Exception 
    {
        String adresseHotel = "http://localhost:8081/hotel";
        if (args.length > 0) 
        {
            adresseHotel = args[0];
        }

        System.out.println("Adresse de l'hotel : " + adresseHotel);

        // Liste des adresses des hôtels partenaires
        List<String> adressesHotels = new ArrayList<>();
        adressesHotels.add(adresseHotel);
        adressesHotels.add("http://localhost:8082/hotel");

        String idAgence = "agence_lille";
        String secretAgence = "secret_lille_123";
        String ville = "Montpellier";
        String arrivee = "10-11-2026";
        String depart = "15-11-2026";
        int nbPersonnes = 2;
        int etoilesMin = 3;
        double prixMax = 1000.0;

        List<OffreDTO> toutesLesOffres = new ArrayList<>();
        List<ClientHotel> clientsHotels = new ArrayList<>();

        System.out.println("- Recherche aupres des hotels partenaires");

        for (int h = 0; h < adressesHotels.size(); h++) 
        {
            String adr = adressesHotels.get(h);
            ClientHotel client = new ClientHotel(adr);

            try 
            {
                List<OffreDTO> offresHotel = client.rechercher(idAgence, ville, arrivee, depart, nbPersonnes, etoilesMin, prixMax);
                System.out.println("Reponse de " + adr + " : " + offresHotel.size() + " offre(s) trouvee(s)");

                for (int i = 0; i < offresHotel.size(); i++) 
                {
                    toutesLesOffres.add(offresHotel.get(i));
                    clientsHotels.add(client);
                }
            } 
            catch (WebServiceException e) 
            {
                // Gestion de la defaillance partielle : l'hotel eteint ne fait pas planter l'agence
                System.out.println("Avertissement : Hotel a l'adresse " + adr + " injoignable.");
            }
        }

        // Tri des offres par prix croissant
        toutesLesOffres.sort((o1, o2) -> Double.compare(o1.getPrixTotal(), o2.getPrixTotal()));

        System.out.println("\nTotal des offres agregees et triees : " + toutesLesOffres.size());
        for (int i = 0; i < toutesLesOffres.size(); i++) 
        {
            OffreDTO o = toutesLesOffres.get(i);
            System.out.println("Offre " + (i + 1) + " : " + o.getIdentifiant() + " - " + o.getNomHotel() + " (" + o.getNombreEtoiles() + " etoiles) - " + o.getDescriptionChambre() + " - " + o.getPrixTotal() + " EUROS");
        }

        // Verification de la tarification personnalisee selon l'agence (Point de controle F)
        if (clientsHotels.size() > 0 && toutesLesOffres.size() > 0) 
        {
            ClientHotel premierClient = clientsHotels.get(0);
            List<OffreDTO> offresParis = premierClient.rechercher("agence_paris", ville, arrivee, depart, nbPersonnes, etoilesMin, prixMax);
            if (offresParis.size() > 0) 
            {
                System.out.println("\n- Comparaison des tarifs selon l'agence partenaire :");
                System.out.println("Chambre (" + offresParis.get(0).getDescriptionChambre() + ") pour agence_lille (-10%) : " + toutesLesOffres.get(0).getPrixTotal() + " EUROS");
                System.out.println("Chambre (" + offresParis.get(0).getDescriptionChambre() + ") pour agence_paris (-20%) : " + offresParis.get(0).getPrixTotal() + " EUROS");
            }
        }

        //////////////////////// A5 : Reservation, Idempotence, Securite ////////
        if (toutesLesOffres.size() > 0) 
        {
            OffreDTO choix = toutesLesOffres.get(0);
            ClientHotel clientCible = clientsHotels.get(0);

            ClientDTO voyageur = new ClientDTO();
            voyageur.setNom("Dupont");
            voyageur.setPrenom("Pierre");

            CarteBancaireDTO carte = new CarteBancaireDTO();
            carte.setNumeroCarte("1234567890123456");
            carte.setMoisExpiration(12);
            carte.setAnneeExpiration(2027);
            carte.setCvc("123");

            // Creation de la demande de reservation
            DemandeReservation demande = new DemandeReservation();
            demande.setIdDemande("DEMANDE-001");
            demande.setIdentifiantAgence(idAgence);
            demande.setSecretAgence(secretAgence);
            demande.setIdOffre(choix.getIdentifiant());
            demande.setClient(voyageur);
            demande.setCarte(carte);

            System.out.println("\n- Test Reservation");
            ConfirmationReservationDTO conf = clientCible.reserver(demande);
            System.out.println("Reservation confirmee : Ref=" + conf.getReferenceReservation() + " | " + conf.getMessage() + " | Prix=" + conf.getPrixTotal() + " EUROS");

            // Test d'idempotence avec la meme demande
            System.out.println("\n- Test Idempotence");
            ConfirmationReservationDTO rejeu = clientCible.reserver(demande);
            System.out.println("Reponse idempotence : Ref=" + rejeu.getReferenceReservation());

            // Test avec un mauvais mot de passe
            System.out.println("\n- Test Refus Authentification");
            demande.setIdDemande("DEMANDE-002");
            demande.setSecretAgence("mauvais_secret");

            try 
            {
                clientCible.reserver(demande);
            } 
            catch (Exception e) 
            {
                System.out.println("Refus recu de l'hotel : " + e.getMessage());
            }
        }
    }
}
