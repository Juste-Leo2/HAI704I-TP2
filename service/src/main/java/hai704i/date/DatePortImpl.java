package hai704i.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.jws.WebService;

/**
 * Implementation du contrat. Elle ne figure PAS dans le WSDL : le client ne
 * connaitra jamais cette classe, seulement les operations et les donnees.
 */
@WebService(endpointInterface = "hai704i.date.DatePort",
            serviceName = "DateService",
            portName = "DateSoapPort",
            targetNamespace = "http://date.hai704i.umontpellier.fr/")
public class DatePortImpl implements DatePort {

    private static final DateTimeFormatter JMA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HMS = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public String formaterDate(Format format) {
        LocalDateTime maintenant = LocalDateTime.now();
        switch (format) {
            case JMA:    return maintenant.format(JMA);
            case HMS:    return maintenant.format(HMS);
            default:     return maintenant.format(HMS) + " " + maintenant.format(JMA);
        }
    }

    @Override
    public DateDecomposee dateDuJour() {
        LocalDateTime maintenant = LocalDateTime.now();
        return new DateDecomposee(maintenant.getDayOfMonth(),
                                  maintenant.getMonthValue(),
                                  maintenant.getYear());
    }
}
