package hu.webuni.airport.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class A {

    @Id
    @GeneratedValue
    protected long a1;

    protected String a2;
}
