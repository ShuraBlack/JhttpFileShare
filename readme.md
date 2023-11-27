# JhttpFileShare
JhttpFileShare is a straightforward application designed for local data exchange. It provides a simple interface for sharing 
files across different devices within your network. This tool is not just about transferring data, it’s about making local 
file sharing uncomplicated and efficient.

This application will start a local server to access your directories and transfer files to other devices.
> ❗️Disclaimer: Your JDK executable needs to be able to bypass your firewall before the server is accessible in your local network.<br>
> [Win11 Tutorial by pureinfotech](https://pureinfotech.com/allow-apps-firewall-windows-11/)

## License
This project is licensed under the **Apache 2.0** license.<br>
**Copyright (c)** 2023 ShuraBlack<br>
For more information about the license, see [apache.org](https://www.apache.org/licenses/LICENSE-2.0)
or check the <b>LICENSE</b> file in the project

## Dependencies
❤️ Thanks to all the great programmers out there, which made all of this possible

This project requires Java 11+ [SDK](https://www.oracle.com/java/technologies/downloads/)<br>
All dependencies are managed by [Maven](https://maven.apache.org)<br>

Logging:
- [Log4j Core](https://github.com/apache/logging-log4j2) **2.20.0**
- [Log4j API](https://github.com/apache/logging-log4j2) **2.0.5**
- [Log4J Simple](https://github.com/apache/logging-log4j2) **2.0.5**


Testing:
- [Junit5](https://github.com/junit-team/junit5) **5.9.2**

## Download ![Static Badge](https://img.shields.io/badge/version-v0.1.5-%230679b6)
Currently this project only supports [GitHub Release](https://github.com/ShuraBlack/JhttpFileShare/releases) with a **.jar**.

## Java Arguments
```
JhttpFileShare Server x.x.x

USAGE:
	java -jar JhttpFileShare.jar [options/flags]

FLAGS:
	-ip					Shows all Network Interfaces
	-v, -verbose				Enables verbose mode (more informations Server-side)
	-nr					Disables the root folder restriction (Access entire file browser)
	-up					Enables uploading to the host mashine
	-h, -help				Shows this help

OPTIONS:
	-ip=<network_name>			Sets the IP Address to the given network name [default: 0.0.0.0]
	-p, -port=<port>			Sets the Port to the given port [default: 80]
	-threads=<size>				Sets the Thread Pool Size to the given size [default: 3]
	-root=<path>				Sets the root folder [default: user.dir]
```

## Usage
Start the server with the following command:
```
java -jar JhttpFileShare.jar 
```
The CLI will show you the IP Address and Port of the server. You can now access the server with any device in your local network.
> ❕Disclaimer: I recommend to keep the default port, since you dont need to write it explicitly in the url.

If you are not sure about the IP Address of your server, you can use the `-ip` flag to show all available network interfaces
and set the wanted Network Interface with the `-ip=<network_name>` option.
```
java -jar JhttpFileShare.jar -ip=wlan0
```

## Example Page
![Example](https://github.com/ShuraBlack/JhttpFileShare/assets/69372954/1294609d-8dff-42df-a981-2c9928646453)

