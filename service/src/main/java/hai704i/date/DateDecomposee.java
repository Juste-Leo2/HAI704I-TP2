package hai704i.date;

/**
 * Donnee echangee, transmise PAR VALEUR : le client en recevra une copie,
 * reconstruite a partir du XML. Dans le schema engendre, cette classe devient
 * un xs:complexType dont la xs:sequence fixe l'ordre des trois champs.
 *
 * Un constructeur sans parametre est indispensable : c'est lui que JAXB
 * utilise pour reconstruire l'objet a la reception.
 */
public class DateDecomposee {

    private int jour;
    private int mois;
    private int annee;

    public DateDecomposee() { }

    public DateDecomposee(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    public int getJour() { return jour; }
    public void setJour(int jour) { this.jour = jour; }

    public int getMois() { return mois; }
    public void setMois(int mois) { this.mois = mois; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
}
