// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.depot;

import java.util.ArrayList;
import java.util.List;

import hai704i.hotel.domaine.Chambre;
import hai704i.hotel.domaine.Offre;
import hai704i.hotel.domaine.Reservation;

public class HotelDepotMemoire implements HotelDepot 
{
    private final List<Chambre> chambres = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Offre> offresGenerees = new ArrayList<>();

    public HotelDepotMemoire() 
    {
        // quelques exemples
        chambres.add(new Chambre("1", 1, 50.0, "Chambre Simple Standard"));
        chambres.add(new Chambre("2", 2, 80.0, "Chambre Double Standard"));
        chambres.add(new Chambre("3", 2, 100.0, "Chambre Double Vue Mer"));
    }

    // Constructeur avec liste de chambres
    public HotelDepotMemoire(List<Chambre> chambresInitiales) 
    {
        for (int i = 0; i < chambresInitiales.size(); i++) 
        {
            chambres.add(chambresInitiales.get(i));
        }
    }

    @Override
    public List<Chambre> toutesLesChambres() 
    {
        return new ArrayList<>(chambres);
    }

    @Override
    public List<Chambre> chambresDisponibles(int nbPersonnes) 
    {
        List<Chambre> resultats = new ArrayList<>();

        for (int i = 0; i < chambres.size(); i++) 
        {
            Chambre ch = chambres.get(i);
            if (ch.peutAccueillir(nbPersonnes) && !estChambreReservee(ch.numero())) 
            {
                resultats.add(ch);
            }
        }
        return resultats;
    }

    @Override
    public void enregistrerReservation(Reservation reservation) 
    {
        reservations.add(reservation);
    }

    @Override
    public Reservation trouverReservationParDemandeId(String idDemande) 
    {
        if (idDemande == null) 
        {
            return null;
        }

        for (int i = 0; i < reservations.size(); i++) 
        {
            Reservation r = reservations.get(i);
            if (idDemande.equals(r.idDemande())) 
            {
                return r;
            }
        }
        return null;
    }

    @Override
    public boolean estChambreReservee(String numeroChambre) 
    {
        for (int i = 0; i < reservations.size(); i++) 
        {
            Reservation r = reservations.get(i);
            // Si la référence de l'offre contient le numéro de la chambre
            if (r.idOffre() != null && r.idOffre().contains(numeroChambre)) 
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void enregistrerOffres(List<Offre> offres) 
    {
        for (int i = 0; i < offres.size(); i++) 
        {
            offresGenerees.add(offres.get(i));
        }
    }

    @Override
    public Offre trouverOffre(String idOffre) 
    {
        if (idOffre == null) 
        {
            return null;
        }

        for (int i = 0; i < offresGenerees.size(); i++) 
        {
            Offre o = offresGenerees.get(i);
            if (idOffre.equals(o.identifiant())) 
            {
                return o;
            }
        }
        return null;
    }
}
