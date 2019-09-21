# QueryDecoder

```xml
<dependency>
    <groupId>com.github.paulosalonso</groupId>
    <artifactId>querydecoder</artifactId>
    <version>1.0.0</version>
</dependency>
```

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

Como parte do projeto, e um exemplo da utilização, existe a classe __SpringJpaSpecificationDecoder__, que utiliza a classe __QueryDecoder__ para aplicar filtros em consultas utilizando o __Spring Data JPA__. Para isso, a classe __SpringJpaSpecificationDecoder__ implementa __org.springframework.data.jpa.domain.Specification__. Para utilizá-la, o repositório Spring deve implementar, além de __org.springframework.data.jpa.repository.JpaRepository__, a interface __org.springframework.data.jpa.repository.JpaSpecificationExecutor__:

```java
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, JpaSpecificationExecutor<Pessoa> {
}
```

Assim, ficam disponíveis os métodos de __JpaSpecificatonExecutor__ que recebem um __Specification__ como parâmetro. __SpringJpaSpecificationDecoder__ pode ser usado da seguinte maneira:

```java
@Autowired
private PessoaRepository repository;
.
.
.
Specification spec = new SpringJpaSpecificationDecoder("nome[CT]:Paulo");

List<Pessoa> pessoas = this.repository.findAll(spec);
```

O código acima deve retornar uma lista com todas as pessoas que contém "Paulo" no nome.
