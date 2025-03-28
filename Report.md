# Relazione di Progetto: Gestore di password

Questo progetto, sviluppato per il corso di PSS 2024/25, si propone di creare un'applicazione di gestione delle password.

## Analisi

L'applicazione mira a garantire una protezione avanzata dei credenziali degli utenti in un unico posto.

### Requisiti Funzionali

- [RF 1]: Registrazione degli utenti:
    1. Gli utenti devono poter creare un account definendo un nome utente e una password.

- [RF 2]: Autenticazione degli utenti:
    1. Sarà possibile accedere all'app tramite inserimento dei propri username e password.
    2. Il sistema dovrà poi essere in grado di verificare che la password inserita sia uguale a quella dell’utente corrispondente.

- [RF 3]: Gestione delle credenziali salvate:
    1. Gli utenti devono poter gestire i propri servizi, ossia archiviarli, visualizzarli, modificarli ed infine eliminarli.
    2. L'applicazione permetterà di aggiungere informazioni aggiuntive alle credenziali, come per esempio le note.

- [RF 4]: Sicurezza dei dati
    1. I dati sensibili, come username e password, non saranno mai memorizzati in chiaro.
    2. I dati saranno temporaneamente decriptati in memoria solo quando necessario.

- [RF 5]: Funzionalità di ricerca _(opzionale)_.
    1. Gli utenti finali avranno la possibilità di cercare un servizio specifico utilizzando una barra di ricerca.

- [RF 6]: Autenticazione a due fattori (2FA) _(opzionale)_.
    1. Ci sarà un’opzione per aggiungere un ulteriore livello di sicurezza, tramite autenticazione a due fattori.

- [RF 7]: Generazione di password sicure _(opzionale)_.
    1. Si potrà generare automaticamente password complesse e sicure, che rispettino parametri specifici, come la presenza di lettere maiuscole, numeri e caratteri speciali.

- [RF 8]: Backup e ripristino dei dati _(opzionale)_.
    1. Il gestore di password offrirà la possibilità di creare backup dei dati in formato criptato e di ripristinarli.

### Requisiti Non Funzionali

- [RNF 1]: L'applicazione deve garantire un alto livello di sicurezza durante tutte le operazioni.

- [RNF 2]: Il gestore dovrà essere efficiente nell'uso delle risorse.

- [RNF 3]: L’interfaccia grafica dovrà essere intuitiva e semplice da utilizzare.

- [RNF 4]: L’app dovrà essere in grado di funzionare completamente offline.

- [RNF 5]: Il progetto sarà open-source, consentendo miglioramenti futuri a tutti.


## Analisi e modello del Dominio

Il dominio dell'applicazione riguarda la gestione sicura di credenziali per l'accesso a diversi servizi online.
Gli elementi principali sono:
1. **Utente (UserAccount)**  
    E' un'entità principale che possiede un identificativo univoco (come nome utente o email) e una chiave segreta derivata dalla password principale.

2. **Servizio (Service)**  
    Ogni utente gestisce uno o più servizi, caratterizzati da un nome, uno username, un'email, informazioni aggiuntive e una password cifrata.

3. **Autenticazione (AccountManager)**  
    Parte responsabile dell'accesso al sistema che avvenire tramite inserimento di un username e una password.
    Può essere rafforzato con un sistema di autenticazione a due fattori (2FA).

4. **Crittografia (CryptoManager)**  
    I dati devono essere protetti attraverso meccanismi di crittografia.
    Ogni servizio memorizza credenziali cifrate utilizzando una chiave derivata dalla password principale dell'utente.

5. **Backup (BackupManager)**  
    Entità per la creazione e ripristino dei backup delle credenziali salvate.
    I dati devono essere cirfati per mantenere la sicurezza.

6. **Autenticazione a Due Fattori (TwoFactorAuthManager)**  
    Per aumentare la sicurezza, oltre alla password principale, potrebbe essere richiesto un codice OTP.

### Eliminare o aggiornare
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
_Gestione sicura delle credenziali:_ Garantire che la memorizzazione e il recupero delle credenziali avvengano in modo sicuro.  
_Derivazione sicura della chiave principale:_ È essenziale scegliere algoritmi di derivazione delle chiavi robusti per proteggere i dati.  
_Sicurezza nei backup:_ Deve essere garantito che i backup non compromettano la sicurezza delle credenziali.  
_Autenticazione a due fattori:_ L'integrazione con un sistema 2FA deve essere gestita in modo efficace senza compromettere l'usabilità.

# Design

## Architettura
Il sistema è fatto da 3 componenti principali, che seguono il pattern architetturale Model-View-Controller (MVC):
- **Model:** Comprende le classi principali che rappresentano il dominio dell'applicazione (gestione servizi, crittografia, backup, ecc.). Sono responsabili della gestione delle informazioni memorizzate e della logica del gestore. Il componente primario è `ServiceManager`, che si occupa della gestione di tutti i servizi.
- **View:** Si occupa dell'interfaccia grafica e dell'interazione con l'utente. Le principali classi sono `LoginView`, `MainView` e `ServiceManagerView`, che rappresentano schermate diverse dell’applicazione. Ogni vista comunica con il rispettivo controller per gestire le operazioni richieste.
- **Controller:** Funge da intermediario tra Model e View, leggendo le azioni dell'utente e aggiornando lo stato dell'app. `LoginController`, `MainController` e `ServiceManagerController` gestiscono la logica di interazione delle rispettive viste, facendo uso dei manager del Model.

In particolare, quando si fa un login `LoginView` invoca `LoginController`, che utilizza `AccountManager` per verificare le credenziali. Se tutto va bene, `ViewNavigator` cambia la vista su `MainView`. `MainView` comunica con `MainController`, che utilizza `SessionManager` per mantenere lo stato dell’utente connesso. `ServiceManagerView` mostra i servizi disponibili e passa le richieste a `ServiceManagerController`, che esegue operazioni richieste su `ServiceManager`.

```mermaid

classDiagram

    %% Model
    class AccountManager

    class ServiceManager

    class SessionManager

    %% View
    class LoginView {
        <<Concept>>
        + initialize() void
    }

    class RegistrationView {
        <<Concept>>
        + initialize() void
    }

    class MainView {
        <<Concept>>
        + initialize() void
    }

    class ServiceManagerView {
        <<Concept>>
        + initialize() void
    }

    %% Controller
    class LoginController {
        + handleLogin() void
        + HandleRegister() void
    }

    class Login2FAController {
        + handleLogin() void
    }

    class RegisterController {
        + handleRegister() void
    }

    class MainController {
        + handleLogout() void
    }

    class ServiceManagerController {
        + handleAddService() void
        + handleRemoveService() void
        + handleModifyService() void
    }

    class GenericNavigator {
        + navigateTo() void
    }

    LoginView --> LoginController : controller
    RegistrationView --> RegisterController: controller
    MainView --> MainController : controller
    ServiceManagerView --> ServiceManagerController : controller

    LoginController ..> AccountManager : usa
    MainController ..> SessionManager : usa
    ServiceManagerController ..> ServiceManager : usa

    GenericNavigator --> LoginView
    GenericNavigator --> MainView
    GenericNavigator --> ServiceManagerView
    GenericNavigator --> RegistrationView

    LoginController --> Login2FAController : 2FA

    LoginController ..> GenericNavigator : change view
    Login2FAController --> GenericNavigator : change view
    RegisterController --> GenericNavigator: change view
    MainController ..> GenericNavigator : change view
    ServiceManagerController ..> GenericNavigator : change view
```

## Design dettagliato

#### Parte di Nataliia Skybun.

**1. Creazione dei Servizi**  
```mermaid

classDiagram

    class Service {
        - name: String
        - username: String
        - email: String
        - password: byte[]
        - encryptionConfig: AlgorithmConfig
        - info: String
    }

    class ServiceBuilder {
        + build(): Service
    }

    ServiceBuilder -- Service : constructs

```

**Problema**  
Gestore di password deve gestire vari tipi di servizi con un numero di campi. Inoltre, la password deve essere criptata prima di essere memorizzata, per evitare che vengano archiviati dati sensibili in chiaro.  
**Soluzione**  
Si è pensato di utilizzare il design pattern Builder in modo da semplificare la creazione dei nuovi servizi (evitando costruttori lunghi), assicurarsi che la password sia sempre criptata prima della memorizzazione e permette di avere una validazione dei campi centralizzata (ad esempio, evitando nomi vuoti o password nulle).  
_Pro:_ chiarezza nella creazione dei servizi; robustezza degli oggetti creati; sicurezza della password. _Contro:_ complessità aggiunta al sistema; possibile sovraccarico di memoria, per le classi intermedi.  
**Pattern**  
_Service:_ rappresenta il servizio con i dati sensibili. _ServiceBuilder:_ consente la costruzione di un Service in modo controllato. Durante la creazione cifra la password usando CryptoManager.

**2. Gestione dei servizi**  
```mermaid

classDiagram

    class ServiceManager {
        + getInstance(): ServiceManager
        + getServices(): List<Service>
        + selectService(serviceName: String): Service
        + addService(service: Service): boolean
        + removeService(serviceName: String): boolean
        + searchService(searchTerm: String): List<Service>
        + modifyService(serviceName: String, newService: Service): boolean
        + getDecryptedPassword(service: Service): String
        + generatePassword(length: int, useSpecialChar: boolean, useNumbers: boolean, useUpperCase: boolean, useLowerCase: boolean): char[]
        + loadServices(): boolean
        + saveServices(): void
        + destroy(): void
    }

    class Service {
        - name: String
        - username: String
        - email: String
        - password: byte[]
        - encryptionConfig: AlgorithmConfig
        - info: String
    }

    ServiceManager -- Service : manage

```
**Problema**  
L'applicazione richiede un manager che gestisca un elenco di servizi legati all'utente, con funzionalità per l'aggiunta, la modifica, la rimozione e la ricerca di servizi. Inoltre, i dati dei servizi devono essere protetti tramite crittografia e memorizzati in un file sicuro. Utilizza anche il gestore di password per creare password sicure in caso di necessità.  
**Soluzione**  
Si è deciso di implementare la classe come un singleton per evitare istanze multiple e garantire un'unica gestione dei servizi in tutta l'app.  
_Pro:_ sicurezza; gestione centralizzata. _Contro:_ difficile da testare; evoluzione limitata.  
**Pattern**  
_ServiceManager_ con il metodo getInstance() assicura che esista solo una singola istanza della classe durante l'esecuzione dell'app.

**3. Generatore di password sicure**  
```mermaid

classDiagram

    class PasswordGenerator {
        + generatePassword(length: int, useSpecialChar: boolean, useNumbers: boolean, useUpperCase: boolean,  useLowerCase: boolean): char[]
    }

    ServiceManager --> PasswordGenerator : usa

```
**Problema**  
Era necessario uno strumento che generi password complesse, sicure e conformi a specifici parametri (lunghezza, presenza di caratteri speciali, numeri e lettere maiuscole / minuscole) per evitare password banali ed evitare una vulnerabilità.  
**Soluzione**  
Si è progettato un modulo dedicato alla generazione delle password che accetta in input un numero che indica la lunghezza della password desiderata (con un minimo di 12 caratteri) e i valori booleani che indicano se utilizzare o meno un tipo di carattere.

**4. Sistema di backup e ripristino**
```mermaid

classDiagram

    class BackupManager {
        + createBackup(List~Service~: services): byte[]
        + restoreBackup(backupFile: File, accountManager: AccountManager, password: char[], salt: byte[]): void
    }

    BackupManager --> FileManager : usa
    BackupManager --> UserAccount : backup/restore
    BackupManager --> Service : backup/restore

```
**Problema**  
Il sistema deve essere in grado di creare e ripristinare backup criptati di dati sensibili associati ai servizi e alle informazioni utente.  
**Soluzione**  
Creazione di una classe _BackupManager_ che gestisce centralmente la creazione e il ripristino dei dati. Viene generato una password lunga e complessa assieme a un salt per la crittografia. L'utente deve memorizzare questi dati in modo da effettuare il ripristino quando necessario. In questo modo si riesce a garantire una maggior robustezza al sistema, dato che contiene i dati altamente sensibili.  



#### Parte di Yuhang Zhu.  
**1. Account utente e authentication data**
```mermaid

classDiagram

%% Interfaces
class MustBeDestroyed {
  <<interface>>
  +destroy() void
}

%% Abstract classes
class UserAccountAbstract {
  <<abstract>>
  + getUsername() String
  + setUsername(username: String) void
}

%% Concrete classes
class UserAccount {
  + getMasterKey() byte[]
  + setMasterKey(masterKey: byte[]) void
}

class UserAuthInfo {
  + getPasswordDerivedKeyConfig() AlgorithmConfig
  + setPasswordDerivedKeyConfig(cfg: AlgorithmConfig) void
  + getKeyStoreEncryptionConfig() AlgorithmConfig
  + setKeyStoreEncryptionConfig(cfg: AlgorithmConfig) void
  + getKeyStoreConfigEncryptedData() byte[]
  + setKeyStoreConfigEncryptedData(data: byte[]) void
  + getServiceConfigEncryptedData() byte[]
  + setServiceConfigEncryptedData(data: byte[]) void
  + isEnabled2FA() boolean
  + setEnabled2FA(enabled: boolean) void
}

class UserAuthKey {
  + getPassword() byte[]
  + setPassword(password: byte[]) void
  + getPasswordDerivedKey() byte[]
  + setPasswordDerivedKey(key: byte[]) void
  + getTotpEncryptionKey() byte[]
  + setTotpEncryptionKey(key: byte[]) void
  + getTotpKey() byte[]
  + setTotpKey(key: byte[]) void
  + getServiceConfigKey() byte[]
  + setServiceConfigKey(key: byte[]) void
}

class CollectedUserData {
    +byte[] getPassword()
    +void setPassword(byte[] password)
    +byte[] getConfirmPassword()
    +void setConfirmPassword(byte[] confirmPassword)
    +AlgorithmConfig getPasswordDerivationConfig()
    +void setPasswordDerivationConfig(AlgorithmConfig passwordDerivationConfig)
    +AlgorithmConfig getKeyStoreConfigEncryptionConfig()
    +void setKeyStoreConfigEncryptionConfig(AlgorithmConfig keyStoreConfigEncryptionConfig)
    +boolean isEnabled2FA()
    +void setEnabled2FA(boolean enabled2fa)
    +byte[] getTotpKey()
    +void setTotpKey(byte[] totpKey)
}

%% Relationships
MustBeDestroyed <|.. UserAccountAbstract : implements
MustBeDestroyed <|.. UserAuthKey : implements
MustBeDestroyed <|.. CollectedUserData: implements
UserAccountAbstract <|-- UserAccount : extends
UserAccountAbstract <|-- UserAuthInfo : extends

```

**Problema**  
Il sistema gestisce in modo sicuro i dati di autentication e i dati di sessione dell'utente correntemente loggato.

**Soluzione**  
Tutti i dati utenti implementano direttamente oppure indirettamente tramite ereditarietà, un interfaccia comune che richiede l'implementazione di un metodo che permetta di eliminare i dati di sessione e i dati di autenticazione.
La presenza di almeno 2 classi che richiedano dei dati comuni, ha portato all'utilizzo di una classe astratta che ponga le basi per tutte le classi che richiedano una porzione di proprietà comuni.
La classe **UserAuthKey** contiene temporaneamente le chiavi intermedie dalle quali verranno generate la master key che verrà usata per cifrare e decifrare le password utenti.
La classe **UserAccount** conttiene le 2 informazioni di base essenziali: username e master key.
La clase **UserAuthInfo** contiene le informazioni riguardanti il singolo utente e che permettono di ottenere la master key e la lista delle chiavi dell'utente.
Queste classi vengono usati come DTO per trasferire dati da una classe all'altra.

**2. Account manager**
```mermaid

classDiagram
    %% Interfaces
    class AccountManager {
        <<interface>>
        +register(CollectedUserData data)
        +login(CollectedUserData data)
        +logout()
        +getSessionManager() SessionManager
        +getServiceManager() ServiceManager
    }

    class SessionManager {
        <<interface>>
        +getUserAuthInfo() UserAuthInfo
        +setUserAuthInfo(UserAuthInfo info)
        +getUserAuthKey() UserAuthKey
        +setUserAuthKey(UserAuthKey key)
        +getKeyStoreConfig() KeyStoreConfig
        +setKeyStoreConfig(KeyStoreConfig config)
        +getServiceConfig() ServiceCryptoConfig
        +setServiceConfig(ServiceCryptoConfig config)
        +getUserAccount() UserAccount
        +setUserAccount(UserAccount account)
    }

    class MustBeDestroyed {
        <<interface>>
        +destroy()
    }

    class RegistrationService {
        <<interface>>
        +register(CollectedUserData collectedUserData)
    }

    class LoginService {
        <<interface>>
        +login(CollectedUserData collectedUserData)
    }

    %% Concrete classes
    class DefaultAccountManager {
        +setSessionManager(SessionManager sessionManager)
        +setServiceManager(ServiceManager serviceManager)
    }

    class DefaultSessionManager {
    }

    class ServiceManager {
        
    }

    class KeyStoreConfig {
        +byte[] getKeyStoreEncryptionKey()
        +char[] getKeyStoreEncryptionKeyChar()
        +byte[] getSaltWithPasswordDerived()
        +char[] getSaltWithPasswordDerivedChar() 
        +byte[] getSaltWithTotpEncryptionKey()
        +char[] getSaltWithTotpEncryptionKeyChar()
        +byte[] getServiceDecryptionSalt()
        +void setKeyStoreEncryptionKey(byte[] keyStoreEncryptionKey)
        +void setSaltWithPasswordDerived(byte[] saltWithPasswordDerived)
        +void setSaltWithTotpEncryptionKey(byte[] saltWithTotpEncryptionKey)
        +void setServiceDecryptionSalt(byte[] serviceDecryptionSalt)
        +byte[] getSaltForHKDF()
        +void setSaltForHKDF(byte[] saltForHKDF)
    }

    class ServiceCryptoConfig {
        +char[] getFileName()
        +void setFileName(char[] fileName)
        +byte[] getSaltForHKDF()
        +void setSaltForHKDF(byte[] saltForHKDF)
        +byte[] getSaltForServiceEncryption()
        +void setSaltForServiceEncryption(byte[] serviceSalt)
    }

    class CollectedUserData {

    }

    %% Relationships
    AccountManager <|.. DefaultAccountManager : implements
    SessionManager <|.. DefaultSessionManager : implements
    MustBeDestroyed <|.. DefaultSessionManager : implements
    MustBeDestroyed <|.. KeyStoreConfig : implements
    MustBeDestroyed <|.. ServiceCryptoConfig : implements

    DefaultAccountManager --o SessionManager : uses
    DefaultAccountManager --o ServiceManager : uses
    DefaultAccountManager --o RegistrationService : uses
    DefaultAccountManager --o LoginService : uses
    DefaultSessionManager --o KeyStoreConfig : uses
    DefaultSessionManager --o ServiceCryptoConfig : uses

    CollectedUserData <.. AccountManager : uses
    CollectedUserData <.. RegistrationService: uses
    CollectedUserData <.. LoginService : uses 

```

**Problema**
Il sistema di autenticazione deve gestire le fasi autenticazione dell'utente che comprendono login, registration e logout con relativa eliminazione dei dati sessione.

**Soluzione**
La soluzione adottata è la suddivisione delle classi in classi con responsabilità comuni e correlate, con ognuna delle quali che cerca di implementare i principi SOLID. Per rendere le classi più indipendenti, le classi dialogano tra di loro utilizzando delle interfacce.
L'interfaccia **AccountManager** richiede di implementare 3 metodi collegati all'autenticazione e alla gestione dell'utente: login, logout e registration utilizzando i dati collezionati dalla view. 
Esso viene implementato dalla classe concreta **DefaultAccountManager** che utilizza tramite aggregazione 4 classi sotto forma di interfacce: registrationService per gestire la fase di registrazione, loginService per gestire la fase di login, ServiceManager per gestire le password del singolo utente e SessionManager per gestire i dati personali di crittografia dell'account corrente.
La classe **DefaultSessionManager** implementa l'interfaccia **SessionManager** e viene usata per gestire tutti i dati di sicurezza dell'utente come la sua password oppure la master key usata per decifrare le password utente oppure le password intermedie che vengono usate per derivare la master key finale. Utilizza inoltre 2 classi con funzionalità di DTO perchè vengono usati solo contenere dati da trasferire tra classi.

**3. Login e registrazione**
```mermaid

classDiagram
    class LoginService {
        <<interface>>
        +login(CollectedUserData collectedUserData)
    }

    class DefaultLoginService {
        -loadUserPersonalData()
        -deriveKeyFromPassword()
        -loadKeyStore()
        -decryptServiceConfig()
        -deriveMasterKey()
        -loadUserPassword()
    }

    class RegistrationService {
        <<interface>>
        +register(CollectedUserData collectedUserData)
    }

    class DefaultRegistrationService {
        -insertingData()
        -getKeyFromPassword()
        -createKeyStore()
        -createServiceConfig()
        -encryptConfigData()
        -saveData()
        -createMasterKey()
        -loadUserPassword()
        +register(CollectedUserData)
    }


    class AuthenticationStep {
        <<interface>>
        +executeStep()
    }

    class AuthenticationStepAbstract {
        <<abstract>>
        +executeStep()*
        +getSessionManager()
        +setSessionManager(SessionManager sessionManager)
    }

    class AuthenticationCollectingStepAbstract {
        <<abstract>>
        +getCollectedUserData()
        +setCollectedUserData(CollectedUserData collectedUserData)
    }

    class InsertUserData
    class LoadUserData
    class DeriveKeyFromPassword
    class DecryptKeyStoreConfig
    class LoadKeyStore
    class GetKeyFromKeyStore
    class DeriveServiceConfigKey
    class DecryptServiceConfig
    class DeriveMasterKey
    class LoadUserPassword
    class InsertUserConfig
    class CreateKeyStore
    class CreateServiceConfig
    class EncryptKeyStoreConfig
    class EncryptServiceConfig
    class SaveKeyStore
    class SaveUserData
    class CloseKeyStore

    LoginService <|.. DefaultLoginService : implements
    RegistrationService <|.. DefaultRegistrationService : implements
    AuthenticationStep <|.. AuthenticationStepAbstract : implements

    AuthenticationStepAbstract <|-- AuthenticationCollectingStepAbstract : extends

    AuthenticationCollectingStepAbstract <|-- InsertUserData : extends
    AuthenticationCollectingStepAbstract <|-- InsertUserConfig : extends

    AuthenticationStepAbstract <|-- LoadUserData : extends
    AuthenticationStepAbstract <|-- DeriveKeyFromPassword : extends
    AuthenticationStepAbstract <|-- DecryptKeyStoreConfig : extends
    AuthenticationStepAbstract <|-- LoadKeyStore : extends
    AuthenticationStepAbstract <|-- GetKeyFromKeyStore : extends
    AuthenticationStepAbstract <|-- DeriveServiceConfigKey : extends
    AuthenticationStepAbstract <|-- DecryptServiceConfig : extends
    AuthenticationStepAbstract <|-- DeriveMasterKey : extends
    AuthenticationStepAbstract <|-- LoadUserPassword : extends
    AuthenticationStepAbstract <|-- CreateKeyStore : extends
    AuthenticationStepAbstract <|-- CreateServiceConfig : extends
    AuthenticationStepAbstract <|-- EncryptKeyStoreConfig : extends
    AuthenticationStepAbstract <|-- EncryptServiceConfig : extends
    AuthenticationStepAbstract <|-- SaveKeyStore : extends
    AuthenticationStepAbstract <|-- SaveUserData : extends
    AuthenticationStepAbstract <|-- CloseKeyStore : extends

    DefaultLoginService --* AuthenticationStep : composition
    DefaultRegistrationService --* AuthenticationStep : composition

```

**Problema**  
L'applicazione richiede di implementare la fase di login e la fase di registrazione.

**Soluzione**  
La soluzione è realizzata attraverso il pattern della **Chain of Responsibility**, implementato mediante un'interfaccia generica AuthenticationStep, la quale definisce il metodo executeStep(). 
La classe concreta DefaultLoginService realizza l’interfaccia LoginService e raccoglie in una lista ordinata gli step di autenticazione, eseguendoli in sequenza al momento del login, come ad esempio il caricamento dei dati utente, derivazione di chiavi crittografiche, apertura e decrittazione del keystore, configurazione dei servizi e derivazione della master key.
La classe concreta DefaultRegistrationService realizza l’interfaccia RegistrationService e raccoglie in una lista ordinata gli step di autenticazione, eseguendoli in sequenza al momento della registrazione, occupandosi dell’inserimento e salvataggio dei dati utenti, derivazione di chiavi dalla password, creazione e cifratura del keystore e della configurazione del servizio, e generazione della master key.
Questo permette il riutilizzo di alcuni step tra login e registration andando a semplificare e modularizzare il lavoro.

**4. Gestione del key store**
```mermaid

classDiagram
    class KeyStoreManager {
        <<interface>>
        +createNewKeyStore()
        +loadKeyStore()
        +createKeyStoreConfig()
        +createKeyStoreEntry()
        +populateNewKeyStore()
        +saveKeyStore()
        +closeKeyStore()
        +encryptConfig()
        +decryptConfig()
        +getKeyStoreKeys()
    }

    class DefaultKeyStoreManager {
        +getKeyStore() KeyStore
    }

    class KeyStoreConfigService {
        <<interface>>
        +generateKeyStoreConfigKey(KeyStoreConfig)
        +generateKeyStoreConfigSalt(KeyStoreConfig)
        +generateKeyStoreEntry(UserAuthKey)
        +encryptConfig(data: byte[], key: byte[], algorithmConfig: AlgorithmConfig) byte[]
        +decryptConfig(data: byte[], key: byte[], algorithmConfig: AlgorithmConfig) KeyStoreConfig
    }

    class DefaultKeyStoreConfigService {
        -generateAndSetKey(int passwordLength,  setterFunction: Consumer~byte[]~)
        -generateTotpEncryptionKey(setterFunction: Consumer~byte[]~)
        -generateTotpKey(setterFunction: Consumer~byte[]~)
    }

    class KeyStoreCreator {
        <<interface>>
        +createKeyStore(keyStorePassword: char[]) KeyStore
    }

    class DefaultKeyStoreCreator {
    }

    class KeyStoreEntryService {
        <<interface>>
        +retrieveKey(keyStore: KeyStore, alias: String, passwordToDecryptEntry: char[]) byte[]
        +insertKey(keyStore: KeyStore, alias: String, keyToInsert: byte[], passwordToEncryptEntry: char[])
    }

    class DefaultKeyStoreEntryService {
    }

    class KeyStoreStorageService {
        <<interface>>
        +loadKeyStore(input: byte[], keyStorePassword: char[]) KeyStore
        +saveKeyStore(filePath: Path, keyStore: KeyStore, keyStorePassword: char[])
        +closeKeyStore(keystore: KeyStore)
    }

    class DefaultKeyStoreStorageService {
    }

    class KeyStoreConfig {
        +serialize() byte[]
        +destroy()
        +getters/setters()
    }

    %% Relations
    KeyStoreManager <|.. DefaultKeyStoreManager: implements
    KeyStoreConfigService <|.. DefaultKeyStoreConfigService: implements
    KeyStoreCreator <|.. DefaultKeyStoreCreator: implements
    KeyStoreEntryService <|.. DefaultKeyStoreEntryService: implements
    KeyStoreStorageService <|.. DefaultKeyStoreStorageService: implements

    DefaultKeyStoreManager --* KeyStoreConfigService : composition
    DefaultKeyStoreManager --* KeyStoreCreator : composition
    DefaultKeyStoreManager --* KeyStoreStorageService : composition
    DefaultKeyStoreManager --* KeyStoreEntryService : composition
    DefaultKeyStoreManager --* SessionManager : composition

    DefaultKeyStoreManager --o FileManager : uses

    DefaultKeyStoreConfigService ..> KeyStoreConfig : uses
    DefaultKeyStoreCreator ..> KeyStoreConfig : uses
    DefaultKeyStoreEntryService ..> KeyStoreConfig : uses
    DefaultKeyStoreStorageService ..> KeyStoreConfig : uses

```

**Problema**
Il key store contiene le chiavi simmetriche dell'utente. Queste chiavi verrano usate per decriptare i file contenenti le password del singolo utente, quando vengono combinate insieme alla password utente. Quindi è necessario la gestione del keystore a partire dalla sua creazione, all'inserimento delle chiavi e alla possibilità di richiedere le chiavi.

**Soluzione**
La classe DefaultKeyStoreManager agisce come Facade, offrendo un'interfaccia semplice e unificata verso una serie di operazioni complesse. Questo pattern nasconde la complessità sottostante delle operazioni del KeyStore dietro metodi chiari e semplificati, richiamando le classi che si occupano delle operazioni a basso livello.
Ogni operazione sul key store è stato spezzato in classi separate ognuno dei quali implementa una interfaccia in modo tale da avere una flessibilità futura nel caso si volessero implementare diverse versioni del keystore. Quindi c'è la classe che si occupa di creare il keystore, una classe che si occupa di gestire la configurazione del keystore, chi si occupa di inserire e ottenere le chiavi e chi si occupa di salvare e fare il caricamento di essi.


**5. Gestione delle password utente**
```mermaid

classDiagram
    class ServiceCryptoConfigManager {
        +createServiceConfig()
        +encryptConfig()
        +decryptConfig()
    }
    
    class DefaultServiceCryptoConfigManager {
    }
    
    class ServiceCryptoConfigService {
        +createPasswordListConfig() : ServiceCryptoConfig
        +encryptConfig(data: byte[], key: byte[], algorithmConfig: AlgorithmConfig) byte[]
        +decryptConfig(data: byte[], key: byte[], algorithmConfig: AlgorithmConfig) byte[]
    }
    
    class DefaultServiceCryptoConfigService {
        -generateFileName(setterFunction: Consumer)
        -generateHkdfSalt(setterFunction: Consumer)
        -generateEncryptionSalt(setterFunction: Consumer)
    }
    
    class ServiceCryptoConfig {
        +serialize() byte[]
        +destroy()
    }
    
    ServiceCryptoConfigManager <|.. DefaultServiceCryptoConfigManager : implements
    ServiceCryptoConfigService <|.. DefaultServiceCryptoConfigService : implements
    ServiceCryptoConfig ..|> MustBeDestroyed : implements

    DefaultServiceCryptoConfigManager --* ServiceCryptoConfigService : uses

    DefaultServiceCryptoConfigService --* CryptoManager : composition
    DefaultServiceCryptoConfigManager --o SessionManager : uses
    
    ServiceCryptoConfigService ..> ServiceCryptoConfig : uses
    DefaultServiceCryptoConfigManager ..> ServiceCryptoConfig : uses

```

**Problema**
L'applicazione richiede la gestione sicura delle configurazioni crittografiche che vengono utilizzate per criptare/decriptare file contenenti informazioni sensibili in particolare le password dei servizi del singoli utente. In particolare, è presente la generazione della configurazione: 
Creare una configurazione che includa elementi di sicurezza quali un nome di file univoco e sali crittografici (uno per l’HKDF e uno per l’encryption).
Crittografia e Decrittografia: Fornire operazioni che, a partire da una configurazione, permettano di criptarla per proteggerla e successivamente decriptarla per il corretto utilizzo.

**Soluzione**
La soluzione proposta prevede una suddivisione in due componenti principali, ognuna con il proprio insieme di responsabilità:
ServiceCryptoConfigManager: implementa un pattern di tipo facade che permette di gestire operazioni ad alto livello: creare una nuova configurazione di servizio., criptare la configurazione attuale, decriptare la configurazione precedentemente criptata. Lasciando però le operazioni effettive ad un'altra componente che lavora a basso livello.

ServiceCryptoConfigService: Responsabile delle operazioni di basso livello come criptazione, decriptazione, generazione dei dati di configurazione.

**6. Algoritmi**

```mermaid 

classDiagram

    class CryptoManager {
        +deriveMasterKey(password: byte[], algorithmConfig: AlgorithmConfig) byte[]
        +encrypt(data: byte[], key: SecretKeySpec, algorithmConfig: AlgorithmConfig) byte[]
        +decrypt(data: byte[], key: SecretKeySpec, algorithmConfig: AlgorithmConfig) byte[]
    }

    class KeyDerivationAlgorithm {
        <<Interface>>
        +deriveKey(source: byte[], config: AlgorithmConfig) byte[]
    }

    class KeyDerivationFactory {
        +createAlgorithm(name: String) KeyDerivationAlgorithm$
    }

    class Argon2
    class bcrypt
    class hkdf
    class scrypt

    class EncryptionAlgorithm {
        <<Interface>>
        +encrypt(source: byte[], key: SecretKeySpec, algorithmConfig: AlgorithmConfig) byte[]
        +decrypt(source: byte[], key: SecretKeySpec, algorithmConfig: AlgorithmConfig) byte[]
    }

    class EncryptionAlgorithmFactory {
        +createAlgorithm(name: String) EncryptionAlgorithm$
    }

    class AES

    class MustBeDestroyed {
        <<Interface>>
    }

    class AlgorithmConfigBuilder {
        <<Interface>>
        +setAlgorithmName(algorithmName: String) AlgorithmConfigBuilder
        +setAlgorithmType(algorithmType: String) AlgorithmConfigBuilder
        +setSalt(salt: byte[]) AlgorithmConfigBuilder
        +setParameters(parameters: Map<String,String>) AlgorithmConfigBuilder
        +addParameter(key: String, value: String) AlgorithmConfigBuilder
        +build() AlgorithmConfig
    }

    class DefaultAlgorithmConfigBuilder {
        -algorithmName: String
        -algorithmType: String
        -salt: byte[]
        -parameters: Map<String,String>
    }

    class AlgorithmConfigFactory {
        +createAlgorithmConfig(algorithmName: String, salt: byte[], params: Map<String,String>) AlgorithmConfig$
        -mergeParams(userParams: Map<String, String>, defaultParams: Map<String, String>) Map<String, String>$
        -buildConfig(name: String, type: String, salt byte[], params: Map<String, String>) AlgorithmConfig$
    }

    class AlgorithmConfig {
        -algorithmName: String
        -algorithmType: String
        -salt: byte[]
        -parameters: Map<String,String>
        +addNewParameter(key: String, value: String) void
        +removeParameterByName(key String) void
        +updateParameter(key: String, value: String) void
        +getParameterValueByName(key: String) String
        +destroy() void
    }

    CryptoManager --> KeyDerivationFactory : uses
    CryptoManager --> EncryptionAlgorithmFactory : uses
    CryptoManager --> AlgorithmConfig : uses
    AlgorithmConfigFactory --> AlgorithmConfigBuilder : uses

    KeyDerivationFactory --> KeyDerivationAlgorithm : create
    EncryptionAlgorithmFactory --> EncryptionAlgorithm : create

    KeyDerivationAlgorithm <|.. Argon2 : implements
    KeyDerivationAlgorithm <|.. bcrypt : implements
    KeyDerivationAlgorithm <|.. hkdf : implements
    KeyDerivationAlgorithm <|.. scrypt : implements
    EncryptionAlgorithm <|.. AES : implements
    AlgorithmConfigBuilder <|.. DefaultAlgorithmConfigBuilder : implements
    AlgorithmConfig ..|> MustBeDestroyed : implements

    DefaultAlgorithmConfigBuilder --> AlgorithmConfig : build

```

**Problema**
Questo è il cuore del sistema. L’applicazione richiede un sistema robusto, modulare e flessibile per la gestione della crittografia e dell’hashing dei dati sensibili. L'applicazione deve supportare molteplici algoritmi di derivazione di chiavi e cifratura/decifratura, frnire configurazioni personalizzabili tramite parametri specifici per ogni algoritmo e creare una modularitàp er usi futuri.

**Soluzione**
Le soluzioni adotatte soo:
Factory Method Pattern: viene utilizzato per creare istanze specifiche degli algoritmi crittografici e di derivazione chiavi, senza esporre le logiche concrete di creazione.
Strategy Pattern: permette di definire famiglie intercambiabili tramite la definizione delle interfacce di algoritmi crittografici, facilitando la selezione dinamica dell'algoritmo appropriato in fase di esecuzione permettendo di creare grandissima flessibilità nella scelta degli algoritmi.
Builder Pattern: vien utilizzato per creare configurazioni di algoritmi crittografici personalizzat facilitando la gestione delle configurazioni.

**7. File manager**
```mermaid

classDiagram
    class FileManager {
        <<interface>>
        +loadData(fileName: String) byte[]
        +saveData(fileName: String, data: byte[])
        +deleteData(fileName: String)
        +createPath(fileName: String) Path
    }

    class AbstractFileManager {
        +createPath(fileName: String)* 
        #InputStream openInputStream(path: Path)* 
    }

    class DefaultFileManager {
    }

    class GenericFileManager {
    }

    class ResourcesFileManager {
        +void saveData(fileName: String, data: byte[])
        +void deleteData(fileName: String)
    }

    FileManager <|.. AbstractFileManager : implements
    
    AbstractFileManager <|-- DefaultFileManager : extends
    AbstractFileManager <|-- GenericFileManager : extends
    AbstractFileManager <|-- ResourcesFileManager : extends 

```

**Problema**
Nel progetto esiste la necessità di gestire operazioni  file, quali caricamento e salvataggio. Tuttavia, queste operazioni possono variare a seconda del contesto applicativo (file di risorse, file generici, file nel sistema operativo). 

**Soluzione**
Per risolvere questi problemi è stato utilizzato il pattern Template Method combinato con l'astrazione tramite l'interfaccia.
FileManager (Interfaccia) definisce il contratto comune per operazioni sui file (leggere, salvare, cancellare, creare percorso).
AbstractFileManager (Classe astratta) implementa parzialmente il contratto, definendo un comportamento comune per alcune operazioni (come caricamento e salvataggio dati) e lasciando astratte altre operazioni specifiche (come la creazione del percorso e il recupero di input sorgente dei dati).
DefaultFileManager, GenericFileManager, ResourcesFileManager (Implementazioni concrete) estendono AbstractFileManager e implementano la logica specifica per ciascun contesto:
    DefaultFileManager: gestisce file del file system in una directory utente.
    GenericFileManager: implementazione generica senza vincoli specifici su percorso o estensioni.
    ResourcesFileManager: gestisce file accessibili tramite classpath (cartella resources). Non supporta l'operazione di salvataggio ed eliminazione in quanto sono risorse di sole lettura.

**8. Classi di utilità**

```mermaid

classDiagram
    class CryptoUtils {
      +generateSalt(int length) byte[]
      +cleanMemory(char[] source)
      +cleanMemory(byte[] source)
      +destroy(Supplier~T~ getterFunction, Consumer~T~ setterFunction)
      -CryptoUtils()
    }

    class DataUtils {
      +concatArray(char[] firstData, char[] secondData) char[]
      +concatArray(byte[] firstData, byte[] secondData) byte[]
      -DataUtils()
    }

    class EncodingUtils {
      +charToByteConverter(char[] source) byte[]
      +charToByteConverter(char[] source, String charsetName) byte[]
      +byteToCharConverter(byte[] source) char[]
      +byteToCharConverter(byte[] source, String charsetName) char[]
      +byteToBase64(byte[] source) char[]
      +base64ToByte(char[] source) byte[]
      +byteToBase32Byte(byte[] source) byte[]
      +deserializeData(byte[] data, TypeReference~T~ classTypeReference) ~T~ T
      +serializeData(T data) byte[]
    }

```

**Problema**
Alcune funzionalità sono considerate di base e vengono utilizzate di frequente. Non hanno bisogno di avere dati associati direttamente ma vengono frequentemente richiamati in piu parti del codice.

**Soluzione**
La gestione di queste funzionalità viene gestita utilizzando delle classi di utilità ovvero classi che raccolgono dei metodi statici che si occupano di specifiche aree, senza la necessità ogni volta di istanziare l'oggetto:
CryptoUtils si occupa di operazioni si sicurezza crittografiche e di sicurezza della memoria.
DataUtils gestisce la manipolazione e concatenazione dei dati.
EncodingUtils centralizza le operazioni di conversione e codifica.

**9. Navigation e Controller**

```mermaid

classDiagram    

    class GenericNavigator~S, T~ {
        <<interface>>
        +navigateTo(pathToFile: String, sceneTitle: String) GenericController~S, T~ 
        +~U~ navigateTo(pathToFile: String, sceneTitle: String, optionalData: U) GenericController~S, T~ 
        +getView() S
    }

    class GenericNavigatorAbstract~S, T~  {
        <<abstract>>
        + getData() T
        # setView(Parent, String)*
    }

    class SceneNavigator {
    }

    class StepNavigator {
    }

    class GenericController~S, T~ {
        <<interface>>
        + setNavigator(navigator: GenericNavigator~S, T~)
        + setData(data: T)
        + initializeData()
        + initializeData(optionalData: U)
    }

    class ControllerAbstract~S, T~ {
        <<abstract>>
        + getNavigator() GenericNavigator~S, T~
        + getData() T
    }

    GenericNavigator <|.. GenericNavigatorAbstract : implements
    GenericController <|.. ControllerAbstract : implements

    GenericNavigatorAbstract <|-- SceneNavigator : extends
    GenericNavigatorAbstract <|-- StepNavigator : extends

    GenericNavigator ..> GenericController : return

    ControllerAbstract <|-- MainRegistrationController : extends
    ControllerAbstract <|-- LoginController : extends

```

**Problema**
Nell'applicaozione la gestione della navigazione tra scene oppure all'interno della stessa scenadell'interfaccia utente può diventare complessa e difficile da mantenere. La presenza di un sistema di navigazione ben riutilizzabile permette di garantire una transizione fluida tra le diverse viste e il corretto passaggio dei dati tra controller.

Inoltre, molte volte, le view hanno bisogno di metodi e campi comuni quindi anche qui è possibile gestirlo tramite dei moduli riutilizzabili.

**Soluzione**
Il progetto implementa un sistema di navigazione generico basato su un'interfaccia GenericNavigator<S, T>, che definisce le operazioni di navigazione per passare tra scene o tra componenti UI. Il sistema utilizza un'architettura basata su classi astratte NavigatorAbstract<S, T> e implementazioni concrete per gestire la navigazione in diversi contesti, come finestre (Stage) o pannelli (Pane). Questa implementazione segue il pattern Template method pattern in cui le classi che estendono la classe astratta devono implementare dei metodi dichiarati nella classe genitore.
Similmente, la gestione dei controller si basa sull'interfaccia GenericController<S, T> e sulla sua implementazione astratta ControllerAbstract<S, T>, sempre basato sul Template Method PATTERN che permette di settare eventuali dati per il controller (in particolare la classe AccountManager) e di settargli il navigator che permette di gestire lo stage in cui si trova e di cambiare la scene.

**10. Registration**

```mermaid

classDiagram
    class GenericController~S, T~ {
        <<interface>>
        + setNavigator(navigator: GenericNavigator~S, T~)
        + setData(data: T)
        + initializeData()
        + initializeData(optionalData: U)
    }

    class ControllerAbstract~S, T~ {
        <<abstract>>
        + getNavigator() GenericNavigator~S, T~
        + getData() T
    }
    
    class MainRegistrationController {
      +handleBack()
      +handleNext()
      +updateStepView()
      +navigateToMain()
    }
    
    class RegistrationConfigCreationController {
      <<abstract>>
      +createAlgorithmConfig()
      +loadAlgorithmList()
      +populateComboBox()
    }
    
    class RegistrationStep1Controller {
      -collectData()
      -validateInput()
    }
    
    class RegistrationStep2Controller {
    }
    
    class RegistrationStep3Controller {
    }
    
    class RegistrationStep4Controller {
      +add2FA()
      +dontAdd2FA()
    }
    
    class RegistrationStep5Controller {
      -load2FAImage()
      -update2FACode()
      -setTimer()
    }

    class StepHandler {
      <<interface>>
      +handleStep() boolean
    }

    class GenericNavigator {
      +navigateTo(path: String, title: String)
    }
    
    ControllerAbstract ..|> GenericController : implements

    ControllerAbstract <|-- MainRegistrationController : extends
    ControllerAbstract <|-- RegistrationConfigCreationController : extends
    ControllerAbstract <|-- RegistrationStep1Controller : extends
    ControllerAbstract <|-- RegistrationStep4Controller : extends
    ControllerAbstract <|-- RegistrationStep5Controller : extends
    RegistrationConfigCreationController <|-- RegistrationStep2Controller : extends
    RegistrationConfigCreationController <|-- RegistrationStep3Controller : extends
    
    RegistrationStep1Controller ..|> StepHandler : implements
    RegistrationStep4Controller ..|> StepHandler : implements
    RegistrationStep5Controller ..|> StepHandler : implements
    RegistrationConfigCreationController ..|> StepHandler  : implements

    MainRegistrationController --* GenericNavigator : create
    
    GenericNavigator --> StepHandler : uses

```

**Problema**
La registrazione è divisa in step, ognuno dei quali richiede di essere completato. Da ogni step, è possibile andare avanti e indietro tra gli step di registrazione.

**Soluzione**
La soluzione adottata è quella di organizzarla in una serie di step sequenziali, con la possibilità di navigare avanti e indietro, garantendo un flusso guidato all’utente, questo è attuato tramite un pattern Wizard o multi step.
Ogni classe estende una classe di base che offre delle funzionalità di navigazione e di settaggio di dati (Template method pattern) con la possibilità di modificare i metodi per renderli personalizzabili.
La presenza dell'interfaccia StepHandler implementata da ogni step (Strategy pattern), permette di gestire ogni step a partire del main controller senza conoscere la classe effettiva dello step che si sta eseguendo in quel momento.

**11. TOTP authentication**

```mermaid

classDiagram
    class TOTPAuthentication {
        <<interface>>
        generateCode() char[]
        remainingTime() double
        validateCode(code: char[]) boolean
        generateOtpAuthURL(account: String) char[]
        generateQRCodeForJavaFX(account: String, width: int, height: int) WritableImage
    }

    class DefaultTotpAuthentication {
        - verifySeedLength(seed: byte[]) byte[]
        - createTOTPGenerator() TOTPGenerator
        - generateQrMatrix(account: String, width: int, height: int) BitMatrix
    }

    class MustBeDestroyed {
        <<interface>>
        destroy() void
    }

    TOTPAuthentication <|.. DefaultTotpAuthentication : implements
    MustBeDestroyed <|.. DefaultTotpAuthentication : implements

```

**Problema**
L'obiettivo è implementare un sistema di generazione e validazione di codici temporanei (TOTP ovvero Time-Based One-Time Password) per garantire un ulteriore livello di sicurezza rispetto all'utilizzo delle sole tradizionali password.

**Soluzione**
La soluzione proposta implementa il pattern Strategy definita stata definita un'interfaccia comune (TOTPAuthentication) per garantire modularità e intercambiabilità delle implementazioni. Una iimplementazione concreta (DefaultTotpAuthentication) realizza le funzionalità richieste utilizzando una libreria esterna per l'effettiva generazione dei codici OTP. In questo modo è posisible supportare diverse librerie o diverse implementazione per un generatore di codici TOTP.


# Sviluppo

## Testing automatizzato  
Per garantire la correttezza delle funzionalità principali dell'applicazione sono stati implementati test automatici utilizzando **JUnit** e in alcuni casi **Mockito**.  
Il testing si è concentrato sui componenti core della logica applicativa, in particolare:  
- _Classe [Service]:_ verifica del corretto funzionamento dei metodi per la creazione dei vari servizi.  
- _Classe [ServiceManager]:_ test per controllare certi metodi dedicati alla gestione dei servizi.  
- _Gestione degli errori:_ test per garantire che eccezioni e condizioni di errore vengano gestite correttamente.  

L’_interfaccia grafica_ non è stata testata per la complessità aggiuntiva e mancanza di tempo.  
Per l'implementazione dei test si è utilizzato **JUnit 5**, sfruttando le annotazioni @Test per definire i casi di test e @BeforeEach per la preparazione del contesto di esecuzione. Inoltre, **Mockito 5.12.0** usato per creare mock e simulare il comportamento di dipendenze esterne, evitando effetti collaterali nei test.

## Note di sviluppo

#### Parte di Nataliia Skybun.
#### 1. Utilizzo di libreria Passay
**Dove:** `com.zysn.passwordmanager.model.utils.security.impl.PasswordGenerator`  
**Snippet:**
```java
public char[] generatePassword(int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {

        if (length < 12) {
            throw new IllegalArgumentException("Password length must be greater than 11.");
        }

        List<CharacterRule> rules = new ArrayList<>();
        if (useUpperCase) rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        if (useLowerCase) rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        if (useNumbers) rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        if (useSpecialChar) rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

        if (rules.size() < 2) {
            throw new IllegalArgumentException("You must select at least two character categories.");
        }
       
        org.passay.PasswordGenerator passayGenerator = new org.passay.PasswordGenerator();
        String password = passayGenerator.generatePassword(length, rules);
       
        return password.toCharArray();
    }
```

#### 2. Utilizzo di lambda expressions
**Dove:** `com.zysn.passwordmanager.model.service.ServiceManager`  
**Snippet:**
```java
public boolean removeService(String serviceName) {
        return services.removeIf(service -> service.getName().equals(serviceName));
    }
```

#### 3. Utilizzo di Clipboard di Java (AWT)
**Dove:** `com.zysn.passwordmanager.controller.backup.BackupController.java`  
**Snippet:**
```java
private void copyToClipboard(String text) {
         StringSelection selection = new StringSelection(text);
         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
     }
…

Button copyButton = new Button("Copy");
         copyButton.setOnAction(e -> {
             copyToClipboard("Password: " + passwordText + "\nSalt: " + saltText);
             alert.setHeaderText("Copied to clipboard!");
         });
```

#### Parte di Yuhang Zhu.  
#### 1. Utilizzo della libreria Bouncy Castle
**Dove:** `com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Scrypt`  
**Snippet:**
```java
@Override
    public byte[] deriveKey(byte[] source, AlgorithmConfig algorithmConfig) {
        // Configurations of the algorithm
        int costFactor = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.COST_FACTOR.getParameter()));
        int blockSize = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.BLOCK_SIZE.getParameter()));
        int parallelism = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.PARALLELISM.getParameter()));
        int keySize = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.KEY_SIZE.getParameter())) / 8;

        byte[] salt = algorithmConfig.getSalt();

        byte[] keyBytes = SCrypt.generate(source, salt, costFactor, blockSize, parallelism, keySize);

        return keyBytes;
    }
```

#### 2. Utilizzo della libreria Jackson
**Dove:** `com.zysn.passwordmanager.model.utils.encoding.EncodingUtils`  
**Snippet:**
```java
public static <T> byte[] serializeData(final T data) {
        final ObjectMapper objectMapper = new ObjectMapper();

        byte[] serializedData = null;

        try {
            serializedData = objectMapper.writeValueAsBytes(data);
        } catch (final JsonProcessingException e) {
            System.err.println(
                    "Error during data serialization: ensure the data structure is compatible with JSON format.");
        }

        return serializedData;
    }
```

#### 3. Utilizzo della libreria OTP-Java
**Dove:** `com.zysn.passwordmanager.model.security.totp.impl.DefaultTotpAuthentication`  
**Snippet:**
```java
@Override
    public char[] generateCode() {
        return this.totpGenerator.now().toCharArray();
    }
```

#### 4. Utilizzo della libreria ZXing
**Dove:** `com.zysn.passwordmanager.model.security.totp.impl.DefaultTotpAuthentication`  
**Snippet:**
```java
@Override
public WritableImage generateQRCodeForJavaFX(final String account, final int width, final int height) {
    final BitMatrix bitMatrix = this.generateQrMatrix(account, width, height);

    final WritableImage image = new WritableImage(width, height);

    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            // Trasforma i bit 0 e 1 in neri e bianchi
            image.getPixelWriter().setColor(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
        }
    }

    return image;
}
```

#### 5. Utilizzo del framework Spring Boot
**Dove:** Nella creazione del fatty jar usando il comando `.\gradlew  bootJar`. Questo perchè la JVM richiede che i provider di sicurezza siano firmati digitalmente. Quando si creare un fat jar è possibile che le firme, che servono per garantire autenticità e integrità dei provider di sicurezza siano validi, vengano cancellate o modificate. Con il framework Spring Boot i pacchetti e le dipendenze non compromette queste firme. 

In assenza di bootJar bisognere usare un'altra libreria, includere BC come dipendeza esterna e configurare la classPath per poterla utilizzare oppure modificare le impostazioni di Java e della JVM per bypassare questo check di sicurezza. Le opzioni 2 e 3 possono risultare complicate per utente normale oppure ridurre il livello di sicurezza del sistema.

# Commenti finali
## Autovalutazione e lavori futuri

#### Parte di Nataliia Skybun.
La mia parte del progetto si è concentrata sulla gestione dei servizi, la generazione di password sicure e l'implementazione del sistema di backup e ripristino.
L'utilizzo del pattern Builder per la creazione dei servizi ha garantito una maggiore robustezza e sicurezza nella gestione delle password. L'implementazione del ServiceManager come singleton ha permesso una gestione centralizzata dei servizi. Utilizzo della libreria Passay per il generatore di password sicure ha assicurato un approccio migliore per la creazione delle password. Infine, il sistema di backup e ripristino garantisce la protezione dei dati sensibili in caso di necessità.

Ci sono alcune aree che potrebbero essere migliorate in futuro.  
_Prestazioni del ServiceManager:_ Essendo un singleton, potrebbe diventare un collo di bottiglia in caso di un numero elevato di servizi. Si potrebbe valutare l'implementazione di un sistema di caching o di una gestione asincrona dei servizi. _Sistema di backup incrementale:_ Il sistema di backup attuale crea un backup completo dei dati ogni volta. Si potrebbe valutare l'implementazione di un sistema di backup incrementale per ridurre la dimensione e il tempo di creazione. _Test approfonditi:_ Implementare test unitari più completi per garantire la robustezza e l'affidabilità del codice. Anche per la parte GUI.

Ritengo di aver svolto per bene la mia parte, ma riconosco che un altro membro del gruppo ha avuto un carico di lavoro maggiore e ha fatto un ottimo lavoro. Questo mi ha dato modo di imparare e migliorare grazie al suo contributo. 


#### Parte di Yuhang Zhu. 


# Guida Utente

Questa guida ti aiuterà a navigare e utilizzare al meglio la nostra applicazione di gestione delle password.

**1. Accesso all'Applicazione**

Quando avvii l'applicazione, ti troverai di fronte alla schermata di Login, dove potrai:

- Accedere inserendo il tuo Username e Password;

- Registrarti se non hai ancora un account, cliccando su Register.

**1.1 Creazione di un Nuovo Account**

Cliccando su Register, dovrai completare 5 passaggi per creare il tuo account:

- Scegli Username e Password per il tuo account;

- Seleziona un algoritmo di hashing per la protezione della password principale e imposta i parametri richiesti;

- Scegli un algoritmo di crittografia per la gestione delle credenziali salvate e configurane i parametri;

- Decidi se abilitare l'Autenticazione a Due Fattori (2FA) per maggiore sicurezza.

Completata la registrazione, verrai reindirizzato alla pagina principale dell'applicazione.

**2. Pagina Principale (Main Page)**

La schermata principale mostra:

- Una lista dei servizi salvati (inizialmente vuota);

- Una barra di ricerca per trovare rapidamente un servizio;

- Tre pulsanti:

	- _Add Service:_ per aggiungere un nuovo servizio.
	- _Backup:_ per creare o ripristinare un backup.
	- _Logout:_ per disconnettersi e tornare alla schermata di Login.

**3. Aggiunta di un Nuovo Servizio**

Cliccando su Add Service, potrai inserire i dettagli di un nuovo servizio:

- Nome del servizio (es. "Gmail");
- Username o email utilizzati per l'accesso;
- Password del servizio;
- Note aggiuntive (opzionale).

Generatore di password: Puoi generare una password sicura scegliendo la lunghezza e i caratteri da includere. Premendo Generate, la password verrà automaticamente inserita nel campo.

Per salvare, premi Save. Se vuoi annullare, premi Cancel.

**4. Modifica o Eliminazione di un Servizio**

Cliccando su un servizio nella lista, accedi alla sua pagina dedicata, dove potrai:

- Modificare i dettagli e aggiornare i campi;

- Eliminare il servizio definitivamente.

Per motivi di sicurezza, la password è nascosta, ma può essere resa visibile cliccando sull'icona con tre puntini.

**5. Backup e Ripristino**

Cliccando su Backup, puoi:

- _Creare un backup:_ Premi Create, e verranno generate una password e un salt. Salvali in un posto sicuro per un futuro ripristino.

- _Ripristinare un backup:_ Inserisci la password, il salt e seleziona il file di backup da caricare.

Tornare alla schermata principale premendo Back.

**6. Disconnessione**

Premendo il pulsante Logout, tornerai alla pagina iniziale di Login, chiudendo la sessione attiva.