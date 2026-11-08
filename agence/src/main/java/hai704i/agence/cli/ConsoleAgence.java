// Architecture inspirée de l'exemple employe-demo (Bachar Rima)
// Respect de la séparation stricte : la console ignore totalement l'existence de SOAP

package hai704i.agence.cli;

import java.util.List;
import java.util.Scanner;

import hai704i.agence.application.LogiqueAgence;
import hai704i.agence.application.OffreVue;
import hai704i.agence.application.ResultatReservation;

/**
 * Interface en ligne de commande, et point d'entrée interactif du client agence.
 */
public class ConsoleAgence 
{
    private final LogiqueAgence agence;
    private final Scanner entree = new Scanner(System.in);
    private List<OffreVue> offresEnMemoire;

    public ConsoleAgence(LogiqueAgence agence) 
    {
        this.agence = agence;
    }

    public static void main(String[] args) 
    {
        String idAgence = args.length > 0 ? args[0] : "agence_lille";
        String secretAgence = args.length > 1 ? args[1] : "secret_lille_123";

        System.out.println("Agence partenaire : " + idAgence);
        new ConsoleAgence(new LogiqueAgence(idAgence, secretAgence)).boucle();
    }

    private void boucle() 
    {
        while (true) 
        {
            System.out.println();
            System.out.println("1) chercher\n2) réserver\n3) quitter");
            System.out.print("> ");

            String choix = entree.hasNextLine() ? entree.nextLine().trim() : "3";

            try 
            {
                switch (choix) 
                {
                    case "1":
                        rechercher();
                        break;
                    case "2":
                        reserver();
                        break;
                    case "3":
                        System.out.println("au revoir");
                        return;
                    default:
                        System.out.println("choix inconnu");
                        break;
                }
            } 
            catch (RuntimeException e) 
            {
                // Filet de sécurité : même un défaut imprévu ne doit pas sortir
                // l'utilisateur de la boucle sur une trace de pile.
                System.out.println("opération impossible : " + e.getMessage());
            }
        }
    }

    private void rechercher() 
    {
        String ville = demander("ville", "Montpellier");
        String arrivee = demander("date d'arrivée (JJ-MM-AAAA)", "01-12-2026");
        String depart = demander("date de départ (JJ-MM-AAAA)", "06-12-2026");
        int nbPersonnes = entier(demander("nombre de personnes", "2"), 2);
        int etoilesMin = entier(demander("étoiles minimales", "3"), 1);
        double prixMax = decimal(demander("prix maximum (0 = illimité)", "1000.0"), 0.0);

        offresEnMemoire = agence.rechercherOffres(ville, arrivee, depart, nbPersonnes, etoilesMin, prixMax);

        // Affichage des éventuelles défaillances partielles
        List<String> avertissements = agence.getDerniersAvertissements();
        for (int i = 0; i < avertissements.size(); i++) 
        {
            System.out.println("! " + avertissements.get(i));
        }

        afficherOffres();
    }

    private void afficherOffres() 
    {
        if (offresEnMemoire == null || offresEnMemoire.isEmpty()) 
        {
            System.out.println("aucune offre");
            return;
        }

        System.out.printf("%-4s %-12s %-24s %-12s %-30s %10s%n",
                "N°", "REF", "HÔTEL", "ÉTOILES", "CHAMBRE", "PRIX");

        for (int i = 0; i < offresEnMemoire.size(); i++) 
        {
            OffreVue o = offresEnMemoire.get(i);
            System.out.printf("%-4d %-12s %-24s %-12s %-30s %8.2f EUR%n",
                    (i + 1),
                    o.identifiant(),
                    o.nomHotel(),
                    o.nombreEtoiles() + " étoiles",
                    o.descriptionChambre(),
                    o.prixTotal());
        }
    }

    private void reserver() 
    {
        if (offresEnMemoire == null || offresEnMemoire.isEmpty()) 
        {
            System.out.println("aucune offre disponible : lancez d'abord une recherche");
            return;
        }

        int numOffre = entier(demander("numéro de l'offre (1 à " + offresEnMemoire.size() + ")", "1"), -1);

        if (numOffre < 1 || numOffre > offresEnMemoire.size()) 
        {
            System.out.println("! numéro d'offre invalide");
            return;
        }

        OffreVue offreChoisie = offresEnMemoire.get(numOffre - 1);

        String nom = demander("nom du voyageur", "Dupont");
        String prenom = demander("prénom du voyageur", "Pierre");
        String numeroCarte = demander("numéro de carte (16 chiffres)", "1234567890123456");
        int moisExp = entier(demander("mois d'expiration", "12"), 12);
        int anneeExp = entier(demander("année d'expiration", "2027"), 2027);
        String cvc = demander("CVC", "123");

        ResultatReservation resultat = agence.reserver(offreChoisie, nom, prenom, numeroCarte, moisExp, anneeExp, cvc);

        if (resultat.accepte()) 
        {
            System.out.println("réservation " + resultat.referenceReservation() + " confirmée");
            System.out.println(resultat.message());
        } 
        else 
        {
            System.out.println("! " + resultat.refus());
        }
    }

    /**
     * Saisie avec valeur par défaut : si l'utilisateur appuie directement sur Entrée,
     * la valeur par défaut est automatiquement retenue.
     */
    private String demander(String libelle, String defaut) 
    {
        System.out.print(libelle + " [" + defaut + "] : ");
        String s = entree.hasNextLine() ? entree.nextLine().trim() : "";
        return s.isEmpty() ? defaut : s;
    }

    private static int entier(String s, int valeurParDefaut) 
    {
        try 
        {
            return Integer.parseInt(s.trim());
        } 
        catch (NumberFormatException e) 
        {
            return valeurParDefaut;
        }
    }

    private static double decimal(String s, double valeurParDefaut) 
    {
        try 
        {
            return Double.parseDouble(s.replace(',', '.').trim());
        } 
        catch (NumberFormatException e) 
        {
            return valeurParDefaut;
        }
    }
}

