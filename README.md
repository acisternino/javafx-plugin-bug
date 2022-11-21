# JavaFX Gradle plugin - bug 119 sample projects

**ARCHIVED - The bug seems to be gone with the lastest releases of JavaFX and the plugin (0.0.13)**

This repo contains two example projects for describing bug
[#119](https://github.com/openjfx/javafx-gradle-plugin/issues/119) of the
[JavaFX Gradle plugin](https://github.com/openjfx/javafx-gradle-plugin/).

- [javamod](./javamod): simple modular application that can be run using `./gradlew run`.
- [javamodfx](./javamodfx): simple JavaFX modular application that can be run
  using `./gradlew run` but would fail if argument parsing is performed.

The bug shows up when running, with `./gradlew run`, an application that performs
CLI argument parsing, e.g. using [picocli](https://picocli.info/).

What the JavaFX plugin seems to be doing is re-arranging the arguments of the
`java` executable invoked to run the program.

The plugin ends up adding some arguments meant for the JVM **after** the `--module`
option. However, as specified in [`java`'s manual](https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html),
everything that follows the `--module` option and its argument is passed to the
application as arguments to the main class.

If the application performs any type of argument parsing it may fail because of
unrecognized arguments.

This behavior is visible in the [error.txt](./error.txt) file that is a snippet
taken from running `./gradlew run` in the javamodfx project with debug enabled.

This is the first line reformatted for clarity:

```
2022-01-12T23:06:22.380+0100 [INFO] [org.javamodularity.moduleplugin.tasks.RunTaskMutator] jvmArgs for task run: [--add-modules, javafx.controls, --module-path, <all dependency jars>, --patch-module, com.example.javamodfx=/tmp/javafx-plugin-bug/javamodfx/build/resources/main, --module, com.example.javamodfx/com.example.javamodfx.App]
```

So far so good.

However, this is the third line where the JavaFX plugin reworks the command line:

```
2022-01-12T23:06:22.381+0100 [INFO] [org.gradle.process.internal.DefaultExecHandle] Starting process 'command '/usr/lib/jvm/java-17-openjdk/bin/java''. Working directory: /tmp/javafx-plugin-bug/javamodfx Command: /usr/lib/jvm/java-17-openjdk/bin/java --add-modules javafx.controls --module-path <all dependency jars> --patch-module com.example.javamodfx=/tmp/javafx-plugin-bug/javamodfx/build/resources/main --module com.example.javamodfx/com.example.javamodfx.App -Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.variant --module com.example.javamodfx/com.example.javamodfx.App
```

As it is clearly visible, the options

```
-Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.variant --module com.example.javamodfx/com.example.javamodfx.App
 ```

are appended **after** the first `--module` option and end up as arguments to `main()`:

```
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] Program args:
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] -Dfile.encoding=UTF-8
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] -Duser.country=US
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] -Duser.language=en
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] -Duser.variant
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] --module
2022-01-12T23:06:23.192+0100 [QUIET] [system.out] com.example.javamodfx/com.example.javamodfx.App
```
