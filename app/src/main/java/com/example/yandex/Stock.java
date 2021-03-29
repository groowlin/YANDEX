package com.example.yandex;

public class Stock {
    private String name;
    private String currency;
    private String exchange;
    private String ipo;
    private String ticker;
    private String weburl;
    private String logo;
    private String finnhubIndustry;
    private String country;
    private String percent;
    private String sum;
    private String pc;

    public Stock() {
    }

    public Stock(String name, String currency, String exchange, String ipo, String ticker, String weburl,
                 String logo, String finnhubIndustry, String country, Double percent, Double sum, Double pc) {
        this.name = name;
        this.currency = currency;
        this.exchange = exchange;
        this.ipo = ipo;
        this.ticker = ticker;
        this.weburl = weburl;
        this.logo = logo;
        this.finnhubIndustry = finnhubIndustry;
        this.country = country;
        this.percent = String.format("%.2f", percent);
        this.sum = String.format("%.2f", sum);
        this.pc = String.format("%.2f", pc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getIpo() {
        return ipo;
    }

    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFinnhubIndustry() {
        return finnhubIndustry;
    }

    public void setFinnhubIndustry(String finnhubIndustry) {
        this.finnhubIndustry = finnhubIndustry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getSum() {
        return sum;
    }

    public void setC(String percent) {
        this.sum = sum;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }


    @Override
    public String toString() {
        return "Stock{" +
                "name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", exchange='" + exchange + '\'' +
                ", ipo='" + ipo + '\'' +
                ", ticker='" + ticker + '\'' +
                ", weburl='" + weburl + '\'' +
                ", logo='" + logo + '\'' +
                ", finnhubIndustry='" + finnhubIndustry + '\'' +
                ", country='" + country + '\'' +
                ", percent=" + percent +
                '}';
    }
}
