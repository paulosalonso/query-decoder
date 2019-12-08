package com.alon.querydecoder;

public class ExpressionParser {
    
    private ExpressionParser() {}
    
    public static Expression parse(String expression) {
        
        if (expression.startsWith("("))
            return GroupExpressionParser.parse(expression);
        
        return SingleExpressionParser.parse(expression);
        
    }

    protected static void validateStartOfExpression(String expression) {
        if (!expression.matches("^[a-zA-Z(].*"))
            throw new IllegalArgumentException(
                    String.format("Invalid start of expression: <%s>.", expression));
    }
    
}
