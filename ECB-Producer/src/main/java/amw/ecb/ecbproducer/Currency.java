package amw.ecb.ecbproducer;

public class Currency {
    private String date;
    private String from;
    private String to;
    private double rate;
    public Currency(){
    };

    public Currency(String date, String from, String to, double rate){
        this.date = date;
        this.from = from;
        this.to = to;
        this.rate = rate;
    };
    public String getDate() {
        return date;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public double getRate() {
        return rate;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
}