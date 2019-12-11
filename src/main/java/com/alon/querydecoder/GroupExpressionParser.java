package com.alon.querydecoder;

public class GroupExpressionParser {
    
    private GroupExpression building;

    private GroupExpressionParser(String expression) {

        ExpressionParser.validateStartOfExpression(expression);
        this.checkIfStartsWithParenthesis(expression);

        this.building = new GroupExpression();
        
        int opened = 0;
        int closed = 0;
        StringBuilder currentExpressionBuilder = new StringBuilder();
        
        expression = this.normalize(expression);
        
        for (int i = 0; i < expression.length(); i++) {            
            char currentChar = expression.charAt(i);
            
            currentExpressionBuilder.append(currentChar);

            if (currentChar == '(')
                opened++;
            else if (currentChar == ')')
                closed++;
            
            if (opened == closed) {
                String currentExpression = this.removeParentheses(currentExpressionBuilder.toString());
                
                this.building.setGroupedExpression(ExpressionParser.parse(currentExpression));
                
                break;
            }            
        }
        
        this.checkParenthesesCount(opened, closed);
        
        expression = this.removeParsedGroup(expression, currentExpressionBuilder.toString());
        
        if (!expression.isBlank()) {
            if (this.nextCharIsRightParenthesis(expression))
                throw new IllegalArgumentException(
                        String.format("The expression closes more parentheses than it opens: <%s)>.", currentExpressionBuilder.toString()));

            this.building.setLogicalOperator(this.determineLogicalOperator(expression));
            
            expression = this.removeLogicalOperatorFromStart(expression);
            
            this.building.setNext(ExpressionParser.parse(expression));
        }
        
    }
    
    public static GroupExpression parse(String expression) {
        
        return new GroupExpressionParser(expression).building;
    
    }
    
    private void checkIfStartsWithParenthesis(String expression) {
        
        if (!expression.startsWith("("))
            throw new IllegalArgumentException("A group must start with parentheses");
        
    }

    private boolean nextCharIsRightParenthesis(String expression) {
        return expression.startsWith(")");
    }
    
    private void checkParenthesesCount(int opened, int closed) {
        
        if (opened > closed)
            throw new IllegalArgumentException("The expression opens more parentheses than it closes.");
        
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
        
        if (expression.startsWith("AND "))
            return LogicalOperator.AND;
        else if (expression.startsWith("OR "))
            return LogicalOperator.OR;
        
        throw new IllegalArgumentException(
                String.format("The logical operator was not found in [%s].", expression));
        
    }
    
    private String removeLogicalOperatorFromStart(String expression) {
        
        if (expression.startsWith(LogicalOperator.AND.name()))
            return expression.substring(4).trim();
        else if (expression.startsWith(LogicalOperator.OR.name()))
            return expression.substring(3).trim();
        
        return expression;
        
    }
    
}
