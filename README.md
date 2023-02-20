# Digi Permission- An easy way to implement modern permission instructions popup.
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=18)
[![](https://jitpack.io/v/hamidfathi1998/Digi-Permission.svg)](https://jitpack.io/#hamidfathi1998/Digi-Permission)

Digi Permission is an extension Android library, easy way to implement modern permission instructions popup.You can use it for basic permission request occasions or handle more complex conditions

<p align="left">
 <a><img width="25%" height="420px"  src="gif/digi-permission.gif"></a>
</p>


# 💥 Setup
Add these to your `build.gradle` file 

```groovy
repositories {
  google()
  mavenCentral()
}
dependencies {
    implementation 'com.github.hamidfathi1998:Digi-Permission:@VERSION'
}
```

# ⚡ Usage
Use Digi-Permission to request Android runtime permissions is extremely simple.

For example. If you want to request ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN and INTERNET permissions, declared them in the AndroidManifest.xml first.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.permissionx.app">

    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.INTERNET" />

</manifest>
```


Then you can use below codes to request.

```kotlin
DigiPermission.initialize(this)
    .addPermissions(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
    .permissionResult { allGranted, grantedList, deniedList ->
        if (allGranted) {
            Toast.makeText(
                this,
                "Permissions Are Granted \n$grantedList",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                "Permissions Are Denied: \n$deniedList",
                Toast.LENGTH_LONG
            ).show()
        }
    }
```

Pass any instance of FragmentActivity or Fragment into init method, and specify the permissions that you want to request in the permissions method, then call request method for actual request.
The request result will be callback in the request lambda. allGranted means if all permissions that you requested are granted by user, maybe true or false. grantedList holds all granted permissions and deniedList holds all denied permissions.



## If this project helps you in anyway, show your love :heart: by putting a :star: on this project :v:

## Contributing

Please fork this repository and contribute back using
[pull requests](https://github.com/hamidfathi1998/Digi-Permission/pulls).

Any contributions, large or small, major features, bug fixes, are welcomed and appreciated
but will be thoroughly reviewed .

### - Contact - Let's become friend
- [Github](https://github.com/hamidfathi1998)
- [Linkedin](https://www.linkedin.com/in/hamidfathi1998/)