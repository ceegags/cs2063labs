# TryFire

This is an app demonstrating how you can use
[Firebase](https://www.firebase.com/) in an Android app to easily add
functionality to an app that would otherwise require implementing a
server.

Follow these [instructions](https://www.firebase.com/docs/android/quickstart.html).

In step two, set the dependencies in build.gradle (Module: app). I
also had to set the packagingOptions. I did not need to download and
install the Firebase Android SDK and muck around with JARs.

This app uses the examples from steps 3-5. I didn't bother with
authenticating users or securing data. (But you would probably want to
do that in a non-toy app!)

To run this, be sure to set your Firebase URL in
MainActivity.java. Run the app on multiple devices at the same time
for maximum effect.
