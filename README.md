[ProjectGithubUrl]:     https://github.com/michaelbel/bottomsheet
[ProjectLicenceUrl]:    http://www.apache.org/licenses/LICENSE-2.0
[ProjectBintrayUrl]:    https://bintray.com/michael-bel/maven/bottomsheet/_latestVersion
[ProjectGooglePlayUrl]: https://play.google.com/store/apps/details?id=org.michaelbel.bottomsheetdialog
[ApkFileUrl]:           https://github.com/michaelbel/BottomSheet/raw/master/app/release/bottomsheet-v1.1.1.apk
[MDGuidelinesUrl]:      https://material.io/guidelines/components/bottom-sheets.html#
[AndroidArsenalUrl]:    https://android-arsenal.com/details/1/6623

[LauncherIconPath]:    ../master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png
[GooglePlayBadgePath]: ../master/art/badges/google-play-badge.png

[ApiBadge]:            https://img.shields.io/badge/API-14%2B-blue.svg
[PlatformBadge]:       https://img.shields.io/badge/Platform-Android-blue.svg
[IDEBadge]:            https://img.shields.io/badge/Android_Studio-3.0.1-blue.svg
[LicenseBadge]:        https://img.shields.io/badge/License-Apache_v2.0-blue.svg
[BintrayBadge]:        https://api.bintray.com/packages/michael-bel/maven/bottomsheet/images/download.svg
[AndroidArsenalBadge]: https://img.shields.io/badge/Android%20Arsenal-BottomSheet-blue.svg?style=flat
[PlaystoreBadge]:      https://img.shields.io/badge/Google_Play-Demo-blue.svg

# BottomSheet
[![Platform][PlatformBadge]][ProjectGithubUrl]
[![Api][ApiBadge]][ProjectGithubUrl]
[![AndroidStudio][IDEBadge]][ProjectGithubUrl]
[![License][LicenseBadge]][ProjectLicenceUrl]
[![Bintray][BintrayBadge]][ProjectBintrayUrl]
[![AndroidArsenal][AndroidArsenalBadge]][AndroidArsenalUrl]
[![Playstore][PlaystoreBadge]][ProjectGooglePlayUrl]

<!--[![LauncherIcon][LauncherIconPath]][ProjectGithubUrl]-->

BottomSheet dialogs library for Android with [material design concept][MDGuidelinesUrl].
Bottom sheets slide up from the bottom of the screen to reveal more content.

<div style="dispaly:flex;">
    <img style="margin-left:0px;" src="/art/screens/light_1.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/light_2.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/light_3.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/light_4.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/dark_1.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/dark_2.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/dark_3.png" width="24%">
    <img style="margin-left:0px;" src="/art/screens/dark_4.png" width="24%">
</div>

<div style="dispaly:flex;">
    <img style="margin-left:0px;" src="/art/screens/light_landscape.png" width="49%">
    <img style="margin-left:0px;" src="/art/screens/dark_landscape.png" width="49%">
</div>    

## Demo

[![GetApp][GooglePlayBadgePath]][ProjectGooglePlayUrl]

Or [Download APK][ApkFileUrl]


## Download
```gradle
dependencies {
    implementation 'org.michaelbel:bottomsheet:1.1.1'
}
```

## Usage
```java
new BottomSheet.Builder(this)
    .setTitle(...)
    .setItems(..., ..., ...)
    .setCustomView(...)
    .setContentType(...)
    .setTitleTextColor(...)
    .setItemTextColor(...)
    .setBackgroundColor(...)
    .setIconColor(...)
    .setItemSelector(...)
    .setFullWidth(...)
    .setCellHeight(...)
    .setDividers(...)
    .setTitleMultiline(...)
    .setDarkTheme(...)
    .setCallback(new BottomSheet.Callback() {
        @Override
        public void onOpen() {
        }
        
        @Override
        public void onClose() {
        }
    })
    .show();
```

## License

    Copyright 2016-2018 Michael Bel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

<!-- Yes, this is a comment. -->
