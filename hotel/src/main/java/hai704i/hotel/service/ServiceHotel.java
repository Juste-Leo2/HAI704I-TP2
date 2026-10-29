package hai704i.hotel.service;

import java.util.ArrayList;
import java.util.List;

import hai704i.hotel.depot.HotelDepot;
import hai704i.hotel.domaine.Chambre;
import hai704i.hotel.domaine.Offre;

public class ServiceHotel 
{
    private final HotelDepot depot;
    private final String nomHotel;
    private final int etoiles;
    private final String ville;
    private final List<String> agencesAutorisees;

    public ServiceHotel(HotelDepot depot, String nomHotel, int etoiles, String ville) 
    {
        this.depot = depot;
        this.nomHotel = nomHotel;
        this.etoiles = etoiles;
        this.ville = ville;

        // Liste des agences partenaires (autorisées)
        this.agencesAutorisees = new ArrayList<>();
        this.agencesAutorisees.add("agence_lille");
        this.agencesAutorisees.add("agence_paris");
    }

    // get
    public String getNomHotel() 
    {
        return nomHotel;
    }

    public int getEtoiles() 
    {
        return etoiles;
    }

    public String getVille() 
    {
        return ville;
    }

    public HotelDepot getDepot() 
    {
        return depot;
    }

    /**
     * Recherche les offres disponibles selon les critères reçus.
     */
    public List<Offre> rechercher(String villeDemandee, String dateArrivee, String dateDepart, 
                                 int nbPersonnes, int etoilesMin, double prixMax) 
    {
        List<Offre> offres = new ArrayList<>();

        //  Java ne sait pas comparer des chaînes avec "==" , donc on utilise .equals() ou .equalsIgnoreCase() (insensible à la casse)
        //  ici on ajoute "!" pour dire différent
        if (!this.ville.equalsIgnoreCase(villeDemandee) || this.etoiles < etoilesMin) 
        {
            return offres;
        }

        int nbNuits = calculerNombreNuits(dateArrivee, dateDepart);

        // Recherche des chambres disponibles
        List<Chambre> disponibles = depot.chambresDisponibles(nbPersonnes);

        // Création des offres selon le prix maximum
        for (int i = 0; i < disponibles.size(); i++) 
        {
            Chambre ch = disponibles.get(i);
            double prixTotal = ch.prixParNuit() * nbNuits;

            if (prixMax <= 0 || prixTotal <= prixMax) 
            {
                String idOffre = "OFFRE-" + ch.numero();
                offres.add(new Offre(idOffre, ch, prixTotal, nbNuits));
            }
        }

        return offres;
    }


    public boolean estPartenaire(String idAgence)
    {
        for (int i = 0; i < agencesAutorisees.size(); i++) 
        {
            if (agencesAutorisees.get(i).equalsIgnoreCase(idAgence)) 
            {
                return true;
            }
        }
        return false;
    }



    /**
     * Calcule le nombre de jours entre deux dates au format JJ-MM-AAAA.
     */
    private int calculerNombreNuits(String arrivee, String depart) 
    {
        if (arrivee == null || depart == null) 
        {
            return 1;
        }

        String[] partArr = arrivee.trim().split("[-/]");
        String[] partDep = depart.trim().split("[-/]");

        if (partArr.length == 3 && partDep.length == 3) 
        {
            int jourArr = Integer.parseInt(partArr[0]);
            int moisArr = Integer.parseInt(partArr[1]);
            int anneeArr = Integer.parseInt(partArr[2]);

            int jourDep = Integer.parseInt(partDep[0]);
            int moisDep = Integer.parseInt(partDep[1]);
            int anneeDep = Integer.parseInt(partDep[2]);

            java.time.LocalDate d1 = java.time.LocalDate.of(anneeArr, moisArr, jourArr);
            java.time.LocalDate d2 = java.time.LocalDate.of(anneeDep, moisDep, jourDep);

            long diff = java.time.temporal.ChronoUnit.DAYS.between(d1, d2);
            if (diff > 0) 
            {
                return (int) diff;
            }
        }

        return 1;
    }
}
