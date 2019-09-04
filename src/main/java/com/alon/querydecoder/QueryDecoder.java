package com.alon.querydecoder;

import java.util.function.Function;

/**
 * 
 * Faz o parser de uma expressão no formato de string em uma estrutura de
 * objetos que podem ser manipulados dinamicamente.
 * 
 * Sintaxe: atributo[operador]:valor
 * 
 * Operadores
 * 
 * Os operadores provêm formas de comparação de valores, conforme a especificação abaixo:
 * 
 * Nome do Operador       Operador          Função
 * EQUAL                  EQ ou suprimido   Verificar se os valores são iguais
 * CONTAINS               CT                Verifica se um texto contém determinado texto em seu conteúdo
 * LESSTHAN               LT                Verifica se é menor que determinado valor
 * LESSTHANOREQUALTO      LTE               Verifica se é menor ou igual a determinado valor
 * GREATERTHAN            GT                Verifica se é maior que determinado valor
 * GREATERTHANOREQUALTO   GTE               Verifica se é maior ou igual a determinado valor
 * BETWEEN                BT                Verifica se está entre dois valores
 * IN                     IN                Verifica se está entre uma lista de valores
 * 
 * Exemplos:
 * nome[EQ]:João da Silva | q=nome:João da Silva 
 * nome[CT]:João
 * valor[LT]:100
 * valor[LTE]:100
 * valor[GT]:100
 * valor[GTE]:100
 * valor[BT]:100-200
 * valor[IN]:100,120,150
 * 
 * Operadores lógicos (AND/OR)
 * 
 * Os operadores AND (E) e OR (OU) podem ser usados da forma habitual em consultas SQL,
 * incluindo aninhamentos:
 * 
 * nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)
 * 
 * @author Paulo Alonso
 * 
 * @param <T> Tipo de retorno do decoder
 *
 */
public class QueryDecoder<T> {

    protected Decoder decoder;
    
    private final Function<Decoder, T> decodeFunction;

    public QueryDecoder(String query, Function<Decoder, T> decodeFunction) {
        this.decoder = this.createDecoder(query);
        this.decodeFunction = decodeFunction;
    }
    
    public Decoder getDecoder() {
        return this.decoder;
    }
    
    private Decoder createDecoder(String query) {
        if (query.startsWith(("(")))
            return new Group(query);
        else
            return new Expression(query);
    }
    
    public T decode() {
        return this.decodeFunction.apply(this.decoder);
    }
}
