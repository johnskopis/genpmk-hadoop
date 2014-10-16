#!/bin/bash -ex
set -e
wget -S -T 10 -t 5 http://$NAME-emr.s3.amazonaws.com/native-libs.tgz
mkdir -p /home/hadoop/lib/native
tar -C /home/hadoop/lib/native -xzf native-libs.tgz
