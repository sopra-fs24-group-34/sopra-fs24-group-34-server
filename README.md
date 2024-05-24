# SoPra FS24 group 34 Server - Guess Who?

## Introduction
## Introduction
Does the person have brown eyes? No! Did you forget this game? "Guess Who" is back. We implemented the traditional tabletop game where users have to guess characters by asking yes or no questions.
The rules are simple:
1. **Setup**:  Each player selects a character card without revealing it to their opponent. This card represents the character the opponent must guess.
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

## Technologies
- Java and Spring Boot Framework: used Java as programming language and used the Spring Boot Framework
- JPA: used for database persistence
- Heroku: handling deployment
- REST: used for communication between the client and server
- Websockets: used for communication between the client and server using the STOMP messaging protocol

## High-level Components
some text

## Launch & Deployment
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

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

## Interface User Flow
[Have a detailed look of the interface user flow here](https://github.com/sopra-fs24-group-34/sopra-fs24-group-34-client/tree/dev)


## Roadmap
### 3D animations
- Create 3D animations for folding respectively unfolding the characters during the game.

### Individual profile picture
- Allow users to upload individual profile pictures.

### Extending the game
- Allow users to select what type of category the images in game should be.
- For example, a user could select "cats" as category and then play "Guess Who?" with cats instead of people.

## Authors and Acknowledgment
- [Smail Alijagic](https://www.github.com/smailalijagic)
- [Dario Giuliani](https://github.com/DarioTheCoder)
- [Nedim Jukic](https://github.com/nedim-j)
- [Liam Kontopulos](https://github.com/LiamK21)
- [Till Sollberger](https://github.com/Tillsollberger)

We want to express our sincere gratitude to [Marco Leder](https://www.github.com/marcoleder) for his outstanding expertise and support throughout the 
development of our project.


## Licencse
[Apache-2.0 license](https://github.com/sopra-fs24-group-34/sopra-fs24-group-34-server/blob/main/LICENSE)

## Appendix
- [UML Diagram](https://lucid.app/lucidchart/bdc43c7c-3a02-4163-9724-150a430a899a/edit?invitationId=inv_7c71d23b-ad34-4ca4-b878-67235064b5df&page=0_0#)
- [Component Diagram](https://lucid.app/lucidchart/49acbc96-3e66-4064-99c4-4174bcf3b833/edit?invitationId=inv_56df17db-d1b8-4ae0-b4c8-e27462ec2213&page=0_0#)
- [Activity Diagram](https://lucid.app/lucidchart/e5d280ab-f80c-4e6c-8c0b-7544ba9b8936/edit?invitationId=inv_10376661-1660-47e2-b32b-fbf7d82989de&page=0_0#)
- [Figma Mockups](https://www.figma.com/file/b6orEYoJfIJ8n25mSPVsY7/Untitled?type=design&node-id=0-1&mode=design&t=ch054pYdPzTn8U1s-0)
