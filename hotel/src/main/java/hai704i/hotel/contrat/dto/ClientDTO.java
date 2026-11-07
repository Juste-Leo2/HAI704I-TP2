// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.dto;

public class ClientDTO 
{
    private String nom;
    private String prenom;

    public ClientDTO() 
    {
    }

    public ClientDTO(String nom, String prenom) 
    {
        this.nom = nom;
        this.prenom = prenom;
    }

    // get et set
    public String getNom() 
    {
        return nom;
    }

    public void setNom(String nom) 
    {
        this.nom = nom;
    }

    public String getPrenom() 
    {
        return prenom;
    }

    public void setPrenom(String prenom) 
    {
        this.prenom = prenom;
    }
}
