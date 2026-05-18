package com.meupet.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//indica que a anotação ficará disponível durante a execução(necessário para o reflection)
@Retention(RetentionPolicy.RUNTIME)
//indicação para classes
@Target(ElementType.TYPE) 
public @interface UnusedVersao {
    String numero() default "1.0";
    String autor() default "Equipe MeuPet";
}
