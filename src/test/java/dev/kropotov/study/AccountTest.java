package dev.kropotov.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {
    private final String OWNER = "Pete";
    private final Currency CURRENCY = Currency.RUB;
    private final int VALUE = 1;

    @Test
    @DisplayName("Проверка инкапсуляции владельца")
    public void emptyOwnerTest() {
        assertThrows(IllegalArgumentException.class, () -> new Account(null));
        Account account = new Account(OWNER);
        assertThrows(IllegalArgumentException.class, () -> account.setOwner(""));
    }

    @Test
    @DisplayName("Проверка сеттера владельца")
    public void notEmptyOwnerTest() {
        Account account = new Account(OWNER);
        assertEquals(account.getOwner(), OWNER);
    }

    @Test
    @DisplayName("Проверка инкапсуляции средств отрицательным значением")
    public void negativeFundsValue() {
        Account account = new Account(OWNER);
        assertThrows(IllegalArgumentException.class, () -> account.putFunds(CURRENCY, -1));
    }

    @Test
    @DisplayName("Проверка инкапсуляции средств пустой валютой")
    public void positiveFundsValueNullCurrency() {
        Account account = new Account(OWNER);
        account.putFunds(CURRENCY, VALUE);
        assertEquals(account.getFunds().get(CURRENCY), VALUE);
    }

    @Test
    @DisplayName("Проверка изменения средств")
    public void positiveFundsValue() {
        Account account = new Account(OWNER);
        assertThrows(IllegalArgumentException.class, () -> account.putFunds(null, VALUE));
    }

    @Test
    @DisplayName("Проверка отмены изменения владельца")
    public void undoSetOwner() {
        Account account = new Account(OWNER);

        account.setOwner(OWNER + " new");
        account.undo();
        assertEquals(account.getOwner(), OWNER);

        assertThrows(IllegalStateException.class, account::undo);
    }

    @Test
    @DisplayName("Проверка отмены изменения средств")
    public void undoPutFunds() {
        Account account = new Account(OWNER);
        account.putFunds(CURRENCY, VALUE);

        account.putFunds(CURRENCY, VALUE * 2);
        account.undo();
        assertEquals(account.getFunds().get(CURRENCY), VALUE);

        account.undo();
        assertEquals(account.getFunds().size(), 0);

        assertThrows(IllegalStateException.class, account::undo);
    }

    @Test
    @DisplayName("Проверка сохранения и восстановления состояния")
    public void saveAndRestore() {
        History history = new History();

        Account account = new Account(OWNER);
        account.putFunds(CURRENCY, VALUE);

        LocalDate date = LocalDate.now();
        history.addState(date, AccountState.class, account.save());

        account.setOwner(OWNER + " new");
        account.putFunds(CURRENCY, VALUE * 2);

        AccountState accountState = (AccountState) history.getState(date, AccountState.class);
        account.restore(accountState);

        assertEquals(account.getFunds().get(CURRENCY), VALUE);
        assertEquals(account.getOwner(), OWNER);
    }
}
