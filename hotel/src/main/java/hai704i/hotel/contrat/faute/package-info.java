// Fichier adapté du projet employe-demo (Bachar Rima)

/**
 * Les refus métier que le contrat <em>déclare</em>.
 *
 * <p>Une exception vérifiée, annotée {@code @WebFault} et citée dans la
 * signature d'une opération, devient un {@code <wsdl:fault>} du contrat, puis un
 * {@code <soap:Fault>} porteur d'un {@code <detail>} typé sur le réseau. Le
 * client en reçoit une classe engendrée, distincte pour chaque faute, et peut
 * donc les distinguer dans des {@code catch} séparés.
 *
 * <p>Une exception <em>non</em> déclarée traverse aussi le réseau, mais sous la
 * forme d'une faute générique : le client ne sait plus laquelle il a reçue.
 *
 * <p>Chaque faute porte son {@code targetNamespace} explicitement, pour la même
 * raison que les types de transfert : le contrat public ne doit pas dépendre du
 * nom des paquetages Java.
 */
package hai704i.hotel.contrat.faute;
