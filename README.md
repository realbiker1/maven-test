[![Build Status](https://travis-ci.org/dverstap/wintersleep-nnzero.svg?branch=master)](https://travis-ci.org/dverstap/wintersleep-nnzero)
[![Maven Central](https://img.shields.io/maven-central/v/org.wintersleep.nnzero/wintersleep-nnwero.svg)]()
[![License](https://img.shields.io/github/license/dverstap/wintersleep-nnzero.svg)]()

Wintersleep 99.0-does-not-exist Maven Repo Server
=================================================

This is a lightweight Maven repository server that only serves empty
jar files and pom.xml files, for artifacts with version
`99.0-does-not-exist`. It is inspired by:

http://day-to-day-stuff.blogspot.com/2007/10/announcement-version-99-does-not-exist.html

Requests for artifacts with a different version than
`99.0-does-not-exist` receive an HTTP `404 Not Found` error.

This repository server not only serves empty "main" jar files, but
also:

- A minimal but valid `pom.xml`

- That `pom.xml` file is also included in the main jar file.

- `javadocs` and `sources` jar files, to avoid warnings when
  downloading those artifacts.

- MD5 and SHA1 checksums of the above files, again to avoid warnings.


Running the Server
------------------

This repository server is very minimal: it does not require any
dependencies at all, as it is based on the mini web server that is
built into the JDK (`com.sun.net.httpserver`).

It can be started like this:

> java -jar wintersleep-nnzero-0.1.0.jar

That will listen on all network interfaces on port 9999. It accepts
two optional arguments (bind address and port). For example to listen
only on the IPv4 loopback interface and on a different port, run as:

> java -jar wintersleep-nnzero-0.1.0.jar 127.0.0.1 8888

Logging can be enabled with `java.util.logging` logging properties,
for example:

> java -Djava.util.logging.config.file=config/logging.properties -jar wintersleep-nnzero-0.1.0.jar


99.0-does-not-exist
-------------------

`99.0-does-not-exist` is a bad version number in one corner-case: if
anybody uses a completely open-ended version range (such as
`[1.0.0,)`), they (or their dependees) might end up with an empty jar
file.

However, completely open-ended version numbers are generally a bad
idea. Ending up with an empty jar file then forces you to fix the
version number (in dependency management for instance).
