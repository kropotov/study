package dev.kropotov.study;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс Account, который будет представлять собой абстракцию банковского счета.
 * Он будет хранить имя владельца и количество всех имеющихся валют
 */

public class Account {
    @Getter
    private String owner;

    private Map<Currency, Integer> funds = new HashMap<>();

    private final Deque<AccountOperation> accountOperations = new ArrayDeque<>();

    public Account(String owner) {
        checkOwner(owner);
        this.owner = owner;
    }

    public void setOwner(String owner) {
        checkOwner(owner);
        String oldOwner = this.owner;
        accountOperations.push(() -> this.owner = oldOwner);
        this.owner = owner;
    }

    private void checkOwner(String owner) {
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner must be not empty");
        }
    }

    public Map<Currency, Integer> getFunds() {
        return new HashMap<>(funds);
    }

    /**
     * Метод проверки возможности отмены.

     * @return true в случае возможности отмены, false - невозможности
     */
    public boolean canUndo() {
        return !accountOperations.isEmpty();
    }

    /**
     * Метод undo, который будет отменять одно последнее изменение объекта класса Account
     */
    public void undo() {
        if (!canUndo()) {
            throw new IllegalStateException("Unknown account state");
        }
        accountOperations.pop().execute();
    }

    /**
     * Метод, который принимает Валюту и её количество и заменяет текущее количество данной Валюты на указанное.
     * Если такой валюты ранее не было – она добавляется в список.
     *
     * @param currency Валюта
     * @param value    Значение
     */
    public void putFunds(Currency currency, Integer value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be >= 0");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency must be not null");
        }

        Integer oldValue = funds.get(currency);
        if (oldValue == null) {
            accountOperations.push(() -> this.funds.remove(currency));
        } else {
            accountOperations.push(() -> this.funds.put(currency, oldValue));
        }

        funds.put(currency, value);
    }

    /**
     * Метод сохранения
     *
     * @return возвращает объект, который хранит состояние Account на момент запроса сохранения.
     */
    public AccountState save() {
        return new AccountState(owner, new HashMap<>(funds));
    }

    /**
     * Метод restore для приведения соответствующего ему объекта Account в состояние,
     * соответствующее моменту создания сохранения.
     */
    public void restore(AccountState accountState) {
        this.owner = accountState.getOwner();
        this.funds = accountState.getFunds();
    }

    @Override
    public String toString() {
        return "Account{" + "owner='" + owner + '\'' + ", funds=" + funds + '}';
    }
}
