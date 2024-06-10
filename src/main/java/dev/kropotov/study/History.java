package dev.kropotov.study;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class History {
    private final Map<LocalDate, AccountState> accountStateMap = new HashMap<>();

    public void addAccountState(LocalDate date, AccountState accountState) {
        accountStateMap.put(date, accountState);
    }

    public AccountState getAccountState(LocalDate date) {
        return accountStateMap.get(date);
    }
}
