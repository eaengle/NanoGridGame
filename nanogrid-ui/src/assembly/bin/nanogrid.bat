@echo off
set "APP_HOME=%~dp0.."
start "" javaw -jar "%APP_HOME%\lib\nanogrid.jar" %*
