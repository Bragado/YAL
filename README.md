# YAL

## Auto-Avaliação

Antes de procedermos à auto avaliação, gostariamos de relembrar que o projeto não está completo, pois faltou a conversão da AST para HIR no final da análise semântica, no entanto, tanto a análise semântica como a conversão para JVM foram implementados faltando só a "ponte" entre as duas fases que ficou incompleta.
Iremos dividir o projeto em quatro fases: Análise Lexil (10%) ; Análise Sintática (45%) ; Análise Semântica (30%) ; JVM (15%)

### Luís Coelho

Análise Lexical - 7/10 - fiz a análise lexical sozinho com uma pequena ajuda do Pedro Gomes

Análise Sintática - 30/45 - transformei a linguagem para LL(1) ; calculei o first e follow sets e tabela; implementei o algoritmo preditivo ; criei uma nova liguagem para transformar a CST retornada numa AST

Análise Semântica - 30/30 - tudo o que foi implementado na análise semântica foi feito por mim

JVM - 0/15

Total de contribuição: 67%

### Pedro Gomes

Neste trabalho a minha contribuição para ele foi na fase da análise sintática, em que fiz a identificação dos first e follow sets e consequente tabela LL(1). Nesta fase tivemos de alterar a linguagem por causa de muita recursão à esquerda. Por isso a minha contribuição nesta fase foi de cerca de 40%.
Na fase de JVM a minha contribuição foi apenas de ajudar em perceber algumas conversões pelo que a minha contribuição nesta fase foi de apenas 10%.
Pelo que a minha contribuição final para o projeto foi de 20%

### Arthur Matta

A minha contribuição para este trabalho se deu no teste da linguagem após sua conversão para LL(1) (encontrando um erro na mesma), na análise sintática, pelo que considero uma contribuição de 15% nesta etapa; e na elaboração da HIR e sua respectiva conversão para JVM, pelo que  considero uma contribuição de 90% nesta etapa.

Contribuição total: 20,25%


### André Maia

A minha contribuição após grande dificuldade de perceber e entender o que era pedido e com ajuda dos meus colegas de grupo é de 15%
