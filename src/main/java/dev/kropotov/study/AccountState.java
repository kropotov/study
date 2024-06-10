package dev.kropotov.study;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class AccountState implements State {
    @Getter
    private final String owner;
    private final Map<Currency, Integer> funds;

    public AccountState(String owner, Map<Currency, Integer> funds) {
        this.owner = owner;
        this.funds = new HashMap<>(funds);
    }

    public Map<Currency, Integer> getFunds() {
        return new HashMap<>(funds);
    }
}
