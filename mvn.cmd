@echo off
set "DIR=%~dp0"
if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\Java\jdk-26.0.2" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
    )
)
call "%DIR%apache-maven-3.9.9\bin\mvn.cmd" -Dmaven.repo.local="%DIR%.m2\repository" %*
