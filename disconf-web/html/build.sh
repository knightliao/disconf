#!/bin/bash

cd "${0%/*}"

#
# 打包FE包
#

#
if [ -d "output" ]; then
    printf '%s\n' "Removing output"
    rm -rf output
fi

mkdir -p output

cp assets output -rp
cp dep output -rp
cp *.html output -rp
