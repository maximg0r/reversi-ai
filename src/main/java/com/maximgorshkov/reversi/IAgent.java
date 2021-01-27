package com.maximgorshkov.reversi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class IAgent
{

    public abstract Action makeAMove(State currentState);
    public abstract long getDuration(); // gets the cumulative duration of an agent

    @Id @GeneratedValue
    private Long id;
}
