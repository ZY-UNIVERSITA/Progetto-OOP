
# User data
```mermaid

classDiagram

%% Interface
class MustBeDestroyed {
  <<interface>>
  +destroy() void
}

%% Abstract classes
class UserAccountAbstract {
  <<abstract>>
  - username : String
  + getUsername() String
  + setUsername(username: String) void
}

%% Concrete classes
class UserAccount {
  - masterKey : byte[]
  + getMasterKey() byte[]
  + setMasterKey(masterKey: byte[]) void
}

class UserAuthInfo {
  - passwordDerivedKeyConfig : AlgorithmConfig
  - keyStoreEncryptionConfig : AlgorithmConfig
  - keyStoreConfigEncryptedData : byte[]
  - serviceConfigEncryptedData : byte[]
  - enabled2FA : boolean
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
  - password : byte[]
  - passwordDerivedKey : byte[]
  - totpEncryptionKey : byte[]
  - totpKey : byte[]
  - serviceConfigKey : byte[]
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
    - byte[] password;
    - byte[] confirmPassword;
    - AlgorithmConfig passwordDerivationConfig;
    - AlgorithmConfig keyStoreConfigEncryptionConfig;
    - boolean enabled2FA;

    private byte[] totpKey;
    + byte[] getPassword()
    + void setPassword(byte[] password)
    + byte[] getConfirmPassword()
    + void setConfirmPassword(byte[] confirmPassword)
    + AlgorithmConfig getPasswordDerivationConfig()
    + void setPasswordDerivationConfig(AlgorithmConfig passwordDerivationConfig)
    + AlgorithmConfig getKeyStoreConfigEncryptionConfig()
    + void setKeyStoreConfigEncryptionConfig(AlgorithmConfig keyStoreConfigEncryptionConfig)
    + boolean isEnabled2FA()
    + void setEnabled2FA(boolean enabled2fa)
    + byte[] getTotpKey()
    + void setTotpKey(byte[] totpKey)
}

%% Relationships
MustBeDestroyed <|.. UserAccountAbstract : implements
MustBeDestroyed <|.. UserAuthKey : implements
MustBeDestroyed <|.. CollectedUserData: implements
UserAccountAbstract <|-- UserAccount : extends
UserAccountAbstract <|-- UserAuthInfo : extends

```

# Account manager
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
        -SessionManager sessionManager
        -ServiceManager serviceManager
        -RegistrationService registrationService
        -LoginService loginService

        +setSessionManager(SessionManager sessionManager)
        +setServiceManager(ServiceManager serviceManager)
    }

    class DefaultSessionManager {
        -UserAuthInfo userAuthInfo
        -UserAuthKey userAuthKey
        -KeyStoreConfig keyStoreConfig
        -ServiceCryptoConfig serviceConfig
        -UserAccount userAccount
    }

    class ServiceManager {
        
    }

    class KeyStoreConfig {
        -byte[] keyStoreEncryptionKey
        -byte[] saltWithPasswordDerived
        -byte[] saltWithTotpEncryptionKey
        -byte[] saltForHKDF
        -byte[] serviceDecryptionSalt

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
        -char[] fileName
        -byte[] saltForHKDF
        -byte[] saltForServiceEncryption

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
    AccountManager <|-- DefaultAccountManager : implements
    SessionManager <|-- DefaultSessionManager : implements
    MustBeDestroyed <|-- DefaultSessionManager : implements
    MustBeDestroyed <|-- KeyStoreConfig : implements
    MustBeDestroyed <|-- ServiceCryptoConfig : implements

    DefaultAccountManager --o SessionManager : uses
    DefaultAccountManager --o ServiceManager : uses
    DefaultAccountManager --o RegistrationService : uses
    DefaultAccountManager --o LoginService : uses
    DefaultSessionManager --o KeyStoreConfig : uses
    DefaultSessionManager --o ServiceCryptoConfig : uses

    CollectedUserData <.. AccountManager : uses
    CollectedUserData <.. RegistrationService: uses
    CollectedUserData <.. LoginService : uses 
