package dev.kropotov.study;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class History {
    private final Map<LocalDate, Map<Class<? extends State>, State>> stateMap = new HashMap<>();

    public Map<Class<? extends State>, State> getState(LocalDate date) {
        return stateMap.get(date);
    }

    public  State getState(LocalDate date, Class<? extends State> objectClass) {
        return getState(date).get(objectClass);
    }

    public void addState(LocalDate date, Class<? extends State> objectClass, State state) {
        Map<Class<? extends State>, State> classMap = stateMap.get(date);
        if (classMap == null) {
            classMap = new HashMap<>();
        }
        classMap.put(objectClass, state);
        stateMap.put(date, classMap);
    }

}
