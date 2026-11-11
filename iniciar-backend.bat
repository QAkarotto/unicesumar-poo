@echo off
title Sistema Academico - Backend Spring Boot
cd /d "%~dp0projects\academico"
echo ========================================================
echo Iniciando Backend Spring Boot (Porta 8080)...
echo ========================================================
call mvnw.cmd spring-boot:run
pause
