package org.needle4k.quickstart.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import jakarta.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
public @interface CurrentUser {
}
