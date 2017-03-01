# BuildSample
Use Gradle build your project

/config.gradle
ext {

    android = [compileSdkVersion: 25,
               buildToolsVersion: "25",
               applicationId    : "ldroid.buildsample",
               minSdkVersion    : 18,
               targetSdkVersion : 25,
               versionCode      : 1,
               versionName      : "1.0"]

    support_library_version = '25.0.0'
}

/build.gradle
// Top-level build file where you can add configuration options common to all sub-projects/modules.
apply from: "config.gradle"

/**
 * Configures the build script classpath for this project.
 *
 * The buildscript {} block is where you configure the repositories and
 * dependencies for Gradle itself--meaning, you should not include dependencies
 * for your modules here. For example, this block includes the Android plugin for
 * Gradle as a dependency because it provides the additional instructions Gradle
 * needs to build Android app modules.
 */
buildscript {

    /**
     * The repositories {} block configures the repositories Gradle uses to
     * search or download the dependencies. Gradle pre-configures support for remote
     * repositories such as JCenter, Maven Central, and Ivy. You can also use local
     * repositories or define your own remote repositories. The code below defines
     * JCenter as the repository Gradle should use to look for its dependencies.
     */
    repositories {
        jcenter()
    }

    /**
     * The dependencies {} block configures the dependencies Gradle needs to use
     * to build your project. The following line adds Android Plugin for Gradle
     * version 2.2.2 as a classpath dependency.
     */
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.2'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

/**
 * The allprojects {} block is where you configure the repositories and
 * dependencies used by all modules in your project, such as third-party plugins
 * or libraries. Dependencies that are not required by all the modules in the
 * project should be configured in module-level build.gradle files. For new
 * projects, Android Studio configures JCenter as the default repository, but it
 * does not configure any dependencies.
 */
allprojects {

    repositories {
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}





/app/build.gradle
/**
 * The first line in the build configuration applies the Android plugin for
 * Gradle to this build and makes the android {} block available to specify
 * Android-specific build options.
 */
apply plugin: 'com.android.application'

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
def keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
def keystoreProperties = new Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

/**
 * The android {} block is where you configure all your Android-specific
 * build options.
 */
android {

    /**
     * compileSdkVersion specifies the Android API level Gradle should use to
     * compile your app. This means your app can use the API features included in
     * this API level and lower.
     *
     * buildToolsVersion specifies the version of the SDK build tools, command-line
     * utilities, and compiler that Gradle should use to build your app. You need to
     * download the build tools using the SDK Manager.
     */
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion

    /**
     * The defaultConfig {} block encapsulates default settings and entries for all
     * build variants, and can override some attributes in main/AndroidManifest.xml
     * dynamically from the build system. You can configure product flavors to override
     * these values for different versions of your app.
     */
    defaultConfig {
        /**
         * applicationId uniquely identifies the package for publishing.
         * However, your source code should still reference the package name
         * defined by the package attribute in the main/AndroidManifest.xml file.
         */
        applicationId rootProject.ext.android.applicationId
        // Defines the minimum API level required to run the app.
        minSdkVersion rootProject.ext.android.minSdkVersion
        // Specifies the API level used to test the app.
        targetSdkVersion rootProject.ext.android.targetSdkVersion
        // Defines the version number of your app.
        versionCode rootProject.ext.android.versionCode
        // Defines a user-friendly version name for your app.
        versionName rootProject.ext.android.versionName
        // multiDexEnabled true

    }

    signingConfigs {

        release {
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }

    /**
     * The buildTypes {} block is where you can configure multiple build types.
     * By default, the build system defines two build types: debug and release. The
     * debug build type is not explicitly shown in the default build configuration,
     * but it includes debugging tools and is signed with the debug key. The release
     * build type applies Proguard settings and is not signed by default.
     */
    buildTypes {

        product {
            minifyEnabled true // Enables code shrinking for the release build type.
            shrinkResources true
            zipAlignEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        /**
         * By default, Android Studio configures the release build type to enable code
         * shrinking, using minifyEnabled, and specifies the Proguard settings file.
         */
        release {
            signingConfig signingConfigs.release
        }

        debug {
            signingConfig signingConfigs.release
        }
    }

    lintOptions {
        checkReleaseBuilds false
        abortOnError false
    }

    dexOptions {
        javaMaxHeapSize "4g"
    }

    /**
     * The productFlavors {} block is where you can configure multiple product
     * flavors. This allows you to create different versions of your app that can
     * override defaultConfig {} with their own settings. Product flavors are
     * optional, and the build system does not create them by default. This example
     * creates a free and paid product flavor. Each product flavor then specifies
     * its own application ID, so that they can exist on the Google Play Store, or
     * an Android device, simultaneously.
     */
    productFlavors {
        // Define separate dev and prod product flavors.
        dev {
            // dev utilizes minSDKVersion = 21 to allow the Android gradle plugin
            // to pre-dex each module and produce an APK that can be tested on
            // Android Lollipop without time consuming dex merging processes.
            minSdkVersion 21
        }
        prod {
            // The actual minSdkVersion for the application.
            minSdkVersion 11
        }
    }


}

/**
 * The dependencies {} block in the module-level build configuration file
 * only specifies dependencies required to build the module itself.
 */
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile "com.android.support:appcompat-v7:${support_library_version}"
}

