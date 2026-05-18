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

## Current behavior baseline

- Generated puzzles store the solution separately from the player's progress.
- Left-click cycles a cell through filled, marked, and clear states.
- Right-click and drag highlights a straight row or column span and shows its length.
- `Check` reports filled cells that do not match the solution.
- `Peek` briefly reveals the solution, then restores the player's progress.
- `Show` reveals the solution.
- `Save Game...` writes the puzzle and current player progress.
- `Save Puzzle...` writes the puzzle with empty player progress.
- `Load Game...` restores both the puzzle and player progress.
- `Load Puzzle...` loads the puzzle and starts with empty player progress.
