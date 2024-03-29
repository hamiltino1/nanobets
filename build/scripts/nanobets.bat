@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  nanobets startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and NANOBETS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\nanobets-1.jar;%APP_HOME%\lib\mariadb-java-client-2.7.2.jar;%APP_HOME%\lib\spark-core-2.9.1.jar;%APP_HOME%\lib\spark-core-2.9.1-sources.jar;%APP_HOME%\lib\slf4j-simple-1.7.30.jar;%APP_HOME%\lib\slf4j-simple-1.7.30-sources.jar;%APP_HOME%\lib\jnano-2.8.1-V21.2.jar;%APP_HOME%\lib\gson-2.8.6.jar;%APP_HOME%\lib\gson-2.8.6-sources.jar;%APP_HOME%\lib\commons-lang3-3.11.jar;%APP_HOME%\lib\commons-lang3-3.11-sources.jar;%APP_HOME%\lib\sql2o-1.6.0.jar;%APP_HOME%\lib\sql2o-1.6.0-sources.jar;%APP_HOME%\lib\mysql-connector-java-8.0.18.jar;%APP_HOME%\lib\mysql-connector-java-8.0.18-sources.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\json-simple-1.1.1-sources.jar;%APP_HOME%\lib\httpclient-4.5.11.jar;%APP_HOME%\lib\httpclient-4.5.11-sources.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\log4j-1.2.17-sources.jar;%APP_HOME%\lib\sqlite-jdbc-3.30.1.jar;%APP_HOME%\lib\sqlite-jdbc-3.30.1-sources.jar;%APP_HOME%\lib\commons-codec-1.14.jar;%APP_HOME%\lib\commons-codec-1.14-sources.jar;%APP_HOME%\lib\jackson-databind-2.11.0.jar;%APP_HOME%\lib\jackson-databind-2.11.0-sources.jar;%APP_HOME%\lib\qrcodegen-1.6.0.jar;%APP_HOME%\lib\qrcodegen-1.6.0-sources.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\Java-WebSocket-1.5.1.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\jetty-webapp-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-server-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-servlet-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-security-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-server-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-servlet-9.4.18.v20190429.jar;%APP_HOME%\lib\protobuf-java-3.6.1.jar;%APP_HOME%\lib\junit-4.10.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\blake2b-1.0.0.jar;%APP_HOME%\lib\jackson-annotations-2.11.0.jar;%APP_HOME%\lib\jackson-core-2.11.0.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\websocket-client-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-client-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-http-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-common-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-io-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-xml-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-api-9.4.18.v20190429.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar;%APP_HOME%\lib\jetty-util-9.4.18.v20190429.jar


@rem Execute nanobets
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %NANOBETS_OPTS%  -classpath "%CLASSPATH%" nanobets.Main %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable NANOBETS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%NANOBETS_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
