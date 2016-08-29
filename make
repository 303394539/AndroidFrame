#!/bin/bash

DIR=$(pwd)
BUILDDIR=/build/outputs/aar

for file in view cache net utils notice repair
do
echo ${DIR}/${file}
cd ${DIR}/${file}
gradle build
cp ${DIR}/${file}${BUILDDIR}/${file}-release.aar $DIR/aar
done

exit 0
