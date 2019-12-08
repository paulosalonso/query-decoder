package com.alon.querydecoder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleExpressionParser {
    
    private static final Pattern FIELD_PATTERN;
    private static final Pattern MATCH_PATTERN;
    private static final Pattern VALUE_PATTERN;
    
    static {
        
        FIELD_PATTERN = Pattern.compile("((\\w.*(?=:))(?<!\\]))|(\\w.*(?=\\[))");
        MATCH_PATTERN = Pattern.compile("(?<=\\[).+(?=\\])");
        VALUE_PATTERN = Pattern.compile("(?<=:).*");
        
    }
    
    private SingleExpression building;
    
    private SingleExpressionParser(String expression) {

        ExpressionParser.validateStartOfExpression(expression);
        
        this.building = new SingleExpression();      
        
        expression = this.normalize(expression);
        
        building.setLogicalOperator(this.determineLogicalOperator(expression));
        
        String currentExpression = this.isolateFirstExpression(expression);
        
        building.setField(this.determineField(currentExpression));
        building.setMatch(this.determineMatch(currentExpression));
        building.setValue(this.determineValue(currentExpression));
        
        if (!expression.equals(currentExpression)) {
            expression = this.removeFirstExpression(expression);
            building.setNext(ExpressionParser.parse(expression));
        }
        
    }
    
    public static SingleExpression parse(String expression) {
        
        SingleExpressionParser builder = new SingleExpressionParser(expression);
        
        return builder.building;
        
    }
    
    private String normalize(String expression) {
        
        return expression.replace(" and ", " AND ")
                         .replace(" or ", " OR ")
                         .trim();
    
    }
    
    private LogicalOperator determineLogicalOperator(String expression) {
        
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
    
    private String isolateFirstExpression(String expression) {
        
        return expression.substring(0, getLogicalOperatorIndex(expression))
                         .trim();
        
    }
    
    private int getLogicalOperatorIndex(String expression) {
        
        if (building.getLogicalOperator() == null)
            return expression.length();
        else
            return expression.indexOf(building.getLogicalOperator().name());
        
    }
    
    private String removeFirstExpression(String expression) {
        
        int index;
        
        if (building.getLogicalOperator() != null) {
            index = expression.indexOf(building.getLogicalOperator().name());
            
            if (building.getLogicalOperator().equals(LogicalOperator.AND))
                index += 4;
            else
                index += 3;
        } else
            index = expression.length();
        
        return expression.substring(index)
                         .trim();
        
    }
    
    private String determineField(String expression) {
        
        Matcher m = FIELD_PATTERN.matcher(expression);

        if (m.find())
            return m.group();
        
        throw new IllegalArgumentException(
                String.format("Field not found in <%s>.", expression));
        
    }
    
    private Match determineMatch(String expression) {
        
        Matcher matcher = MATCH_PATTERN.matcher(expression);
        
        String matchStr;
        
        if (matcher.find())
            matchStr = matcher.group().toUpperCase();
        else
            matchStr = "EQ";
        
        boolean negated = matchStr.startsWith("N");
        
        if (negated)
            matchStr = matchStr.substring(1);
        
        try {
            MatchType matchType = MatchType.valueOf(matchStr);
            return new Match(matchType, negated);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    String.format("Unknown match type: %s. The valid match types are: %s.", matchStr, this.getValidMatchTypes()));
        }        
        
    }
    
    private String getValidMatchTypes() {
        
        List<String> matchTypes = Stream.of(MatchType.values())
                                        .map(item -> item.name())
                                        .collect(Collectors.toList());
        
        return matchTypes.stream()
                         .map(matchType -> String.format("N%s", matchType))
                         .flatMap(value -> matchTypes.stream())
                         .collect(Collectors.joining(", "));
        
    }
    
    private String determineValue(String expression) {
        
        Matcher matcher = VALUE_PATTERN.matcher(expression);

        if (matcher.find())
            return matcher.group();
        
        throw new IllegalArgumentException(
                String.format("Value not found in <%s>.", expression));
        
    }
    
}
