// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.dto;

public class DemandeReservation 
{
    private String idDemande;
    private String identifiantAgence;
    private String secretAgence;
    private String idOffre;
    private ClientDTO client;
    private CarteBancaireDTO carte;

    public DemandeReservation() 
    {
    }

    public DemandeReservation(String idDemande, String identifiantAgence, String secretAgence, 
                              String idOffre, ClientDTO client, CarteBancaireDTO carte) 
    {
        this.idDemande = idDemande;
        this.identifiantAgence = identifiantAgence;
        this.secretAgence = secretAgence;
        this.idOffre = idOffre;
        this.client = client;
        this.carte = carte;
    }

    // get et set
    public String getIdDemande() 
    {
        return idDemande;
    }

    public void setIdDemande(String idDemande) 
    {
        this.idDemande = idDemande;
    }

    public String getIdentifiantAgence() 
    {
        return identifiantAgence;
    }

    public void setIdentifiantAgence(String identifiantAgence) 
    {
        this.identifiantAgence = identifiantAgence;
    }

    public String getSecretAgence() 
    {
        return secretAgence;
    }

    public void setSecretAgence(String secretAgence) 
    {
        this.secretAgence = secretAgence;
    }

    public String getIdOffre() 
    {
        return idOffre;
    }

    public void setIdOffre(String idOffre) 
    {
        this.idOffre = idOffre;
    }

    public ClientDTO getClient() 
    {
        return client;
    }

    public void setClient(ClientDTO client) 
    {
        this.client = client;
    }

    public CarteBancaireDTO getCarte() 
    {
        return carte;
    }

    public void setCarte(CarteBancaireDTO carte) 
    {
        this.carte = carte;
    }
}
