#!/bin/bash

set -e -x

mvn clean package -Dmaven.test.skip=true
# Majority of the time, the destination directory will be "target"
# Use this as a default if the DIST_DIRECTORY is unset.
DIST_DIRECTORY_FINAL="${DIST_DIRECTORY:-target}"

chmod 777 $DIST_DIRECTORY_FINAL

cp -r ./$DIST_DIRECTORY_FINAL/* ../dist

ls -l ../dist
