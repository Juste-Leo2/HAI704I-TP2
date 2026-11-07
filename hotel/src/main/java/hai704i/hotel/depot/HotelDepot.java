// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.depot;

import java.util.List;
import hai704i.hotel.domaine.Chambre;
import hai704i.hotel.domaine.Offre;
import hai704i.hotel.domaine.Reservation;

public interface HotelDepot 
{
    List<Chambre> toutesLesChambres();
    List<Chambre> chambresDisponibles(int nbPersonnes);
    void enregistrerReservation(Reservation reservation);
    Reservation trouverReservationParDemandeId(String idDemande);
    boolean estChambreReservee(String numeroChambre);
    void enregistrerOffres(List<Offre> offres);
    Offre trouverOffre(String idOffre);
}
