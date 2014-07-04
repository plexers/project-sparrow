@echo off
:START
cls
java -jar game.jar
echo/
echo/Program terminated...
echo/1. Rerun
echo/2. Exit
set /p choice=Choose [1]: 
if #%choice%==#2 goto END
goto START
:END
exit
