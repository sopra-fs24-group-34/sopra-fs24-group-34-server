# SoPra FS24 group 34 Server

## Introduction
Does the person have brown eyes? No! Did you forgot this game? "Guess Who" is back. We implemented the traditional tabletop game where users have to guess characters by asking yes or no characters. 
The rules are simple:
1. **Setup**:  Each player selects a character card without revealing it to their opponent. This card represents the character the opponent must guess
2. **Taking Turns**: Players take turns asking yes or no questions about the opponent's character in order to narrow down the possibilities.
3. **Elimination**: Based on the answers to the questions, players can start eliminating characters that do not fit the description until they are confident enough to make a guess.
4. **Winning**: The first player to correctly guess their opponent's character wins the game!


## Table of Contents
1. [Introduction](#introduction)
2. [Technologies](#technologies)
3. [High-level Components](#HLC)
4. [Launch & Deployment](#LD)
5. [Interface User Flow](#IUF)
6. [Roadmap](#roadmap)
7. [Authors and Acknowledgment](#AAA)
8. [Licencse](#licence)
9. [Further Material](#FM)
   1. [UML Diagram](#uml)
   2. [Component Diagram](#component)
   3. [Activity Diagram](#activity)
   4. [Figma Mockups](#figma)

## 2. Technologies
For this webapplication we used ReactJS in the frontend and Java Spring in the backend. More specifically we used the following technologies
- HTML/CSS/JavaScript/TypeScript: ...
- Java: ...
- Websockets: ...

## 3. High-level Components
some text

## 4. Launch & Deployment
some text

## 5. Interface User Flow
some text

## 6. Roadmap
some text

## 7. Authos and Acknowledgment
some text

## 8. Licencse
some text

## 9. Appendix
- [UML Diagram](https://lucid.app/lucidchart/bdc43c7c-3a02-4163-9724-150a430a899a/edit?invitationId=inv_7c71d23b-ad34-4ca4-b878-67235064b5df&page=0_0#)
- [Component Diagram](https://lucid.app/lucidchart/49acbc96-3e66-4064-99c4-4174bcf3b833/edit?invitationId=inv_56df17db-d1b8-4ae0-b4c8-e27462ec2213&page=0_0#)
- [Activity Diagram](https://lucid.app/lucidchart/e5d280ab-f80c-4e6c-8c0b-7544ba9b8936/edit?invitationId=inv_10376661-1660-47e2-b32b-fbf7d82989de&page=0_0#)
- [Figma Mockups](https://www.figma.com/file/b6orEYoJfIJ8n25mSPVsY7/Untitled?type=design&node-id=0-1&mode=design&t=ch054pYdPzTn8U1s-0)



# SoPra RESTful Service Template FS24
## Getting started with Spring Boot
-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: https://spring.io/guides/tutorials/rest/

## Setup this Template with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
If you consider to use IntelliJ as your IDE of choice, you can make use of your free educational license [here](https://www.jetbrains.com/community/education/#students).
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs24` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

## Debugging
If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
Have a look here: https://www.baeldung.com/spring-boot-testing

## DockerHub
Link to server: https://hub.docker.com/repository/docker/sopragroup34/server/general
