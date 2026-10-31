// Fichier inspiré du projet date-demo (Bachar Rima)

package hai704i.agence.application;

import java.util.List;

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
    }
}
