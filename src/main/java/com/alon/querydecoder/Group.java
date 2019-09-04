package com.alon.querydecoder;

/**
 * 
 * Abstrai agrupamentos de expressões dentro de parênteses
 * 
 * @author paulo
 *
 */
public class Group implements Decoder {
    protected Decoder decoder;
    protected LogicalOperator logicalOperator;
    protected Decoder next;
    
    public Group(String query) {
        parse(query);
    }

    public Decoder getDecoder() {
        return decoder;
    }
    
    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }

    @Override
    public Decoder getNext() {
        return next;
    }
    
    @Override
    public Group parse(String expression) {
        
        this.checkIfStartsWithParetheses(expression);
        
        int opened = 0;
        int closed = 0;
        StringBuilder strBuilder = new StringBuilder();
        
        expression = this.normalize(expression);
        
        for (int i = 0; i < expression.length(); i++) {            
            char currentChar = expression.charAt(i);
            
            strBuilder.append(currentChar);

            if (currentChar == '(')
                opened++;
            else if (currentChar == ')')
                closed++;
            
            if (opened == closed) {
                String currentExpression = this.removeParentheses(strBuilder.toString());
                
                if (currentExpression.startsWith(("(")))
                    this.decoder = new Group(currentExpression);
                else
                    this.decoder = new Expression(currentExpression);
                
                break;
            }            
        }
        
        this.checkParenthesesCount(opened, closed);
        
        expression = this.removeParsedGroup(expression, strBuilder.toString());
        
        if (!expression.isBlank()) {
            this.logicalOperator = this.determineLogicalOperator(expression);
            
            expression = this.removeLogicalOperatorFromStart(expression);
            
            if (expression.startsWith("("))
                this.next = new Group(expression);
            else
                this.next = new Expression(expression);
        }
        
        return this;
    }
    
    private void checkIfStartsWithParetheses(String expression) {
        
        if (!expression.startsWith("("))
            throw new IllegalArgumentException("A group must start with parentheses");
        
    }
    
    private void checkParenthesesCount(int opened, int closed) {
        
        if (opened > closed)
            throw new IllegalArgumentException(
                            "The expression is incorrect. " + 
                            "There are more open parentheses " + 
                            "than closed parentheses."
            );
        
    }
    
    private String normalize(String expression) {
        
        return expression.replace(" and ", " AND ")
                         .replace(" or ", " OR ")
                         .trim();
    
    }
    
    private String removeParentheses(String expression) {
        
        return expression.substring(0, expression.length() - 1)
                         .substring(1);
        
    }
    
    private String removeParsedGroup(String expression, String parsedGroup) {
        
        return expression.substring(parsedGroup.length())
                         .trim();
        
    }
    
    private LogicalOperator determineLogicalOperator(String expression) {
        
        if (expression.startsWith(LogicalOperator.AND.name()))
            return LogicalOperator.AND;
        
        return LogicalOperator.OR;
        
    }
    
    private String removeLogicalOperatorFromStart(String expression) {
        
        if (expression.startsWith(LogicalOperator.AND.name()))
            return expression.substring(4).trim();
        else if (expression.startsWith(LogicalOperator.OR.name()))
            return expression.substring(3).trim();
        
        return expression;
        
    }
    
    @Override
    public String toString() {
        String result;
        
        if (decoder instanceof Group)
            result = String.format("(%s)", decoder);
        else
            result = decoder.toString();

        if (next != null) {
            String model;

            if (next instanceof Group)
                model = " %s (%s)"; 
            else
                model = " %s %s";

            result = result.concat(String.format(model, logicalOperator, next));
        }
        
        return result;
    }
}
