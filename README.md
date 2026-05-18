# NanoGrid Game

A nonogram (picross) puzzle game built in Java.

## Modules

| Module | Description |
|---|---|
| `nanogrid-core` | Puzzle generation, game state, win-checking, file I/O |
| `nanogrid-ui` | Swing desktop UI |
| `nanogrid-app` | Console test harness |

## Building

Requires Java 8+ and [Apache Maven](https://maven.apache.org/).

```bash
mvn package
```

## Running

Launch the desktop UI:

```bash
java -jar nanogrid-ui/target/nanogrid-ui-1.0-SNAPSHOT.jar
```

Run the console test harness:

```bash
java -jar nanogrid-app/target/nanogrid-app-1.0-SNAPSHOT.jar
```
