@ECHO OFF
REM to be run in \text-ui-text

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM compile the code into the bin folder
javac -cp ..\src\main\java -Xlint:none -d ..\bin ..\src\main\java\reim\Deadline.java ..\src\main\java\reim\Event.java ..\src\main\java\reim\Reim.java ..\src\main\java\reim\ReimException.java ..\src\main\java\reim\Task.java ..\src\main\java\reim\Todo.java ..\src\main\java\reim\Parser.java ..\src\main\java\reim\Storage.java ..\src\main\java\reim\TaskList.java ..\src\main\java\reim\Ui.java
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\bin reim < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
