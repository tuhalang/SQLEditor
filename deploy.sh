rm -rf org WEB-INF META-INF
./gradlew clean bootWar
jar -xvf ./build/libs/SQLEditor.war