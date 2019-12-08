package com.alon.querydecoder;

public class Match {
    
    private MatchType type;
    private boolean negated;

    public Match() {}

    public Match(MatchType type) {
        this.type = type;
        this.negated = false;
    }

    public Match(MatchType type, boolean negated) {
        this.type = type;
        this.negated = negated;
    }

    public MatchType getType() {
        return type;
    }

    public void setType(MatchType type) {
        this.type = type;
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }
    public String asString() {
        return String.format("%s%s", this.negated ? "N" : "", type.name());
    }
    
    @Override
    public String toString() {
        return this.asString();
    }
    
}
