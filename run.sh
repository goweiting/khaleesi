#!/bin/sh
cd java
pwd
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DIR="$DIR/libs/"
export LD_LIBRARY_PATH=$DIR
java -cp ../out/production/khaleesi:libs/jssc.jar:libs/v4l4j.jar strategy.Strategy
