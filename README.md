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

Modello completo

```mermaid
classDiagram

%% ===================
%% CLASSI PRINCIPALI
%% ===================

class AccountManager {
    - CryptoManager cryptoManager
    - FileManager fileManager
    - SessionManager sessionManager
    - ServiceManager serviceManager

    + AccountManager(cryptoManager: CryptoManager, fileManager: FileManager, sessionManager: SessionManager, serviceManager: ServiceManager)
    + login(username: string, password: char[]): boolean
    + logout()
    + register(username: string, password: char[])
    + changePassword(oldPassword: char[], newPassword: char[])
}

class UserAccount {
    - string username
    - byte[] salt
    - AlgorithmConfig derivationConfig
    - KeySpec masterKey

    + UserAccount(username: string, salt: byte[], derivationConfig: AlgorithmConfig, masterKey: KeySpec)
    + getUsername(): string
    + setUsername(username: string)
    + getMasterKey(): KeySpec
    + setMasterKey(masterKey: KeySpec)
    + getSalt(): byte[]
    + setSalt(salt: byte[])
    + getDerivationConfig(): AlgorithmConfig
    + setDerivationConfig(config: AlgorithmConfig)
}

class ServiceManager {
    - List~Service~ services
    - CryptoManager cryptoManager
    - FileManager fileManager

    + ServiceManager(cryptoManager: CryptoManager, fileManager: FileManager)
    + addService(service: Service)
    + removeService(serviceName: string)
    + modifyService(serviceName: string, newService: Service)
    + getServices(): List~Service~
    + loadServices(key: KeySpec)
    + saveServices(key: KeySpec)
}

class Service {
    - string name
    - string username
    - string email
    - byte[] encryptedPassword
    - AlgorithmConfig encryptionConfig
    - string info

    + Service(name: string, username: string, email: string, encryptedPassword: byte[], encryptionConfig: AlgorithmConfig, info: string)
    + getName(): string
    + setName(name: string)
    + getUsername(): string
    + setUsername(username: string)
    + getEncryptedPassword(): byte[]
    + setEncryptedPassword(encryptedPassword: byte[])
    + getEncryptionConfig(): AlgorithmConfig
    + setEncryptionConfig(config: AlgorithmConfig)
}

class SessionManager {
    - UserAccount currentUser

    + SessionManager()
    + getCurrentUser(): UserAccount
    + setCurrentUser(user: UserAccount)
    + clearSession()
}

class FileManager {
    + FileManager()
    + loadUserData(username: string): UserAccount
    + saveUserData(userAccount: UserAccount)
    + loadServicesFile(): byte[]
    + saveServicesFile(encryptedData: byte[])
}


%% =====================
%% GESTIONE ALGORITMI
%% =====================

class AlgorithmConfig {
    - string algorithmType  %% Esempio: "DERIVATION", "ENCRYPTION", "HMAC" ...
    - string algorithmName  %% Esempio: "Argon2", "AES-GCM" ...
    - Map~string, string~ parameters

    + AlgorithmConfig(algorithmType: string, algorithmName: string, parameters: Map~string, string~)
    + getAlgorithmType(): string
    + getAlgorithmName(): string
    + getParameter(key: string): string
    + setParameter(key: string, value: string)
}

class CryptoManager {
    + CryptoManager()
    + deriveMasterKey(password: char[], salt: byte[], derivationConfig: AlgorithmConfig): KeySpec
    + encrypt(data: byte[], key: KeySpec, encryptionConfig: AlgorithmConfig): byte[]
    + decrypt(data: byte[], key: KeySpec, encryptionConfig: AlgorithmConfig): byte[]
}

class KeyDerivationAlgorithm {
    <<interface>>
    + deriveKey(source: char[], salt: byte[], config: AlgorithmConfig): KeySpec
    + deriveKey(source: KeySpec, salt: byte[], config: AlgorithmConfig): KeySpec
}

class EncryptionAlgorithm {
    <<interface>>
    + encrypt(data: byte[], key: KeySpec, config: AlgorithmConfig): byte[]
    + decrypt(data: byte[], key: KeySpec, config: AlgorithmConfig): byte[]
}

%% =====================
%% RELAZIONI TRA CLASSI
%% =====================

%% AccountManager usa i vari manager
AccountManager --> CryptoManager : uses
AccountManager --> FileManager : uses
AccountManager --> SessionManager : uses
AccountManager --> ServiceManager : uses

%% AccountManager carica/crea l'UserAccount
AccountManager --> UserAccount : load/create

%% ServiceManager gestisce i Service e usa CryptoManager/FileManager
ServiceManager *-- Service : composition
ServiceManager --> CryptoManager : uses
ServiceManager --> FileManager : uses

%% SessionManager mantiene un riferimento ad un solo UserAccount
SessionManager o-- UserAccount : holds

%% FileManager carica/salva UserAccount e file dei servizi
FileManager --> UserAccount : loads/saves
FileManager --> Service : loads/saves

%% UserAccount e Service possiedono la configurazione dell'algoritmo associata
UserAccount --> AlgorithmConfig : "derivationConfig"
Service --> AlgorithmConfig : "encryptionConfig"

%% CryptoManager utilizza i parametri passati (config) e internamente crea un KeyDerivationAlgorithm o EncryptionAlgorithm
CryptoManager --> KeyDerivationAlgorithm : uses
CryptoManager --> EncryptionAlgorithm : uses

```

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

## Difficoltà incontrate e commenti per i docenti

**opzionale**

# Guida utente

