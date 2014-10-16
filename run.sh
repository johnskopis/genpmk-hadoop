#!/bin/bash -ex

usage() {
  cat << EOF
This script is used to launch an EMR job. You should ensure the buckets exist
before running. The 'install_native.sh' and 'native-libs.tgz' should be publicly
accessible and in the EMR bucket.

Don't forget to edit the install_native script first.
EOF

exit
}

NAME=$1
[ -z "$NAME" ] && usage
EMR=$NAME-emr
DATA=$NAME-data
LOGS=$NAME-logs

# Upload the JAR file to S3.
#s3cmd put cascading/target/cascading-0.0.1-SNAPSHOT-jar-with-dependencies.jar s3://$EMR/$NAME.jar

# Upload the data file we want to word count.
#s3cmd put rockyou.txt s3://$DATA/wordlists/rockyou.txt

elastic-mapreduce --create --name "$NAME" \
  --ami-version 3.2.1 \
  --hadoop-version 2.4.0 \
  --debug \
  --enable-debugging \
  --log-uri s3n://$LOGS/logs \
  --jar s3n://$EMR/$NAME.jar \
  --arg "-i" --arg "s3n://$DATA/wordlists/" \
  --arg "-o" --arg "s3n://$DATA/rockyou" \
  --arg "-s" --arg "test" --arg "-p" \
  --num-instances 1 \
  --slave-instance-type m3.xlarge \
  --master-instance-type m3.xlarge \
  --bootstrap-action s3://$EMR/install_native.sh
