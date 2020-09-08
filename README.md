# Toy Service Engine
Implementation of Java Sockets to show RPC communication between a server and 
clients.

## Requirements

- Java OpenJDK >= 14.x [Installation Guide](https://docs.oracle.com/en/java/javase/14/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A)
- JavaFX 14.x [Installation Guide](https://gluonhq.com/products/javafx/)
- Create a `PATH_TO_FX` environment variable that points to the JavaFX lib folder

Linux/Mac:

```shell script
export PATH_TO_FX=path/to/javafx-sdk-14/lib
```

Windows:
```shell script
set PATH_TO_FX="path\to\javafx-sdk-14\lib"
```

## Running in the terminal
In the `out/artifacts` there is an executable file for a server and client.
Click to run or execute in the terminal.

Here are the commands if you want to run them directly.

Client (Linux/Mac):
```shell script
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.binding=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED -jar Client.jar
```

Client (Windows):
```shell script
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.binding=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED -jar Client.jar
```

Server (Linux/Mac):
```shell script
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.binding=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED -jar Server.jar
```

Server (Windows):
```shell script
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.binding=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED -jar Server.jar
```

## Running through the Intelliji IDE

Go to `Run | Edit Configurations` for both the `ClientApplication.java` and `ServerApplication.java`. Under 
VM options add the following:

```
--module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.binding=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
```

> __Note:__ Replace $PATH_TO_FX with the full path to the javafx-sdk `lib` folder

## Team
[Stephen Wanyee](https://github.com/steekam)

[Allan Vikiru](https://github.com/AllanVikiru)

[Nicole Muswanya](https://github.com/Naym0)

[Daniel Olamide](https://github.com/steekam)
