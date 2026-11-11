@echo off
title Sistema Academico - Inicializador Geral
echo ========================================================
echo Iniciando Sistema Academico Full Stack...
echo ========================================================
echo 1. Abrindo Backend Spring Boot (Porta 8080)...
start "Backend Spring Boot" cmd /k "cd /d %~dp0projects\academico && call mvnw.cmd spring-boot:run"

echo 2. Aguardando inicializacao...
timeout /t 6 /nobreak >nul

echo 3. Abrindo Frontend ReactJS (Porta 5173)...
start "Frontend ReactJS" cmd /k "cd /d %~dp0projects\academico\frontend && call npm install && call npm run dev"

echo ========================================================
echo Tudo pronto!
echo Frontend: http://localhost:5173
echo Backend:  http://localhost:8080
echo ========================================================
pause
