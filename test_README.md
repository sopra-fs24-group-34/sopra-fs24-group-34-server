## Introduction

Bringing the beloved board game "Guess Who?" to the digital realm, our web application offers a competitive and 
enjoyable character guessing game to play with a friend This is the repository of the front-end part of our 
implementation, you'll find the back-end part [here](https://github.com/sopra-fs24-group-34/sopra-fs24-group-34-client).

## Technologies

The back-end is written in Java and uses Spring Boot framework. We use JPA for persistence and the deployment is
handled by Heroku. Communication between the server and the client is done with a RESTful API and websockets. For the
websockets an additional messaging protocol called STOMP is used.

## High-level components



## Launch & Deployment

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

## Roadmap

- New game mode: Guess the lyrics
- Global RaveWAver leaderboard
- Submit Quota Extension request (takes about six weeks)

## Authors and Acknowledgment

SoPra Group 34 2024 consists of [Smail Aligajic](https://github.com/smailalijagic), 
[Dario Giuliani](https://github.com/DarioTheCoder), [Nedim Jukic](https://github.com/nedim-j), 
[Liam Kontopulos](https://github.com/LiamK21) and [Till Sollberger](https://github.com/Tillsollberger).

We want to thank Unsplash for providing a well documented and easy to use API and the creators of the original 
"Guess Who" board game for the inspiration. It has been a very educational and challenging semester, and we are very 
thankful for this experience.

## License

Apache-2.0 license