# QueryDecoder

###### Maven Dependency
```xml
<dependency>
    <groupId>com.github.paulosalonso</groupId>
    <artifactId>querydecoder</artifactId>
    <version>1.1.0</version>
</dependency>
```

O objetivo deste projeto é fornecer uma estrutura para desserialização de filtros a partir de uma string. A ideia surgiu da necessidade de disponibilizar uma maneira de obter dados filtrados através de uma API, onde o filtro pode ser utilizado como uma URL query.

Chamaremos essa string de "expressão". A expressão tem uma sintaxe mínima, a qual chamaremos de "expressão unitária", e que pode ser combinada/agrupada através de operadores lógicos (AND/OR) e parênteses.

A sintaxe da expressão mínima é a seguinte:

* atributo[operador]:valor

#### Operadores

| Operador        | Abreviação de         | Função                                                        |
|-----------------|-----------------------|---------------------------------------------------------------|
| EQ ou suprimido | Equal                 | Verifica se os valores são iguais                             |
| SW              | Starts With           | Verifica se um texto começa com o valor informado             |
| EW              | Ends With             | Verifica se um texto termina com o valor informado            |
| CT              | ConTains              | Verifica se um texto contém determinado texto em seu conteúdo |
| LT              | Less Than             | Verifica se é menor que determinado valor                     |
| LTE             | Less Than or Equal    | Verifica se é menor ou igual a determinado valor              |
| GT              | Greater Than          | Verifica se é maior que determinado valor                     |
| GTE             | Greater Than ou Equal | Verifica se é maior ou igual a determinado valor              |
| BT              | BeTween               | Verifica se está entre dois valores                           |
| IN              | IN                    | Verifica se está entre uma lista de valores                   |

##### Exemplos
 
* nome[EQ]:João da Silva, nome:João da Silva 
* nome[CT]:João
* valor[LT]:100
* valor[LTE]:100
* valor[GT]:100
* valor[GTE]:100
* valor[BT]:100-200
* valor[IN]:100,120,150

##### Operadores negados

É possível negar os operadores iniciando-os com 'N'. Por exemplo:

* nome[NEQ]:João da Silva
* nome[NCT]:João
* valor[NLT]:100
* valor[NLTE]:100
* valor[NGT]:100
* valor[NGTE]:100
* valor[NBT]:100-200
* valor[NIN]:100,120,150

#### Operadores lógicos (AND/OR)

Os operadores AND (E) e OR (OU) podem ser usados da forma habitual em consultas SQL, incluindo aninhamentos:

* nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)


#### Estrutura de objetos

A interface __com.alon.querydecoder.Decoder__ abstrai uma expressão. Ela tem duas implementações: 

* __com.alon.querydecoder.Expression__: representa uma expressão unitária.
* __com.alon.querydecoder.Group__: representa um grupo de expressões unitárias que estão entre parênteses.

Toda instância de __Decoder__ faz referência às suas próprias informações (atributo, operador e valor), e à próxima expressão, que é uma outra instância de __Decoder__. Dessa maneira podemos trabalhar com todos os níveis de agrupamento das expressões unitárias.

Vamos tomar como exemplo a expressão abaixo:

* nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)

Para converter essa string em uma estrutura de objetos, temos o seguinte:

```java
QueryDecoder queryDecoder = new QueryDecoder("nome:João da Silva AND (valor[LT]:100 OR valor[GT]:500)");
```

Para utilizar a estrutura criada a partir da string, chamamos o método "decode" de __QueryDecoder__, passando a função (__java.util.function.Function__) que deve ser executada, a qual receberá como parâmetro a instância de __Decoder__ que foi criada a partir da string:

```java
queryDecoder.decode(decoder -> System.out.println(decoder.toString()));
```

O código acima deve imprimir no console a mesma string de entrada, porém os métodos "toString" convertem a estrutura criada em uma nova string, e não apenas imprimem a string informada. Esse comportamento é utilizado para testes unitários, como pode ser visto na classe __com.alon.querydecoder.test.QueryDecoderTest__.

## SpringJpaSpecificationDecoder

A classe SpringJpaSpecificationDecoder foi movida para o projeto [Spring CRUD Base](https://github.com/paulosalonso/spring-crud-base/blob/master/README.md#springjpaspecificationdecoder) a partir da versão 1.1.0.
