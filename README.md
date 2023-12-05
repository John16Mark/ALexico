# Compiladores - Proyecto Final

### Compiladores 5CM1
Lenguaje: Java<br>
IDE utilizado: Eclipse Java<br>
Autor: Juan M.<br>

Scanner basado en el código proporcionado por el profesor.<br>
Parser basado en el código proporcionado por el profesor de la gramática SQL.<br>
Árbol que utiliza las clases de Expression y Statement proporcionadas por el profesor<br>

#### Consideraciones
<b>Impresiones</b>
En el archivo main `Main.java` está declarada la variable `boolean debug`, si tiene valor verdadero, se imprimirá tanto la lista de tokens después del Scanner, como el árbol (ya sea sintáctico o de sintaxis abstracta) después del Parser. Esta variable se podrá modificar a conveniencia dentro del código.

<b>Compilación</b>
Simplemente ejecute el archivo `compilar.bat` dentro de la carpeta principal desde la consola de comandos.

<b>Ejecución</b>
Puede ejecutar el programa de manera manual con el comando `java -cp bin def.Main ARGUMENTO` donde ARGUMENTO es la dirección del archivo de prueba a analizar. <br>
O bien puede hacerlo con los scripts ya creados:
<ol>
<b><li>ejecutar.bat:</b> Ejecuta el programa sin argumentos, lo cual hace que el programa lea prompts introducidos desde la consola.</li>
<b><li>ejecutarESPECIFICO.bat:</b> Este script recibirá un argumento, el argumento deberá ser el nombre del archivo de prueba que quiere que analice el programa (El archivo deberá estar dentro de la carpeta "pruebas").</li>
<b><li>ejecutarTODOS.bat:</b> Este script ejecutará el programa una vez por cada archivo que haya dentro de la carpeta "pruebas", así ejecutando el programa con todos los archivos de la carpeta como argumento.</li>
</ol>

## Analizador Léxico (Scanner)

El Scanner acepta las siguientes palabras reservadas:
|Palabra|Descripción|
|---|---|
|`var`|Declarar variable|
|`fun`|Definir función|
|`return`|Regreso de valor en una función|
|`if`|Condicional|
|`else`|Caso contrario de condicional|
|`for`|Ciclo for|
|`while`|Ciclo while|
|`print`|Función de impresión|
|`and`|Operador <b>and</b> (&&)|
|`or`|Operador <b>or</b> (\|\|)|
|`false`|Valor boleano falso|
|`true`|Valor boleano verdadero|
|`null`|Valor nulo|

Tokens de uno o dos caracteres:
||||||
|---|---|---|---|---|
|\(|\)|\{|\}|,|
|.|-|+|;|/|
|*|!|!=|=|==|
|>|>=|<|<=||

Literales válidas de números:
<img src="https://i.imgur.com/8nLHKME.png">

Literales válidas de identificadores:
<img src="https://i.imgur.com/Jefymr2.png">

El Scanner acepta también literales cadena.

## Analizador Sintáctico (Parser)

El paquete `parser` cuenta con tres analizadores sintácticos:<br>
<ol>
<b><li>ParserASDR:</b> Esta es una implementación de Analizador Sintáctico Recursivo Descendente (ASDR), simplemente recorre las producciones de manera recursiva y si finaliza correctamente, regresa un valor de <em>true</em></li>
<b><li>ParserArbol:</b> Este ASDR recorre las producciones de manera recursiva y va retornando nodos de árbol de todas las producciones que recorrió, formando así un Árbol Sintáctico. El árbol se imprime si la variable <em>debug</em> es verdadera</li>
<b><li>ParserASA:</b> Este ASDR recorre las producciones de manera recursiva y va retornando tipos de dato Expression y Statement que van formando un Árbol de Sintaxis Abstracta. El árbol se imprime si la variable <em>debug</em> es verdadera</li>
</ol>

Todos los analizadores sintácticos analizan basados en la gramática proporcionada por el profesor:
