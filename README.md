# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Phase 2 Chess Server Diagram:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIcDcuj3ZfF5vD6L9sgwr5iWw63O+nxPF+SwfgC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatJvqMJpEuGFoctyvIGoKwowKK4qutKSaXvBpTTrO6COs6BL4SyhFgLG-pcY26DkUybpUaUADiFIwCJ5iEPEiRCmEYlzqGFHSWxRQcdO8ZyrhyYwZcaE8tmuaYIBIJwSUVyviOowLpOfSac246tn0BzFnZhTZD2MD9oOvROSBLledWU5Btx85RW2y6rt4fiBF4KDoHuB6+Mwx7pJkmCBRe+kOZU0gAKK7uV9Tlc0LQPqoT7dB5aB-mytnlC1Nlmf+JXgkh9g5ah2W+hhGLYcZGoCaSMDklSNJMSgklmhGhSWjRtp0do6mMaRYgseaek4ZxsXia1OFTTpgkcCg3CZCJAanXOy2UZGHLSLdFKGEpGAqQQakMV1F16R1aE5VZCB5j1nZ+aUPS+Ve-ldjkYClCFQ5LpwyXroEkK2ru0IwLJo6snlp6FeezD2desnVXV9ijs1T08X57U9Z1zOtbZbLHXNxOjKosL82oY1Ybxib8VdpJzQ9LUvbpa0cvJzA-QwqlJIDnPaVJrH2eChnaOLuGSzrpLILEwuCwb8jy6xityQpCjKkTJM7dbwDaytMPGaUDOjEZ8Gdh1lsQ2AGjc0diNXFMftqOMlT9LHACS0jxwAjL2ADMAAsTwnpkBoVhMXw6AgoANoXoF9F8scAHKjsXewwI0CPUB2elFajwUDkOMck-HFSJ6OKfp1nudTPn+p7SsJdlyAFfT8XTx1w31dNy3ZhY54KUbtgPhQNg3DwLqmQu6MKT5WeKM81HlS1A09OM8EnNDivoyt8cplAiWLWv6O9fv26t-Xqx1PR6mFrCOAJ8UDC1FliI2l1TbuhlkGR6dYzq20OvbZWikgzKRAOrN2WsDoRj1vKd2CC8JS3dGAzIED3azDfktT2r17bRjPoYBiTCYDnB4UGAOTpExf1TKUKBXo6GjlDkA1M3s25lB6H3UYI9ygZxzjAD+7dTidzRj3XoiiUDKJgKo7O6jN4rm3jjAIlhbpIWSDAAAUhAHkHDAil3LhTa+1M0zVEpHeFoscmboLnEOQ+wBrFQDgBAJCUBGHD2kBo3qHU+FBPQCEsu4TInRNiUo+J0jYLsUEQhAAVk4tAEDHE8lgSgNE41KEmxWr7CkstOaYNWuyB2Ks8G-QIf9DWGliHTVIQU-W-DDbA3qeGRpYB6GjPkNklAADmEkJkjg4WO1uG8IocsyOHFY4CL4jIQZpQ-BaAkaMNBwZtDzJDNs+2lJsCnMMGsrh-9Rw8N5FswZsjCm+1HPsoRAF2YVLKZItQ2BEgGDyZogplwFF9GTqnFRY9TGsw7pTHRoU4UItHmozG5i1ypQCF4MJXYvSwGANgQ+f0nwX3Jp3G+cjygVAqlVGqdVjBtWEcCOG0FgEMp+SAbgeBIFCqzNUzC8DxmHOoR6UVsJWkyUQGSxSztTnABgAAM28MMaVuh9D7S+Tsn5MBFTKkoVyy4Sq8BSIjl4nlnKtHovRr0PFmAgA 
