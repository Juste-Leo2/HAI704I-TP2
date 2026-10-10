package hai704i.demo;

import java.lang.reflect.Proxy;

import hai704i.date.gen.DateDecomposee;
import hai704i.date.gen.DatePort;
import hai704i.date.gen.DateService;
import hai704i.date.gen.Format;
import jakarta.xml.ws.BindingProvider;

/**
 * Client engendre a partir du WSDL : aucune classe du service n'est utilisee
 * ici. Tout ce que le client connait vient du contrat.
 */
public class Client {

    public static void main(String[] args) {
        String adresse = args.length > 0 ? args[0] : "http://localhost:8081/date";

        // 1. La classe DateService et l'interface DatePort ont ete ENGENDREES
        //    a partir du WSDL par le plugin Maven (wsimport), pendant le build.
        DateService service = new DateService();
        DatePort port = service.getDateSoapPort();

        // 2. L'adresse du service se configure a l'execution : le WSDL en
        //    contient une, mais rien n'oblige a l'utiliser.
        ((BindingProvider) port).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, adresse);

        System.out.println("adresse   : " + adresse);
        System.out.println("proxy     : " + port.getClass().getName()
                + "  (proxy dynamique : " + Proxy.isProxyClass(port.getClass()) + ")");

        // 3. Deux appels distants qui ressemblent a des appels locaux.
        System.out.println("JMA       : " + port.formaterDate(Format.JMA));
        System.out.println("HMSJMA    : " + port.formaterDate(Format.HMSJMA));

        DateDecomposee d = port.dateDuJour();
        System.out.println("decomposee: " + d.getJour() + "/" + d.getMois() + "/" + d.getAnnee());
        System.out.println("classe recue : " + d.getClass().getName()
                + "  (c'est une COPIE, reconstruite depuis le XML)");
    }
}
