#!/usr/bin/env bash

set -e

function cleanup {
    kill %1
}

trap cleanup EXIT

# TODO: ./mvnw

mvn clean install

java -jar target/wintersleep-nnzero-*.jar 127.0.0.1 9999 &
sleep 2 # wait until it's started up ...

mkdir -p target/test
cd target/test
SERVER_URL=http://127.0.0.1:9999/maven2
CURL='curl --silent --fail'
for ext in .jar .pom -javadoc.jar -sources.jar
do
    URL=${SERVER_URL}/org/wintersleep/nnzero/wintersleep-nnzero/99.0-does-not-exist/wintersleep-nnzero-99.0-does-not-exist

    ${CURL} -O ${URL}${ext}
    for checksum in md5 sha1
    do
        ${CURL} -O ${URL}${ext}.${checksum}
    done
done

cd -
mvn javadoc:jar source:jar
