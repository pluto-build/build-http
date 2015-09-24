#/bin/sh

ARGS="build-wget build.pluto.buildwget.WGetBuilder.factory build.pluto.buildwget.WGetInput $@"

mvn compile exec:java -Dexec.args="$ARGS"
