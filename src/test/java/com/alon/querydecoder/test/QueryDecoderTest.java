/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alon.querydecoder.test;

import com.alon.querydecoder.Decoder;
import com.alon.querydecoder.QueryDecoder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author paulo
 */
public class QueryDecoderTest {

    @Test
    public void testResolvedStringLevelOne() {
        String query = "nome[CT]:Paulo";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelTwo() {
        String query = "nome:Paulo";
        String expected = "nome[EQ]:Paulo";
        
        assertEquals(expected, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelThree() {
        String query = "nome[CT]:Paulo AND sobrenome[CT]:Alonso";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelFour() {
        String query = "nome[CT]:Paulo OR sobrenome[CT]:Alonso";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelFive() {
        String query = "nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40)";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelSix() {
        String query = "nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelSeven() {
        String query = "nome[CT]:Paulo AND ((sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao:Programador)";
        String expected = "nome[CT]:Paulo AND ((sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador)";
        
        assertEquals(expected, this.createDecoder(query).decode());
    }
    
    private QueryDecoder<String> createDecoder(String query) {
        return new QueryDecoder<>(query, this::decoder);
    }
    
    private String decoder(Decoder group) {
        return group.toString();
    }
}
