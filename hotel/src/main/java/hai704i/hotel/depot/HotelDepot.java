// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.depot;

import java.util.List;
import hai704i.hotel.domaine.Chambre;

public interface HotelDepot 
{
    List<Chambre> toutesLesChambres();
    List<Chambre> chambresDisponibles(int nbPersonnes);
}
