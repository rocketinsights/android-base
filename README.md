# Rocket's Kotlin Android Architecture Components Example

This is the first version of a mock application that hopefully will evolve into the base project for Rocket's Android application.

[![Build Status](https://www.bitrise.io/app/edf2965e90d6ca81/status.svg?token=M9TjJbSh1cmaUfFqzBkEUg&branch=master)](https://www.bitrise.io/app/edf2965e90d6ca81)

## Application Installation
Below are the instructions on how to install the Android app.

### Enable Installation from Unknown Sources
First, for any given device, it'll need to have installation from unknown sources enabled.

1. Open the Settings app.
2. Tap on Security.
3. Scroll to Unknown sources and turn the setting on.

Next, there are a few ways to get the latest application.

### Via qa@yourdomainname.com
Whenever the newest build is available, buddybuild emails qa@yourdomainname.com with a link to the latest apk. Tap on that link from the device. Once it has downloaded, tapping on the app should show a prompt to confirm the installation of the application. Occasionally, depending on device or manufacturer, you need to go to the Downloads application and install from there. If there's an issue installing from the mail application, try that.

### Via Slack Channel
Whenever a new build is available, the ﻿#androidslackchannel﻿ slack channel has an automated message sent to it. Open the link in that message from the device and follow the above instructions.

### Request a Specific Build
If you want a specific build, let me (Android developer) know and I can have buddybuild send you that build directly. From there, you can follow the directions for qa@yourdomainname.com.

### Via Android Studio
If you get set up with Android Studio﻿ and the GitHub repo﻿ on your local machine, you'll be able to install it directly from there. If you need help with this, feel free to let me (again, Android developer) know and I'd be happy to walk you through it.

# Continuous Integration
There are a few options for different CI solutions. In addition to a custom Jenkins setup, a few are listed below with pros and cons.

### Bitrise
[Bitrise](https://www.bitrise.io) has a variety of [open source build steps](https://github.com/bitrise-io). One of the big features is having secure keystore download and signing built into the platform. In addition, UI tests are included and easy to set up.

### CircleCI (WIP)
Lateral View uses [CircleCI](https://circleci.com) for their projects and it came strongly recommended. One thing that was a bit of a hangup is that UI tests took some configuration to get working and using a secure keystore wasn't available out of the box. One advantage of Circle is that they support many different platforms so could be a central location for all CI.

### Travis CI (WIP)
I had a lot of trouble getting UI tests running on [Travis](https://travis-ci.com). When trying to use the default recommendations from the standard documentation, I wasn't able to get it running, unfortunately.

### App Center (WIP)
When researching, I had read somewhere that [App Center](https://appcenter.ms) doesn't support building pull requests at this time. Maybe I shouldn't have believed it because that was very surprising. It appears that that is no longer the case, so I'll need to revisit it.

### Nevercode (WIP)
The biggest issue I found with [Nevercode](https://nevercode.io/) is that it didn't seem customizable and it didn't run UI tests. It was slow while initially setting up, but then ran without issue. I didn't dive into code signing because I couldn't find much documentation about it.
