// Fichier inspiré du projet employe-demo (Bachar Rima)

package hai704i.hotel.contrat.dto;

public class ConfirmationReservationDTO 
{
    private String referenceReservation;
    private String idDemande;
    private String nomHotel;
    private double prixTotal;
    private String message;

    public ConfirmationReservationDTO() 
    {
    }

    public ConfirmationReservationDTO(String referenceReservation, String idDemande, String nomHotel, 
                                      double prixTotal, String message) 
    {
        this.referenceReservation = referenceReservation;
        this.idDemande = idDemande;
        this.nomHotel = nomHotel;
        this.prixTotal = prixTotal;
        this.message = message;
    }

    // get et set
    public String getReferenceReservation() 
    {
        return referenceReservation;
    }

    public void setReferenceReservation(String referenceReservation) 
    {
        this.referenceReservation = referenceReservation;
    }

    public String getIdDemande() 
    {
        return idDemande;
    }

    public void setIdDemande(String idDemande) 
    {
        this.idDemande = idDemande;
    }

    public String getNomHotel() 
    {
        return nomHotel;
    }

    public void setNomHotel(String nomHotel) 
    {
        this.nomHotel = nomHotel;
    }

    public double getPrixTotal() 
    {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) 
    {
        this.prixTotal = prixTotal;
    }

    public String getMessage() 
    {
        return message;
    }

    public void setMessage(String message) 
    {
        this.message = message;
    }
}
