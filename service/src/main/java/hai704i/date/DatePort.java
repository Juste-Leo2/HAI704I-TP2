package hai704i.date;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

/**
 * Contrat du service : ce que le WSDL publiera.
 *
 * L'interface porte @WebService : c'est elle qui devient le portType. Les noms
 * donnes ici (nom du port, des operations, des parametres, du resultat) sont
 * ceux que l'on retrouvera dans le WSDL, puis dans les messages SOAP.
 */
@WebService(name = "DatePort",
            targetNamespace = "http://date.hai704i.umontpellier.fr/")
public interface DatePort {

    /** La date du jour, mise en forme selon le format demande. */
    @WebMethod
    @WebResult(name = "date")
    String formaterDate(@WebParam(name = "format") Format format);

    /** La date du jour, decomposee en jour / mois / annee. */
    @WebMethod
    @WebResult(name = "date")
    DateDecomposee dateDuJour();
}
