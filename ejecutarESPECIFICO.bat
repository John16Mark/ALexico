@echo off
cls
CALL compilar.bat
REM Verifica si se proporciona al menos un argumento
if "%1"=="" (
    echo Debes proporcionar un argumento.
    goto :fin
)

REM Concatena el argumento a la cadena "./pruebas/"
set "ruta=./test/%1"

java -cp bin def.Main %ruta%

:fin
