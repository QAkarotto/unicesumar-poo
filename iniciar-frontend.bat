@echo off
title Sistema Academico - Frontend ReactJS
cd /d "%~dp0projects\academico\frontend"
echo ========================================================
echo Iniciando Frontend ReactJS (Porta 5173)...
echo ========================================================
call npm install
call npm run dev
pause
