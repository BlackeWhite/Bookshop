<p align="center">
<img src="./WebContent/resources/img/logo.png" width="40%">
</p>

Progetto di Programmazione Avanzata  
Si vuole progettare un’applicazione web per l'acquisto di libri rivolto sia ad utenti che vogliono acquistare libri sia a librerie che vogliono mettere in vendità i propri libri.  
Vi sono 4 tipologie di utenti:
 * **Utente non registarto**: può visionare tutti i libri ed effettuare l’operazione di registrazione al sito, o login se già iscritto precedentemente.
  * **Utente acquirente (USER)**: accede mediante login, dispone delle stesse operazioni dell’**Utente non registarto**, può aggiungere uno o più libri al carrello, acquistare i libri presenti nel carrello, visualizzare gli acquisti effettuati nella cronologia acquisti, modificare i suoi dati personali di registrazione, aggiungere carte di pagamento ed effettuare l’operazione di logout per passare allo stato di **Utente non registarto**;  
  * **Utente venditore(SELLER)**: per registrarsi deve inviare una mail all'admin con tutte le informazioni sull'azienda. Accede attraverso login e viene reindirizzato alla sua Area personale dove può inserire/modificare/cancellare un libro, modificare le informazioni sugli autori dei suoi libri e visualizzare informazioni sulle vendite dei suoi libri visualizzando copie vendute e incasso totale per tutti i libri da lui venduti, per un singolo libro o per un intervallo di tempo ben definito.
  * **Admin del sito(ADMIN)**: é l'amministratore del sito. Può aggiungere gli **utenti venditore**, eliminare l'**utente acquirente** e/o l'**utente venditore**, aggiungere/eliminare un genere, generare un coupon e verificarne l'utilizzo da parte degli utenti. 

# Pre-requisiti


```
Java 8 (Open JDK 1.8.xxx)
Tomcat 9.0 (fondamentale per il corretto funzionamente dell'applicazione)
MySQL o MariaDB
```

# Guida all'installazione 

1. Eseguire l'operazione di clone del progetto;
2. In *src/main/resources* modificare il file *db.config.properties* inserendo i dati del proprio database;
3. Avviare l'applicazione seguendo una delle seguenti modalità:
   * Da Eclipse utilizzando Maven:
     * modificare nel pom.xml i seguenti campi in base ai dati di configurazione del proprio server 
     ```
        <groupId>org.apache.tomcat.maven</groupId>
          <artifactId>tomcat7-maven-plugin</artifactId>
          <version>2.2</version>
        <configuration>
              <url>http://localhost:8080/manager/text</url>
              <server> inserire qui il nome del server Tomcat </server>  
              <path>/bookshop</path>
               <!--  modificare in base alle proprie credenziali di tomcat -->
              <username> inserire username di Tomcat </username>
              <password> inserire password di Tomcat </password>
         </configuration>
     ```
     * avviare il server Tomcat
     * creare un profilo Maven Build in Run as--->Run configurations, definendo il goals: "clean tomcat7:redeploy", ed eseguirlo
     (in questo caso saranno eseguiti tutti gli unit test prima dell'avvio dell'applicazione)
   * Da Eclipse lanciare il progetto come Run as--->Run on Server utilizzando un server Tomcat (dopo averlo configurato) 
       (in questo caso gli unit test definiti tramite Junit non saranno eseguiti automaticamente ma potranno essere lanciati singolarmente)
   
4. Aprire il browser e digitare il seguente url http://localhost:8080/bookshop/ (la porta potrebbe essere diversa a seconda della porta utlizzata da Tomcat) per accedere al sito 
5. Digitare il seguente url http://localhost:8080/bookshop/populatedb per popolare il database al fine di testare l'utilizzo del sito con una quantità significativa di dati; saranno inseriti diversi libri con i relativi autori, gli utenti dell'applicazione (2 venditori, 2 utenti, 1 admin) e due codici coupon di test.

## Credenziali utenti applicazione
* Admin (ADMIN)
  * username: admin - password: admin
* Venditore (SELLER) 
  * username: libreria - password: 1234
  * username: Mondadori - password: 1234
* Utente (USER) (acquirente - unica persona che può acuqistare sul sito)
  * username: user1 - password: 5678
  * username: user2 - password: 0000
  
## Coupon di test
  (da inserire in fase di checkout dei libri)
  * EXTRASCONTO15 - sconto del 15%
  * ESTATE2021 - sconto del 10%

# Autori
* **Amal Benson Thaliath** - [ABTCoder](https://github.com/ABTCoder)
* **Gianluca Albanese** - [BlackeWhite](https://github.com/BlackeWhite)
* **Alessandro Manilii** - [AlessandroManilii](https://github.com/AlessandroManilii)
* **Agostino Dati** - [agostinodati](https://github.com/agostinodati)
