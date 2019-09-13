# QueryDecoder

O objetivo deste projeto é fornecer uma estrutura para desserialização de filtros a partir de uma string. A ideia surgiu a partir da necessidade de disponibilizar uma maneira de obter dados filtrados através de uma API, onde o filtro pode ser utilizado como uma URL query.

Chamaremos essa string de "expressão". A expressão tem uma sintaxe mínima, a qual chamaremos de "expressão unitária", e que pode ser combinadas/agrupadas através de operadores lógicos (AND/OR).

A sintaxe da expressão mínima é a seguinte:

* atributo[operador]:valor

#### Operadores

| Operador        | Abreviação de         | Função                                                        |
|-----------------|-----------------------|---------------------------------------------------------------|
| EQ ou suprimido | Equal                 | Verifica se os valores são iguais                             |
| CT              | ConTains              | Verifica se um texto contém determinado texto em seu conteúdo |
| LT              | Less Than             | Verifica se é menor que determinado valor                     |
| LTE             | Less Than or Equal    | Verifica se é menor ou igual a determinado valor              |
| GT              | Greater Than          | Verifica se é maior que determinado valor                     |
| GTE             | Greater Than ou Equal | Verifica se é maior ou igual a determinado valor              |
| BT              | BeTween               | Verifica se está entre dois valores                           |
| IN              | IN                    | Verifica se está entre uma lista de valores                   |

#### Exemplos
 
* nome[EQ]:João da Silva, nome:João da Silva 
* nome[CT]:João
* valor[LT]:100
* valor[LTE]:100
* valor[GT]:100
* valor[GTE]:100
* valor[BT]:100-200
* valor[IN]:100,120,150

#### Operadores lógicos (AND/OR)

Os operadores AND (E) e OR (OU) podem ser usados da forma habitual em consultas SQL, incluindo aninhamentos:

* nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)


#### Estrutura de objetos

A interface __com.alon.querydecoder.Decoder__ abstrai uma expressão. Ela tem duas implementações: 

* __com.alon.querydecoder.Expression__: representa uma expressão unitária.
* __com.alon.querydecoder.Group__: representa um grupo de expressões unitárias que estão entre parênteses.

Toda instância de __Decoder__ faz referência às suas próprias informações, (atributo, operador e valor), como à próxima expressão, que  uma outra instância de __Decoder__. Dessa maneira podemos trabalhar com todos os níveis de agrupamento das expressões unitárias.

Vamos tomar como exemplo a expressão abaixo:

* nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)

Para converter essa string na estrutura proposta por este projeto, temos o seguinte:

```java
Expression expression = new QueryDecoder("nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)").getDecoder();
```
