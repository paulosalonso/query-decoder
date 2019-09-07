package com.alon.querydecoder;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 
 * Abstrai expressões de comparação valores de atributos 
 * 
 * @author paulo
 *
 */
public class Expression implements Decoder {
    protected String field;
    protected String value;
    protected MatchType matchType;
    protected LogicalOperator logicalOperator;
    protected Decoder next;

    public Expression(String expression) {
        this.parse(expression);
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public MatchType getMatchType() {
        return matchType;
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
    public final Expression parse(String expression) {
        
        expression = this.normalize(expression);
        
        this.logicalOperator = this.determineNextLogicalOperator(expression);
        
        String currentExpression = this.isolateFirstExpression(expression);
        
        this.field = this.determineField(currentExpression);
        this.matchType = this.determineMatchType(currentExpression);
        this.value = this.determineValue(currentExpression);
        
        if (!expression.equals(currentExpression)) {
            expression = this.removeFirstExpression(expression);
            this.next = this.decodeNext(expression);
        }
        
        return this;
    }
    
    private String normalize(String expression) {
        
        return expression.replace(" and ", " AND ")
                         .replace(" or ", " OR ")
                         .trim();
    
    }
    
    private LogicalOperator determineNextLogicalOperator(String expression) {
        
        int and = expression.indexOf(" AND ");
        int or  = expression.indexOf(" OR ");
        
        and = and < 0 ? expression.length() : and;
        or  = or < 0 ? expression.length() : or;
        
        if (and < or)
            return LogicalOperator.AND;
        else if (or < and)
            return LogicalOperator.OR;
        else
            return null;
        
    }
    
    private int getNextLogicalOperatorIndex(String expression) {
        if (this.logicalOperator == null)
            return expression.length();
        else
            return expression.indexOf(this.logicalOperator.name());
    }
    
    private String isolateFirstExpression(String expression) {
        
        return expression.substring(0, this.getNextLogicalOperatorIndex(expression))
                         .trim();
        
    }
    
    private String removeFirstExpression(String expression) {
        
        int index;
        
        if (this.logicalOperator != null) {
            index = expression.indexOf(this.logicalOperator.name());
            
            if (this.logicalOperator.equals(LogicalOperator.AND))
                index += 4;
            else
                index += 3;
        } else
            index = expression.length();
        
        return expression.substring(index)
                         .trim();
        
    }
    
    private String determineField(String expression) {
        
        Pattern p = Pattern.compile("((\\w.*(?=:))(?<!\\]))|(\\w.*(?=\\[))");
        Matcher m = p.matcher(expression);

        if (m.find())
            return m.group();
        
        return null;
        
    }
    
    private MatchType determineMatchType(String expression) {
        
        String matchTypes = Arrays.asList(MatchType.values())
                                  .stream()
                                  .map(item -> item.name())
                                  .collect(Collectors.joining("|"));

        Pattern p = Pattern.compile(String.format("(?<=\\[)%s(?=\\])", matchTypes));
        Matcher m = p.matcher(expression);

        if (m.find())
            return MatchType.valueOf(m.group());
        else
            return MatchType.EQ;
        
    }
    
    private String determineValue(String expression) {
        
        Pattern p = Pattern.compile("(?<=:).*");
        Matcher m = p.matcher(expression);

        if (m.find())
            return m.group();
        
        return null;
        
    }
    
    private Decoder decodeNext(String expression) {
        if (expression.startsWith("("))
            return new Group(expression);
        else
            return new Expression(expression);
    }
    
    @Override
    public String toString() {
        String result = String.format("%s[%s]:%s", this.field, this.matchType, this.value);
        
        if (next != null)
            result = String.format("%s %s %s", result, this.logicalOperator, this.next);
        
        return result;
    }
}
