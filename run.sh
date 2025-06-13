#!/bin/bash
javadoc -docletpath target/LoadBalancer-1.0-SNAPSHOT-jar-with-dependencies.jar -doclet lb.Doclet "$@"
