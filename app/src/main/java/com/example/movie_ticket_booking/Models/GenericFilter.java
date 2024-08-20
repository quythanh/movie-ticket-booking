package com.example.movie_ticket_booking.Models;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GenericFilter<T> {
    private Map<FilterType, Map<String, Object>> filters = new HashMap<>();

    public GenericFilter(Class<T> type) {
        for (FilterType t : FilterType.values()) {
            filters.put(t, new HashMap<>());
            for (Field f : type.getDeclaredFields())
                this.set(t, f.getName(), null);

            Class<?> superCls = type.getSuperclass();
            if (superCls == null) return;
            for (Field f : superCls.getDeclaredFields())
                this.set(t, f.getName(), null);
        }
    }

    public Map<FilterType, Map<String, Object>> get() {
        return this.filters;
    }

    public void set(FilterType t, String s, Object o) {
        Map<String, Object> m = this.filters.get(t);
        m.put(s, o);
        this.filters.put(t, m);
    }
}
