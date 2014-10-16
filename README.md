genpmk-hadoop
====

A hadoop (cascading) job that generates a precomputed dictionary (PMK) file for
cowpatty.

Motivation
---
I wanted to learn cascading, jni, and maven.

While it's possible to use this to generate pmk files it's almost certainly less
expensive to put your video card to work.

I decided to release genpmk-hadoop anyway so that I can potentially learn more
about these technologies.

Running the job
---
I cheated and used the tomcat native build. The binaries are included here
though (prebuilt for amazon linux). You can build them yourself using
tomcat.patch.

To run the job on EMR follow thse steps:

1. build the project:

    mvn clean package

2. upload the resulting jar to s3:

    s3cmd put cascading/target/cascading-0.0.1-SNAPSHOT-jar-with-dependencies.jar s3://.../test.jar

3. Edit the run.sh and install_native.sh script to your liking.

4. Upload the native libs and install script to your emr bucket

    s3cmd put -P native-libs.tgz s3://.../native-libs.tgz
    s3cmd put -P innstall_native.sh s3://.../install_native.sh

5. Run the job

    ./run.sh


There parameters to the job jar are:

    -i input path, e.g. s3n://.../wordlists/
    -o output path, e.g. s3n://.../myjob-output
    -p run the preprocessing step
    -s ssid


A quick note about performance
---
I ran the job using the rockyou wordlist. It took a little longer than 1 hour to
complete with 8 m3.xlarge nodes. I did some rough math and it seems I was
generating hashes at 140/s/runner on EMR vs 155/s on my laptop. I want to try
again using c3.xlarge nodes.
