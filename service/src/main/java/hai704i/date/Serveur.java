package hai704i.date;

import jakarta.xml.ws.Endpoint;

/**
 * Publication du service. Endpoint.publish demarre un serveur HTTP leger
 * (fourni par Metro) et expose le WSDL a l'adresse ?wsdl.
 *
 * Aucun conteneur, aucun serveur d'application : un simple main().
 */
public class Serveur {

    public static void main(String[] args) {
        String adresse = args.length > 0 ? args[0] : "http://localhost:8081/date";
        Endpoint.publish(adresse, new DatePortImpl());
        System.out.println("Service publie  : " + adresse);
        System.out.println("Contrat WSDL    : " + adresse + "?wsdl");
        System.out.println("Schema XSD      : " + adresse + "?xsd=1");
        System.out.println("Ctrl+C pour arreter.");
    }
}
