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
        parse(expression);
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

//    @Override
//    public final Expression parse(String query) {
//        // Armazena o índice dos operadores lógicos
//        int and = query.indexOf(" AND ");
//        int or  = query.indexOf(" OR ");
//        
//        // Se o índice for menor que zero, o operador não existe na expressão 
//        // e por isso recebe o tamanho da string para ser o valor máximo
//        and = and < 0 ? query.length() : and;
//        or  = or < 0 ? query.length() : or;
//        
//        // Armazena o índice do primeiro operador lógico
//        // Caso não exista operador lógico na expressão, armazena o comprimento da string
//        int i;
//        
//        if (and < or) {
//            i = and;
//            this.logicalOperator = LogicalOperator.AND;
//        } else if (or < and) {
//            i = or;
//            this.logicalOperator = LogicalOperator.OR;
//        } else 
//            i = query.length();
//        
//        // Isola a expressão atual
//        String expression = query.substring(0, i).trim();
//        
//        // Retira a expressão da string
//        if (and < or)
//            query = query.substring(i + 5);
//        else if (or < and)
//            query = query.substring(i + 4);
//        else
//            query = query.substring(i);
//
//        // Obtém o atributo
//        Pattern p = Pattern.compile("((\\w.*(?=:))(?<!\\]))|(\\w.*(?=\\[))");
//        Matcher m = p.matcher(expression);
//
//        if (m.find())
//            this.field = m.group();
//
//        // Obtém o operador
//        String matchTypes = Arrays.asList(MatchType.values()).stream().map(item -> item.name()).collect(Collectors.joining("|"));
//
//        p = Pattern.compile("(?<=\\[)".concat(matchTypes).concat("(?=\\])"));
//        m = p.matcher(expression);
//
//        if (m.find())
//            this.matchType = MatchType.valueOf(m.group());
//        else
//            // Caso não tenha sido informado, ou o informado não seja válido, utiliza EQUALS
//            this.matchType = MatchType.EQ;
//
//        // Obtém o valor
//        p = Pattern.compile("(?<=:).*");
//        m = p.matcher(expression);
//
//        if (m.find())
//            this.value = m.group();
//        
//        // Verifica se existem mais expressões ou grupos a serem criados
//        if (!query.trim().isEmpty()) {
//            if (query.startsWith("("))
//                this.next = new Group(query);
//            else
//                this.next = new Expression(query);
//        }
//        
//        return this;
//    }
    
    @Override
    public Expression parse(String expression) {
        
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
        String result = String.format("%s[%s]:%s", field, matchType, value);
        
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
