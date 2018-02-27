package edu.psu.alexaskill.intent_handlers;

import java.lang.annotation.*;

@Repeatable(Handlers.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handler
{
    String value();
}
