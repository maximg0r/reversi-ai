package com.maximgorshkov.reversi;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;

@Entity
class GameSave {
    private @Id @GeneratedValue Long id;
    @OneToOne(targetEntity = State.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private State currentState;
    @OneToOne(targetEntity = IAgent.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private IAgent computerAgent;
    private int playerColor;

    GameSave() {}

    GameSave(State curreState, IAgent computerAgent, int playerColor) {
        this.currentState = curreState;
        this.computerAgent = computerAgent;
        this.playerColor = playerColor;
    }

    public Long getId() {
        return this.id;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public IAgent getComputerAgent() {
        return computerAgent;
    }

    public void setComputerAgent(IAgent computerAgent) {
        this.computerAgent = computerAgent;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((computerAgent == null) ? 0 : computerAgent.hashCode());
        result = prime * result + ((currentState == null) ? 0 : currentState.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + playerColor;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameSave other = (GameSave) obj;
        if (computerAgent == null) {
            if (other.computerAgent != null)
                return false;
        } else if (!computerAgent.equals(other.computerAgent))
            return false;
        if (currentState == null) {
            if (other.currentState != null)
                return false;
        } else if (!currentState.equals(other.currentState))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (playerColor != other.playerColor)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GameSave [id=" + id + ", currentState=" + currentState + ", computerAgent=" + computerAgent
                + ", playerColor=" + playerColor + "]";
    }


}