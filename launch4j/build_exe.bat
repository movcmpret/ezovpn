@echo off

echo Executing launch4jc.exe launch4jconfig.xml
launch4jc.exe launch4jconfig.xml || echo. && echo. && echo Did you install launch4j and added its dir to PATH? && echo. && echo. && pause && exit 1337
