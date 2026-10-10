# `date-demo` — un service SOAP minimal, publié et consommé

HAI704I — Architectures logicielles distribuées — TP 2 (2026–2027)

Exemple **de démonstration**, à lire et à exécuter avant de commencer le TP. 
C'est le service `Date` que vous avez **analysé au TD 2**, cette fois **publié et appelé pour de vrai**.

Deux projets Maven indépendants :

```text
date-demo/
├── service/   publie le service avec Eclipse Metro (un simple main)
└── client/    engendre son proxy depuis le WSDL, puis appelle le service
```

## Prérequis

* **Java 17** ou plus : `java -version`
* **Maven 3.9** ou plus : `mvn -v`
* une connexion réseau à la **première** construction (Maven télécharge les dépendances)

## 1. Publier le service

```bash
cd service
mvn clean package
mvn exec:java
```

Sortie attendue :

```text
Service publie  : http://localhost:8081/date
Contrat WSDL    : http://localhost:8081/date?wsdl
Schema XSD      : http://localhost:8081/date?xsd=1
Ctrl+C pour arreter.
```

Laissez ce terminal ouvert. Ouvrez `http://localhost:8081/date?wsdl` dans un navigateur :
le contrat est **engendré à partir du code Java**, à la volée.

> Le schéma n'est pas dans le WSDL : celui-ci l'**importe** depuis `?xsd=1`. Ouvrez les deux.

## 2. Appeler le service à la main, avant d'écrire une ligne de client

Avec SoapUI, Postman, ou simplement `curl` :

```bash
curl -X POST http://localhost:8081/date \
     -H "Content-Type: text/xml; charset=utf-8" \
     -H 'SOAPAction: ""' \
     --data-binary @- <<'XML'
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Body>
    <formaterDate xmlns="http://date.hai704i.umontpellier.fr/">
      <format xmlns="">JMA</format>
    </formaterDate>
  </soapenv:Body>
</soapenv:Envelope>
XML
```

Réponse (HTTP 200) :

```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:formaterDateResponse xmlns:ns2="http://date.hai704i.umontpellier.fr/">
      <date>24/09/2026</date>
    </ns2:formaterDateResponse>
  </S:Body>
</S:Envelope>
```

**Regardez bien le `xmlns=""` sur `<format>`.** Le schéma engendré ici ne porte pas
`elementFormDefault="qualified"` : l'élément enveloppe `formaterDate` est dans l'espace de
noms du contrat, mais l'élément local `format`, lui, n'est dans **aucun** espace de noms.
Si vous l'écrivez qualifié, le serveur ne le reconnaît pas, JAXB laisse le paramètre à
`null`, et vous recevez une faute `S:Server` déroutante :

```text
<faultstring>Cannot invoke "hai704i.date.Format.ordinal()" because "format" is null</faultstring>
```

C'est exactement le défaut **D5** du TD 2, vu de l'autre côté.

> **« Non qualifié » ne veut pas dire « valeur libre ».** L'espace de noms de l'**élément** et
> l'espace de noms de son **type** sont deux questions distinctes. Le schéma déclare
> `<xs:element name="format" type="tns:format"/>` : l'élément local `format` n'appartient à
> aucun espace de noms, mais son **type** est `tns:format`, défini dans l'espace de noms cible
> du schéma, et restreint aux valeurs `JMA`, `HMS` et `HMSJMA`. La valeur reste donc contrainte
> par le contrat.

Attention toutefois à ce que vous observerez : **ce service ne valide pas les messages qu'il
reçoit.** Envoyez `<format xmlns="">XYZ</format>` et vous obtiendrez… la même faute
« `format is null` » que ci-dessus : la valeur ne correspond à aucune constante de
l'énumération, JAXB laisse donc le paramètre à `null`. Le contrat interdit `XYZ`, mais personne
ne le vérifie par défaut. Un service qui active la validation de schéma répond, lui, sans
ambiguïté :

```text
<faultstring>cvc-enumeration-valid: Value 'XYZ' is not facet-valid with respect to
enumeration '[JMA, HMS, HMSJMA]'.</faultstring>
```

C'est le défaut **D2** du TD 2. Retenez la leçon de diagnostic : avec ce réglage par défaut,
une **erreur d'espace de noms** et une **valeur hors énumération** produisent le *même*
symptôme. L'annexe 1 explique comment les distinguer.

Essayez aussi d'envoyer la **même requête dans une enveloppe SOAP 1.2** avec
`Content-Type: application/soap+xml` : le point d'accès, lié en SOAP 1.1, répond
**HTTP 415**. C'est le défaut **D6** du TD 2.

## 3. Construire et lancer le client

Dans un second terminal, le service restant en marche :

```bash
cd client
mvn clean package     # wsimport engendre le proxy pendant cette phase
mvn exec:java
```

Sortie attendue (la date varie) :

```text
adresse   : http://localhost:8081/date
proxy     : jdk.proxy3.$Proxy51  (proxy dynamique : true)
JMA       : 24/09/2026
HMSJMA    : 22:01:07 24/09/2026
decomposee: 24/9/2026
classe recue : hai704i.date.gen.DateDecomposee  (c'est une COPIE, reconstruite depuis le XML)
```

Vous pouvez viser une autre adresse sans recompiler :

```bash
mvn exec:java -Dexec.args="http://localhost:8082/date"
```

## 4. Ce qu'il faut avoir observé

* Le **contrat** (`?wsdl` + `?xsd=1`) est engendré depuis le code Java : c'est l'approche
  *code-first* du CM 03.
* Le client ne connaît **aucune** classe du service. Tout ce qu'il manipule
  (`DatePort`, `DateService`, `Format`, `DateDecomposee`) a été **engendré depuis le WSDL**
  dans `client/target/generated-sources/`.
* Le proxy est un **proxy dynamique Java**, comme le stub RMI du TP 1.
* L'objet reçu est une **copie** reconstruite depuis le XML : il n'y a pas d'objet distant,
  pas d'identité partagée.
* Dans le schéma engendré, `dateDecomposee` liste ses champs **par ordre alphabétique**
  (`annee`, `jour`, `mois`) et non dans l'ordre de déclaration : JAXB trie, sauf si on impose
  l'ordre avec `@XmlType(propOrder = …)`. L'ordre d'une `xs:sequence` fait partie du contrat.

## Où est le WSDL utilisé par le client ?

`client/src/main/wsdl/date.wsdl` et `date.xsd` sont une **copie enregistrée** du contrat, pour
que le client se construise même hors ligne. La seule modification par rapport à ce que sert
le serveur : `schemaLocation` pointe sur le fichier voisin au lieu de `?xsd=1`.

En situation réelle, on engendre le client directement depuis le service en marche, en
donnant l'URL `…?wsdl` au plugin `jaxws-maven-plugin`. **Si vous modifiez le service, le
contrat change : il faut réenregistrer le WSDL (ou pointer sur l'URL) et reconstruire le
client.** C'est la première cause d'erreurs incompréhensibles.