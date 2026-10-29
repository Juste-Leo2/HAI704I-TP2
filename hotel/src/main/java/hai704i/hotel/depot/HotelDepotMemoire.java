// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.depot;

import java.util.ArrayList;
import java.util.List;

import hai704i.hotel.domaine.Chambre;

public class HotelDepotMemoire implements HotelDepot 
{
    private final List<Chambre> chambres = new ArrayList<>();

    public HotelDepotMemoire() 
    {
        // quelques exemples
        chambres.add(new Chambre("1", 1, 50.0, "Chambre Simple Standard"));
        chambres.add(new Chambre("2", 2, 80.0, "Chambre Double Standard"));
        chambres.add(new Chambre("3", 2, 100.0, "Chambre Double Vue Mer"));
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
            if (ch.peutAccueillir(nbPersonnes)) 
            {
                resultats.add(ch);
            }
        }
        return resultats;
    }
}
