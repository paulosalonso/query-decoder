package com.alon.querydecoder;

public class GroupExpression implements Expression {
    
    private Expression groupedExpression;
    private LogicalOperator logicalOperator;
    private Expression next;

    public Expression getGroupedExpression() {
        return groupedExpression;
    }

    public void setGroupedExpression(Expression groupedExpression) {
        this.groupedExpression = groupedExpression;
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
        String result = String.format("(%s)", groupedExpression);
        
        if (next != null)
            result = String.format("%s %s %s", result, this.logicalOperator, this.next);
        
        return result;
    }
    
    @Override
    public String toString() {
        return this.asString();
    }
    
}
