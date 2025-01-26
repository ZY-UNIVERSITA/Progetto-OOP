# Relazione di Progetto: Gestore di password

Questo progetto, sviluppato per il corso di PSS 2024/25, si propone di creare un'applicazione di gestione delle password.

## Analisi

L'applicazione mira a garantire una protezione avanzata dei dati sensibili degli utenti.

### Requisiti Funzionali

- [RF 1]: Registrazione degli utenti:
    1. Gli utenti devono poter creare un account fornendo uno username e una password.
    2. Il sistema dovrà guidare l'utente nella scelta di una password sicura, garantendo che rispetti parametri specifici, come la presenza di lettere maiuscole, numeri e caratteri speciali.

- [RF 2]: Autenticazione degli utenti:
    1. L'accesso al sistema sarà possibile tramite una combinazione di username e password.
    2. Il sistema dovrà verificare che la password inserita corrisponda a quella associata all’utente.

- [RF 3]: Gestione delle credenziali salvate:
    1. Gli utenti devono poter archiviare, visualizzare, modificare ed eliminare le credenziali associate ai vari servizi online.
    2. Il sistema permetterà di aggiungere informazioni aggiuntive alle credenziali, come email e note.

- [RF 4]: Sicurezza dei dati
    1. I dati sensibili, come username e password, non saranno mai memorizzati in chiaro.
    2. I dati saranno temporaneamente decriptati in memoria solo quando necessario.

- [RF 5]: Funzionalità di ricerca _(opzionale)_.
    1. Gli utenti devono poter cercare rapidamente le credenziali salvate per un servizio specifico utilizzando una barra di ricerca.

- [RF 6]: Autenticazione a due fattori (2FA) _(opzionale)_.
    1. Sarà offerta un’opzione per aggiungere un ulteriore livello di sicurezza, tramite autenticazione a due fattori.

- [RF 7]: Generazione di password sicure _(opzionale)_.
    1. Gli utenti potranno generare automaticamente password complesse e sicure.

- [RF 8]: Backup e ripristino dei dati _(opzionale)_.
    1. Il sistema offrirà la possibilità di creare backup dei dati in formato criptato e di ripristinarli in caso di necessità.

### Requisiti Non Funzionali

- [RNF 1]: _Sicurezza_. L'applicazione deve garantire un alto livello di sicurezza, proteggendo i dati sensibili degli utenti durante tutte le operazioni.

- [RNF 2]: _Efficienza_. L'applicazione dovrà essere ottimizzata per funzionare su dispositivi con risorse limitate.

- [RNF 3]: _Interfaccia user-friendly_. L’interfaccia grafica dovrà essere intuitiva e semplice da utilizzare, anche per utenti non esperti.

- [RNF 4]: _Operatività offline_. L’applicazione funzionerà completamente offline, eliminando il rischio di esposizione dei dati tramite connessioni esterne.

- [RNF 5]: _Open-source_. L'applicazione sarà open-source, consentendo agli utenti e agli sviluppatori di verificare il codice e personalizzarlo in base alle proprie esigenze.


## Analisi e modello del Dominio

Il dominio dell'applicazione riguarda la gestione sicura di credenziali per l'accesso a diversi servizi online.
Gli elementi principali sono:
1. **Utente (UserAccount)**  
    Un utente rappresenta una persona che vuole gestire in modo sicuro le proprie credenziali per diversi servizi.
    Ogni utente ha un identificativo univoco (come nome utente o email), una password principale (utilizzata per derivare una chiave segreta), e un insieme di servizi a cui accede.

2. **Servizio (Service)**  
    Un servizio è una risorsa digitale per la quale l'utente dispone di credenziali di accesso.
    Ogni servizio ha un nome e memorizza dati come username e password cifrata.

3. **Autenticazione (AccountManager)**  
    Per accedere ai propri dati l'utente deve essere autenticato.
    Può avvenire tramite una password principale e può essere rafforzata con un sistema di autenticazione a due fattori (2FA).

4. **Crittografia (CryptoManager)**  
    I dati devono essere protetti attraverso meccanismi di crittografia.
    Ogni servizio memorizza credenziali cifrate utilizzando una chiave derivata dalla password principale dell'utente.

5. **Backup (BackupManager)**  
    Gli utenti possono creare e ripristinare backup delle loro credenziali per evitare la perdita dei dati.
    Il backup deve essere cifrato per mantenere la sicurezza.

6. **Autenticazione a Due Fattori (TwoFactorAuthManager)**  
    Per aumentare la sicurezza, oltre alla password principale, potrebbe essere richiesto un codice OTP.

Gli elementi costitutivi sono sintetizzati nella seguente figura.

```mermaid

classDiagram

    class AccountManager {
        + login(username: string, password: char[]): boolean
        + logout(): boolean
    }

    class UserAccount {
        + getUsername(): string
        + getMasterKey(): KeySpec
        + getSalt(): byte[]
    }

    class Service {
        + getName(): string
        + getUsername(): string
        + getEncryptedPassword(): byte[]
    }

    class CryptoManager {
        + deriveMasterKey(password: string, salt: byte[]): KeySpec
        + encrypt(data: byte[], key: KeySpec): byte[]
        + decrypt(data: byte[], key: KeySpec): byte[]
    }
    
    class BackupManager {
        + createBackup(userAccount: UserAccount, services: List~Service~): void
        + restoreBackup(backupFile: File, userAccount: UserAccount): void
    }

    class TwoFactorAuthManager {
        + sendOTP(username: string): string
        + verifyOTP(username: string, otp: string): boolean
    }

    AccountManager "1" --> "1" UserAccount : manages
    UserAccount "1..*" *-- "1" Service : manages
    UserAccount "1" --> "1" CryptoManager : uses
    UserAccount "0..*" --> "1" BackupManager : creates/restore backup
    UserAccount "1" --> "1" TwoFactorAuthManager : uses
    Service "1" --> "1" CryptoManager : encrypts/decrypts
    BackupManager "1" --> "0..*" UserAccount : backups/restore
    BackupManager "1" --> "0..*" Service : backups/restore

```

**Difficoltà Principali**  
_Gestione sicura delle credenziali:_ Il problema principale è garantire che la memorizzazione e il recupero delle credenziali avvengano in modo sicuro, evitando accessi non autorizzati.  
_Derivazione sicura della chiave principale:_ È essenziale scegliere algoritmi di derivazione delle chiavi robusti per proteggere i dati.  
_Sicurezza nei backup:_ Deve essere garantito che i backup non compromettano la sicurezza delle credenziali.  
_Autenticazione a due fattori:_ L'integrazione con un sistema 2FA deve essere gestita in modo efficace senza compromettere l'usabilità.

# Design


## Architettura

```mermaid
classDiagram

```

## Design dettagliato

# Sviluppo

## Testing automatizzato

## Note di sviluppo

# Commenti finali

## Autovalutazione e lavori futuri

# Guida utente

