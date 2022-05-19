package amw.ecb.ecbproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CurrencyDeserializer implements Deserializer<Currency> {
    @Override public void close() {

    }

    @Override public void configure(Map<String, ?> arg0, boolean arg1) {

    }

    @Override
    public Currency deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        Currency currency = null;
        try {
            currency = mapper.readValue(arg1, Currency.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return currency;
    }
}
