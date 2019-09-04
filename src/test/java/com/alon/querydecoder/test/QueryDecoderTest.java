/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alon.querydecoder.test;

import com.alon.querydecoder.Decoder;
import com.alon.querydecoder.QueryDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author paulo
 */
public class QueryDecoderTest {
    
    public QueryDecoderTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testResolvedStringLevelOne() {
        String query = "nome[CT]:Paulo";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelTwo() {
        String query = "nome[CT]:Paulo AND sobrenome[CT]:Alonso";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelThree() {
        String query = "nome[CT]:Paulo OR sobrenome[CT]:Alonso";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelFour() {
        String query = "nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40)";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    @Test
    public void testResolvedStringLevelFive() {
        String query = "nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador";
        
        assertEquals(query, this.createDecoder(query).decode());
    }
    
    private QueryDecoder<String> createDecoder(String query) {
        return new QueryDecoder<>(query, this::decoder);
    }
    
    private String decoder(Decoder group) {
        return group.toString();
    }
}
