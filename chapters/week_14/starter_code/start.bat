@echo off
call mvn -q package
java -cp target\classes com.campusflow.Main
