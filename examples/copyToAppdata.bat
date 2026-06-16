@echo off
setlocal

REM Destination directory (Appdata)
set "DESTINATION=%APPDATA%\decision"

REM Creates directory if not exists
if not exist "%DESTINATION%" (
    mkdir "%DESTINATION%"
)

REM Removes all .dhis files in destination
del /Q "%DESTINO%\*.dhis" 2>nul

REM Copy all .dhis files from actual directory to destination
copy "%~dp0*.dhis" "%DESTINATION%\" /Y

echo Files copied to:
echo %DESTINATION%

pause