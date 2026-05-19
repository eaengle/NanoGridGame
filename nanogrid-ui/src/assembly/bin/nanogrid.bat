@echo off
setlocal

set "APP_HOME=%~dp0.."
java -jar "%APP_HOME%\lib\nanogrid.jar" %*
