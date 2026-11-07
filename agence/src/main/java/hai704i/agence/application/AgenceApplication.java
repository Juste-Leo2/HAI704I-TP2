// Fichier inspiré du projet date-demo (Bachar Rima)

package hai704i.agence.application;

import java.util.List;

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

        ClientHotel client = new ClientHotel(adresseHotel);

        String idAgence = "agence_lille";
        String secretAgence = "secret_lille_123";
        String ville = "Montpellier";
        String arrivee = "10-11-2026";
        String depart = "15-11-2026";
        int nbPersonnes = 2;
        int etoilesMin = 3;
        double prixMax = 500.0;

        List<OffreDTO> offres = client.rechercher(idAgence, ville, arrivee, depart, nbPersonnes, etoilesMin, prixMax);

        System.out.println("Nombre d'offres recues : " + offres.size());

        for (int i = 0; i < offres.size(); i++) 
        {
            OffreDTO o = offres.get(i);
            System.out.println("Offre " + (i + 1) + " : " + o.getIdentifiant() + " - " + o.getNomHotel() + " (" + o.getNombreEtoiles() + " etoiles) - " + o.getDescriptionChambre() + " - " + o.getPrixTotal() + " EUROS");
        }

        // Reservation de la premiere offre si disponible
        if (offres.size() > 0) 
        {
            OffreDTO choix = offres.get(0);

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
            ConfirmationReservationDTO conf = client.reserver(demande);
            System.out.println("Reservation confirmee : Ref=" + conf.getReferenceReservation() + " | " + conf.getMessage() + " | Prix=" + conf.getPrixTotal() + " EUROS");

            // Test d'idempotence avec la meme demande
            System.out.println("\n- Test Idempotence");
            ConfirmationReservationDTO rejeu = client.reserver(demande);
            System.out.println("Reponse idempotence : Ref=" + rejeu.getReferenceReservation() );

            // Test avec un mauvais mot de passe
            System.out.println("\n- Test Refus Authentification");
            demande.setIdDemande("DEMANDE-002");
            demande.setSecretAgence("mauvais_secret");

            try 
            {
                client.reserver(demande);
            } 
            catch (Exception e) 
            {
                System.out.println("Refus recu de l'hotel : " + e.getMessage());
            }
        }
    }
}
