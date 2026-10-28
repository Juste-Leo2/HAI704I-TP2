# HAI704I - TP 2 : Services web SOAP (Système de réservation d'hôtels)

**Université de Montpellier** - Master 1 Informatique (Parcours ICO)  
**UE HAI704I** : Architectures Logicielles Distribuées  
**Enseignant responsable** : Bachar Rima  
**Auteur** : Léonard ADAMO  
**Numéro étudiant** : 22517746
**Dépôt GitHub** : [https://github.com/Juste-Leo2/HAI704I-TP2](https://github.com/Juste-Leo2/HAI704I-TP2)  


## 1. Introduction

Pour le moment, ce TP est en cours de construction.

Pour commencer, j'ai mis en place A0 pour setup l'environnement de travail. voir [tuto](tuto.md)

## A1 

Pour effectuer le travail, je me suis basé sur l'exemple complet service employé démo présent sur Moodle

```text
HAI704I-TP2/

├── rapport/
│   └── resume.md
├── captures/
│   └── ___.txt            
│
├── hotel/
│   ├── pom.xml
│   └── src/main/java/hai704i/hotel/
│       ├── Serveur.java
│       ├── contrat/
│       │   ├── HotelPort.java
│       │   ├── dto/
│       │   │   ├── package-info.java
│       │   │   ├── DemandeRecherche.java
│       │   │   └── OffreDTO.java
│       │   └── faute/
│       │       ├── package-info.java
│       │       └── AgenceNonReconnue.java
│       ├── domaine/
│       │   ├── Chambre.java
│       │   └── Offre.java
│       ├── depot/
│       │   ├── HotelDepot.java
│       │   └── HotelDepotMemoire.java
│       ├── service/
│       │   └── ServiceHotel.java
│       └── endpoint/
│           └── HotelEndpoint.java
│
└── agence/
    ├── pom.xml
    └── src/main/java/hai704i/agence/
        ├── application/
        │   └── AgenceApplication.java
        └── cli/
            └── ConsoleAgence.java

```

A titre de comparaison, voilà l'arborescence reprise de l'exemple employé-demo sur Moodle(adapté pour mettre uniquement les fichier comparatif) :

```text
employe-demo/
  captures/                                         sorties réellement enregistrées (A7)
  client/                                           PROJET CLIENT SÉPARÉ
    src/main/
      java/hai704i/employe/client/
        application/                                LOGIQUE : seule à connaître SOAP
          AnnuaireEmployes.java
        cli/                                        INTERFACE CONSOLE (aucun import SOAP)
          ConsoleEmploye.java
      wsdl/
        employes.wsdl                               copie locale du contrat
    pom.xml                                         wsimport ; aucune dépendance au service
  service/                                          SERVICE WEB
    src/main/java/hai704i/employe/
      contrat/                                      CE QUI EST PUBLIÉ
        dto/                                        types de transfert
          DemandeCreation.java
          Employe.java
          package-info.java                         espace de noms déclaré
        faute/                                      les refus déclarés
          DonneesInvalides.java
          EmployeAbsent.java
          package-info.java
        EmployePort.java                            interface @WebService
      domaine/                                      MODÈLE MÉTIER (sans annotation SOAP)
        EmployeMetier.java
      depot/                                        FRONTIÈRE DE PERSISTANCE
        EmployeDepot.java                           interface du dépôt
      service/                                      RÈGLES MÉTIER (ignore SOAP)
        ServiceEmploye.java
      endpoint/                                     ADAPTATEUR SOAP (traduit, délègue)
        EmployeEndpoint.java
    pom.xml                                         dépendance jaxws-rt
```

## 7. Licence

Ce projet est distribué sous licence **Apache 2.0**. Consultez le fichier [LICENSE](LICENSE) pour plus de détails.