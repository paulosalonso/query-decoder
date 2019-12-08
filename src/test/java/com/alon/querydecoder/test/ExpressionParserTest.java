package com.alon.querydecoder.test;

import com.alon.querydecoder.MatchType;
import com.alon.querydecoder.ExpressionParser;
import com.alon.querydecoder.SingleExpression;
import com.alon.querydecoder.SingleExpressionParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionParserTest {

    @Test
    public void testSingleExpression() {
        String expression = "nome[CT]:Paulo";
        
        assertEquals(expression, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testNegatedSingleExpression() {
        String expression = "nome[NCT]:Paulo";
        
        assertEquals(expression, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testSingleExpressionWithSuppressedMatch() {
        String expression = "nome:Paulo";
        String expected = "nome[EQ]:Paulo";
        
        assertEquals(expected, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testChainOfSingleExpressionsWithAndLogicalOperator() {
        String expression = "nome[CT]:Paulo AND sobrenome[CT]:Alonso";
        
        assertEquals(expression, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testChainOfSingleExpressionsWithOrLogicalOperator() {
        String expression = "nome[CT]:Paulo OR sobrenome[CT]:Alonso";
        
        assertEquals(expression, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testChainOfExpressionsWithGroupAtTheEndAndBothLogicalOperators() {
        String expression = "nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40)";
        
        assertEquals(expression, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testChainOfExpressionsWithGroupAtTheMiddleAndBothLogicalOperators() {
        String expression = "nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador";
        
        assertEquals(expression, ExpressionParser.parse(expression).asString());
    }
    
    @Test
    public void testChainOfExpressionsWithNestedGroupAndBothLogicalOperatorsAndBothExplicitAndSuppressedMatches() {
        String expression = "nome[CT]:Paulo AND ((sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao:Programador)";
        String expected = "nome[CT]:Paulo AND ((sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador)";
        
        assertEquals(expected, ExpressionParser.parse(expression).asString());
    }

    @Test
    public void testExpressionWithoutFieldName() {
        String expression = "expression";

        assertEquals(
                String.format("Field not found in <%s>.", expression),
                assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(expression))
                    .getMessage()
        );
    }
    
    @Test
    public void testExpressionWithInvalidMatchType() {
        String expression = "nome[AA]=Paulo";

        assertTrue(
                assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(expression))
                        .getMessage()
                        .startsWith("Unknown match type: AA.")
        );
    }

    @Test
    public void testExpressionWithoutValue() {
        String expression = "nome[EQ]";

        assertEquals(
                String.format("Value not found in <%s>.", expression),
                assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(expression))
                        .getMessage()
        );
    }

    @Test
    public void testExpressionWithMoreCloseThanOpenParentheses() {
        String expression = "(nome:Paulo AND sobrenome:Alonso)) OR idade:20";

        assertEquals(
                String.format("The expression closes more parentheses than it opens: <(nome:Paulo AND sobrenome:Alonso))>."),
                assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(expression))
                        .getMessage()
        );
    }

    @Test
    public void testExpressionWithFewerCloseThanOpenParentheses() {
        String expression = "((nome:Paulo AND sobrenome:Alonso) OR idade:20";

        assertEquals(
                "The expression opens more parentheses than it closes.",
                assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(expression))
                        .getMessage()
        );
    }

    @Test
    public void testExpressionWhichNoStartWithLettersOrParenthesis() {

        String expression = "123:abc";

        assertEquals(
                String.format("Invalid start of expression: <%s>.", expression),
                assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(expression))
                        .getMessage()
        );

    }

    @Test
    public void testMath() {

        SingleExpression expression = SingleExpressionParser.parse("nome:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.EQ, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[EQ]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.EQ, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[EW]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.EW, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[SW]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.SW, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[CT]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.CT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[BT]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.BT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[GT]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.GT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[GTE]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.GTE, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[LT]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.LT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[LTE]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.LTE, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[IN]:Paulo");
        assertFalse(expression.getMatch().isNegated());
        assertEquals(MatchType.IN, expression.getMatch().getType());

    }

    @Test
    public void testNegatedMath() {

        SingleExpression expression = SingleExpressionParser.parse("nome[NEQ]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.EQ, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NEW]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.EW, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NSW]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.SW, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NCT]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.CT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NBT]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.BT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NGT]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.GT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NGTE]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.GTE, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NLT]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.LT, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NLTE]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.LTE, expression.getMatch().getType());

        expression = SingleExpressionParser.parse("nome[NIN]:Paulo");
        assertTrue(expression.getMatch().isNegated());
        assertEquals(MatchType.IN, expression.getMatch().getType());

    }
    
}
