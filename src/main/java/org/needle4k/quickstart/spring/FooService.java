package org.needle4k.quickstart.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FooService {
    private final Formatter formatter;

    @Autowired
    public FooService(@FormatterType("Foo") Formatter formatter) {
        this.formatter = formatter;
    }

    public String doStuff() {
        return formatter.format();
    }
}