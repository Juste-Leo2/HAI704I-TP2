# Installation de l'environnement (Windows)

Guide d'installation rapide pour **Java 17**, **Maven** et **HTTPie**.

---

## 1. JDK 17

1. Télécharger l'installateur Windows x64 (`.exe`) :  
   [Oracle JDK 17 Archive Downloads](https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html)
2. Lancer l'installation par défaut.
3. Vérifier les variables d'environnement système :
   * `JAVA_HOME` : `C:\Program Files\Java\jdk-17`
   * `Path` : placer `C:\Program Files\Java\jdk-17\bin` en tête de liste.

---

## 2. Chocolatey (CLI)

Pour installer le CLI `choco` :

* **Via MSI :** Télécharger le fichier `.msi` dans les assets de la dernière version sur [GitHub Releases Chocolatey](https://github.com/chocolatey/choco/releases) et l'exécuter.
* **Ou via PowerShell (Admin) :**
  ```powershell
  Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
  ```

---

## 3. Maven et HTTPie

Ouvrir un terminal (Admin) et exécuter :

```cmd
choco install maven httpie -y
```

---

## 4. Vérification

Dans un nouveau terminal :

```cmd
javac --version
mvn --version
http --version
```

---

## 5. Validation de l'étape A0 (`date-demo`)

### 5.1 Lancer le service
Dans un premier terminal :
```cmd
cd service
mvn clean package
mvn exec:java
```
Le service écoute sur `http://localhost:8081/date` (WSDL sur `http://localhost:8081/date?wsdl`). Laisser ce terminal ouvert.

### 5.2 Créer le fichier `requete.xml`
À la racine ou dans le dossier d'exécution, créer un fichier `requete.xml` avec le corps de la requête SOAP :
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Body>
    <formaterDate xmlns="http://date.hai704i.umontpellier.fr/">
      <format xmlns="">JMA</format>
    </formaterDate>
  </soapenv:Body>
</soapenv:Envelope>
```

### 5.3 Tester la requête avec HTTPie
Dans un second terminal :
```cmd
http POST http://localhost:8081/date Content-Type:text/xml < requete.xml
```
*(ou sous CMD si la redirection `<` pose problème : `type requete.xml | http POST http://localhost:8081/date Content-Type:text/xml`)*.  
Une réponse `200 OK` avec `<date>...</date>` confirme le bon fonctionnement du service.

### 5.4 Lancer le client généré
Dans le second terminal :
```cmd
cd client
mvn clean package
mvn exec:java
```
Maven compile le client, génère les classes via `wsimport` et affiche la date récupérée via SOAP.
