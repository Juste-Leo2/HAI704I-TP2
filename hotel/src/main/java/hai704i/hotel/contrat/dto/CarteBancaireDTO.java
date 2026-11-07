// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.dto;

public class CarteBancaireDTO 
{
    private String numeroCarte;
    private int moisExpiration;
    private int anneeExpiration;
    private String cvc;

    public CarteBancaireDTO() 
    {
    }

    public CarteBancaireDTO(String numeroCarte, int moisExpiration, int anneeExpiration, String cvc) 
    {
        this.numeroCarte = numeroCarte;
        this.moisExpiration = moisExpiration;
        this.anneeExpiration = anneeExpiration;
        this.cvc = cvc;
    }

    // get et set
    public String getNumeroCarte() 
    {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) 
    {
        this.numeroCarte = numeroCarte;
    }

    public int getMoisExpiration() 
    {
        return moisExpiration;
    }

    public void setMoisExpiration(int moisExpiration) 
    {
        this.moisExpiration = moisExpiration;
    }

    public int getAnneeExpiration() 
    {
        return anneeExpiration;
    }

    public void setAnneeExpiration(int anneeExpiration) 
    {
        this.anneeExpiration = anneeExpiration;
    }

    public String getCvc() 
    {
        return cvc;
    }

    public void setCvc(String cvc) 
    {
        this.cvc = cvc;
    }
}
