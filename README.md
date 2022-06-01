# SOA-ECB-SpringBoot-Producer-With-Kafka
#### Aplikacja SpringBoot mająca na celu pobieranie danych z cenami walut pobieranych z ECB i publikowanie ich na Kafce.

### Na chwilę obecną:
Aplikacja Spring Boot przy uruchomieniu pobiera dane na 90 dni wstecz z XML'a ECB oraz ma schedule pobierania daily danych od poniedziałku do piątku(poza świętami) o godz. 16:10(dane pojawiają się na ECB około 16), następnie wysyłe je do kafki

### Jak włączyć:
1. Uruchomić Kafka Zookeeper
2. Uruchomić Kafka Server
3. Aby sprawdzić działanie (dodawanie rekordów do bazy) uruchomić [drugą aplikację](https://github.com/KamilPalubicki/SOA-ECB-SpringBoot-Consumer) 
4. Uruchomić aplikację  
5. Uruchomić [trzeci mikroserwis API](https://github.com/Kacper-Pohl/SOA-ECB-SpringBoot-API)
