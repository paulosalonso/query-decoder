# QueryDecoder

O objetivo deste projeto é fornecer uma estrutura para desserialização de filtros a partir de uma string. 

Chamaremos essa string de "expressão". A expressão tem uma sintaxe mínima, a qual chamaremos de "expressão unitária", e que pode ser combinadas/agrupadas através de operadores lógicos (AND/OR).

A sintaxe da expressão mínima é a seguinte:

* atributo[operador]:valor

Tabela de operadores:

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
