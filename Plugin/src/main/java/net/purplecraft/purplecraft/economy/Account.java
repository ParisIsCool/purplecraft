package net.purplecraft.purplecraft.economy;

import java.util.UUID;

public class Account {
    private final UUID uuid;
    private final String name;
    private double balance;

    public Account(UUID uuid, String name, double balance) {
        this.uuid = uuid;
        this.name = name;
        this.balance = balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
