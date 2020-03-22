# javaS3Throttler

## What is this?
Nothing serious. Just trying out the AWS SDK in Java.

## OK, but what does it do?
Configure it as described below, and it will upload the file you specify from your computer up to S3, at a rate limited speed.

## How To Use
* Open up the project IntelliJ.
* Edit the variables at the beginning of `Main.java`'s `main()` function to specify your file path (`filePath`), AWS access key id (`accessKeyId`), AWS secret key (`secretKey`) and S3 bucket (`bucket`).
* OPTIONAL: Change the key name used for the object (`dstKey`).
* OPTIONAL: Change the byte rate used for throttling (`txSpeed`).