package com.example.mopay;

public class Cards {
    private String creditCardString;
    private String cvvString;
    private String dateString;
    private String cardHolderString;

    public Cards(){

    }

    public Cards(String creditCardString,String cvvString, String dateString,String cardHolderString){
        this.creditCardString=creditCardString;
        this.cvvString=cvvString;
        this.dateString=dateString;
        this.cardHolderString=cardHolderString;

    }

    public String getCreditCardString() {
        return creditCardString;
    }

    public void setCreditCardString(String creditCardString) {
        this.creditCardString = creditCardString;
    }

    public String getCvvString() {
        return cvvString;
    }

    public void setCvvString(String cvvString) {
        this.cvvString = cvvString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getCardHolderString() {
        return cardHolderString;
    }

    public void setCardHolderString(String cardHolderString) {
        this.cardHolderString = cardHolderString;
    }
}
