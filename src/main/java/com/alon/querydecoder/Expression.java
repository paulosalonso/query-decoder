package com.alon.querydecoder;

public interface Expression {

    public Expression getNext();
    
    public LogicalOperator getLogicalOperator();
    
    public String asString();
    
}
