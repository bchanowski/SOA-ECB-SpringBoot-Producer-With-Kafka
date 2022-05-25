package amw.ecb.ecbproducer;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDate;
import java.util.List;


@SpringBootApplication
@EnableScheduling
public class XmlParserApp implements CommandLineRunner {

    private XMLService xmlService;

    private Currency currency;

    String updateToday = null;

    String[] publicHolidays={
            "2022-05-26",
            "2022-06-06",
            "2022-06-16",
            "2022-10-03",
            "2022-11-01",
            "2022-12-24",
            "2022-12-25",
            "2022-12-26",
            "2022-12-31",
            "2023-01-01",
            "2023-04-07",
            "2023-04-10",
            "2023-05-01",
            "2023-05-09",
            "2023-05-18",
    };

    private final KafkaTemplate<String, Currency> kafkaTemplate;

    public XmlParserApp(XMLService xmlService, KafkaTemplate<String, Currency> kafkaTemplate){
        this.xmlService = xmlService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(XmlParserApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<Currency> currencies = xmlService.parseCurrencyHist();

        for (int i = 0; i < currencies.size(); i++) {
            currency = currencies.get(i);
            ListenableFuture<SendResult<String, Currency>> future =
                    kafkaTemplate.send("currenciesTopic", currency);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Currency>>() {

                @Override
                public void onSuccess(SendResult<String, Currency> result) {

                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message due to : " + ex.getMessage());
                }
            });
        }
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 300000))
    @Scheduled(cron = "0 10 16 ? * MON-FRI")
    public void runScheduled() throws Exception {

        for(int i = 0; i<publicHolidays.length; i++){
            if(publicHolidays[i].equals(LocalDate.now())){
                updateToday=publicHolidays[i];
            }
        }

        if(updateToday!=null){
            System.out.println("No update today there is a scheduled holiday!");
        } else {
            List<Currency> currencies = xmlService.parseCurrency();

            for (int i = 0; i < currencies.size(); i++) {
                currency = currencies.get(i);
                ListenableFuture<SendResult<String, Currency>> future =
                        kafkaTemplate.send("currenciesTopic", currency);

                future.addCallback(new ListenableFutureCallback<SendResult<String, Currency>>() {

                    @Override
                    public void onSuccess(SendResult<String, Currency> result) {
                        System.out.println("Message sent with offset=[" + result.getRecordMetadata().offset() + "]");
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        System.out.println("Unable to send message due to : " + ex.getMessage());
                    }
                });
            }
        }
    }
}
