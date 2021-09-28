package com.example.mopay;

public class Wallet {
    private String amountWallet;
    private String id;

    public Wallet(){

    }

    public Wallet(String amountWallet, String id) {
        this.amountWallet = amountWallet;
        this.id = id;
    }

    public String getAmountWallet() {
        return amountWallet;
    }

    public void setAmountWallet(String amountWallet) {
        this.amountWallet = amountWallet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
