# SOA-ECB-SpringBoot-Producer-With-Kafka
#### Aplikacja SpringBoot mająca na celu pobieranie danych z cenami walut pobieranych z ECB i publikowanie ich na Kafce.

### Na chwilę obecną:
Aplikacja Spring Boot pobiera dane z XML'a ECB daily dane na dzisiejszy dzień, następnie wysyłe je do kafki

### Jak włączyć:
1. Uruchomić Kafka Zookeeper
2. Uruchomić Kafka Server
3. Aby sprawdzić działanie (dodawanie rekordów do bazy) uruchomić [drugą aplikację](https://github.com/KamilPalubicki/SOA-ECB-SpringBoot-Consumer) 
4. Uruchomić aplikację

Do zrobienia:  
Codziennie pobieranie danych o ~16 (poza świętami i weekendami)  
Pobieranie danych historycznych
