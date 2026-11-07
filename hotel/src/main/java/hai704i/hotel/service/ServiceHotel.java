// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import hai704i.hotel.contrat.faute.AuthentificationEchouee;
import hai704i.hotel.contrat.faute.OffreIndisponible;
import hai704i.hotel.contrat.faute.PaiementInvalide;
import hai704i.hotel.depot.HotelDepot;
import hai704i.hotel.domaine.Chambre;
import hai704i.hotel.domaine.Offre;
import hai704i.hotel.domaine.Reservation;

public class ServiceHotel 
{
    private final HotelDepot depot;
    private final String nomHotel;
    private final int etoiles;
    private final String ville;

    // Représentation interne d'un compte partenaire (l'hôtel ne stocke QUE le hash salé)
    private record ComptePartenaire(String idAgence, String sel, String empreinteSalee) {}
    private final List<ComptePartenaire> comptesPartenaires = new ArrayList<>();

    public ServiceHotel(HotelDepot depot, String nomHotel, int etoiles, String ville) 
    {
        this.depot = depot;
        this.nomHotel = nomHotel;
        this.etoiles = etoiles;
        this.ville = ville;

        // Initialisation des partenaires autorisés avec mot de passe haché et salé
        // Mot de passe agence_lille : "secret_lille_123"
        // Mot de passe agence_paris : "secret_paris_456"
        ajouterPartenaire("agence_lille", "sel_lille_random", "secret_lille_123");
        ajouterPartenaire("agence_paris", "sel_paris_random", "secret_paris_456");
    }

    private void ajouterPartenaire(String idAgence, String sel, String motDePasseClair) 
    {
        String empreinte = calculerEmpreinte(motDePasseClair, sel);
        comptesPartenaires.add(new ComptePartenaire(idAgence, sel, empreinte));
    }

    private static String calculerEmpreinte(String motDePasse, String sel) 
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((sel + motDePasse).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) 
            {
                sb.append(String.format("%02x", hash[i]));
            }
            return sb.toString();
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Erreur hachage", e);
        }
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

    public boolean estPartenaire(String idAgence) 
    {
        for (int i = 0; i < comptesPartenaires.size(); i++) 
        {
            if (comptesPartenaires.get(i).idAgence().equalsIgnoreCase(idAgence)) 
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Authentifie l'agence en comparant le secret présenté avec l'empreinte salée en temps constant.
     */
    public boolean authentifierAgence(String idAgence, String secretClair) 
    {
        if (idAgence == null || secretClair == null) 
        {
            return false;
        }

        for (int i = 0; i < comptesPartenaires.size(); i++) 
        {
            ComptePartenaire cp = comptesPartenaires.get(i);
            if (cp.idAgence().equalsIgnoreCase(idAgence)) 
            {
                String empreinteCalculee = calculerEmpreinte(secretClair, cp.sel());
                // Comparaison en temps constant pour éviter les attaques temporelles
                return MessageDigest.isEqual(
                    empreinteCalculee.getBytes(StandardCharsets.UTF_8),
                    cp.empreinteSalee().getBytes(StandardCharsets.UTF_8)
                );
            }
        }
        return false;
    }

    /**
     * Recherche les offres disponibles selon les critères reçus.
     */
    public List<Offre> rechercher(String villeDemandee, String dateArrivee, String dateDepart, 
                                  int nbPersonnes, int etoilesMin, double prixMax) 
    {
        List<Offre> offres = new ArrayList<>();

        if (!this.ville.equalsIgnoreCase(villeDemandee) || this.etoiles < etoilesMin) 
        {
            return offres;
        }

        int nbNuits = calculerNombreNuits(dateArrivee, dateDepart);

        // Recherche des chambres disponibles non réservées
        List<Chambre> disponibles = depot.chambresDisponibles(nbPersonnes);

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

        depot.enregistrerOffres(offres);
        return offres;
    }

    /**
     * Effectue la réservation avec authentification, vérification du paiement et rejeu idempotent.
     */
    public Reservation reserver(String idDemande, String idAgence, String secretAgence, 
                                String idOffre, String nomClient, String prenomClient, 
                                String numeroCarte, int moisExp, int anneeExp, String cvc)
        throws AuthentificationEchouee, OffreIndisponible, PaiementInvalide 
    {
        // Authentification de l'agence
        if (!authentifierAgence(idAgence, secretAgence)) 
        {
            throw new AuthentificationEchouee("Authentification refusee : identifiant ou secret d'agence incorrect.");
        }

        // Idempotence : si la demande a déjà été traitée, on renvoie la réservation existante
        Reservation existante = depot.trouverReservationParDemandeId(idDemande);
        if (existante != null) 
        {
            return existante;
        }

        // Contrôle du paiement
        controlerPaiement(numeroCarte, moisExp, anneeExp, cvc);

        // Vérification de la disponibilité
        String numeroChambre = extraireNumeroChambre(idOffre);
        if (numeroChambre == null || depot.estChambreReservee(numeroChambre)) 
        {
            throw new OffreIndisponible("L'offre " + idOffre + " n'est plus disponible ou la chambre est deja reservee.");
        }

        // Recherche de la chambre pour calculer le prix
        List<Chambre> toutes = depot.toutesLesChambres();
        Chambre chambreTrouvee = null;
        for (int i = 0; i < toutes.size(); i++) 
        {
            if (toutes.get(i).numero().equals(numeroChambre)) 
            {
                chambreTrouvee = toutes.get(i);
                break;
            }
        }

        if (chambreTrouvee == null) 
        {
            throw new OffreIndisponible("Chambre introuvable pour l'offre : " + idOffre);
        }

        Offre offreTrouvee = depot.trouverOffre(idOffre);
        double prixTotal = offreTrouvee != null ? offreTrouvee.prixTotal() : chambreTrouvee.prixParNuit();

        // Masquage du numéro de carte (ex: "**** 1234") et CVC NON STOCKÉ
        String carteMasquee = "**** " + numeroCarte.substring(numeroCarte.length() - 4);
        String refReservation = "RES-" + (int)(Math.random() * 900000 + 100000);

        Reservation nouvelleReservation = new Reservation(
            refReservation,
            idDemande,
            idOffre,
            prenomClient + " " + nomClient,
            prixTotal,
            carteMasquee
        );

        depot.enregistrerReservation(nouvelleReservation);
        return nouvelleReservation;
    }

    private void controlerPaiement(String numero, int mois, int annee, String cvc) throws PaiementInvalide 
    {
        if (numero == null || numero.trim().length() < 12) 
        {
            throw new PaiementInvalide("Numero de carte invalide (12 a 19 chiffres attendus).");
        }
        if (mois < 1 || mois > 12) 
        {
            throw new PaiementInvalide("Mois d'expiration invalide (doit etre entre 1 et 12).");
        }
        if (annee < 2026) 
        {
            throw new PaiementInvalide("Annee d'expiration revolue.");
        }
        if (cvc == null || cvc.trim().length() != 3) 
        {
            throw new PaiementInvalide("Cryptogramme CVC invalide (3 chiffres attendus).");
        }
    }

    private String extraireNumeroChambre(String idOffre) 
    {
        if (idOffre == null) 
        {
            return null;
        }
        if (idOffre.startsWith("OFFRE-")) 
        {
            return idOffre.substring("OFFRE-".length());
        }
        return idOffre;
    }

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
