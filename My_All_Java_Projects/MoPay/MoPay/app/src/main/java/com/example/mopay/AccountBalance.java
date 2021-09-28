package com.example.mopay;

public class AccountBalance {
    private String creditNumber;
    private String balance;
    private String creditPhoneNumber;

    public AccountBalance(){

    }

    public AccountBalance(String creditNumber, String balance, String creditPhoneNumber) {
        this.creditNumber = creditNumber;
        this.balance = balance;
        this.creditPhoneNumber = creditPhoneNumber;
    }



    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreditPhoneNumber() {
        return creditPhoneNumber;
    }

    public void setCreditPhoneNumber(String creditPhoneNumber) {
        this.creditPhoneNumber = creditPhoneNumber;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }
}
