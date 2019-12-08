package com.alon.querydecoder;

public class SingleExpression implements Expression {
    
    protected String field;
    protected String value;
    protected Match match;
    protected LogicalOperator logicalOperator;
    protected Expression next;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    @Override
    public Expression getNext() {
        return next;
    }

    public void setNext(Expression next) {
        this.next = next;
    }

    @Override
    public String asString() {
        String result = String.format("%s[%s]:%s", this.field, this.match, this.value);
        
        if (next != null)
            result = String.format("%s %s %s", result, this.logicalOperator, this.next);
        
        return result;
    }
    
    @Override
    public String toString() {
        return this.asString();
    }
    
}
