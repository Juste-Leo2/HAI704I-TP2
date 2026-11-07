// Fichier adapté du projet date-demo (Bachar Rima)

package hai704i.hotel;

import hai704i.hotel.depot.HotelDepot;
import hai704i.hotel.depot.HotelDepotMemoire;
import hai704i.hotel.endpoint.HotelEndpoint;
import hai704i.hotel.service.ServiceHotel;
import jakarta.xml.ws.Endpoint;

/**
 * Publication du service. Endpoint.publish demarre un serveur HTTP leger
 * (fourni par Metro) et expose le WSDL a l'adresse ?wsdl.
 *
 * Aucun conteneur, aucun serveur d'application : un simple main().
 */
public class Serveur {

    public static void main(String[] args) {
        String adresse = args.length > 0 ? args[0] : "http://localhost:8081/hotel";

        ////////////////////////A2//////////////////////
        // Dépôt en mémoire
        HotelDepot depot = new HotelDepotMemoire();

        // Service métier pur (sans SOAP)
        ServiceHotel service = new ServiceHotel(depot, "Hôtel Belle Etoile", 3, "Montpellier");

        // Point d'accès SOAP (adaptateur)
        HotelEndpoint endpoint = new HotelEndpoint(service);
        ///////////////////////////////////////////////

        ////////////////////////A6//////////////////////
        if (adresse.contains("8082")) 
        {
            java.util.List<hai704i.hotel.domaine.Chambre> chambresSoleil = new java.util.ArrayList<>();
            chambresSoleil.add(new hai704i.hotel.domaine.Chambre("10", 1, 70.0, "Chambre Simple Confort"));
            chambresSoleil.add(new hai704i.hotel.domaine.Chambre("20", 2, 120.0, "Suite Junior"));
            chambresSoleil.add(new hai704i.hotel.domaine.Chambre("30", 2, 150.0, "Suite Vue Panoramique"));
            depot = new HotelDepotMemoire(chambresSoleil);
            service = new ServiceHotel(depot, "Hôtel du Soleil", 4, "Montpellier");
            endpoint = new HotelEndpoint(service);
        }
        ///////////////////////////////////////////////


        Endpoint.publish(adresse, endpoint);
        System.out.println("Service publie  : " + adresse);
        System.out.println("Contrat WSDL    : " + adresse + "?wsdl");
        System.out.println("Schema XSD      : " + adresse + "?xsd=1");
        System.out.println("Ctrl+C pour arreter.");
    }
}
