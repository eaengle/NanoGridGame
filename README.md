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
- New saves use versioned JSON when the filename ends in `.json`.
- Existing XML saves still load and can still be written by using a `.xml` filename.
- The Swing UI renders the puzzle board through a custom painted board view.
- The desktop shell includes a toolbar, timer, move counter, keyboard shortcuts, and fill/mark/erase/cycle modes.
- Puzzle generation supports difficulty levels, optional reproducible seeds, and optional symmetry.
- The UI supports undo and redo for cell changes.

## Core model direction

The core now has explicit domain types that can gradually replace raw `char[][]`
usage in the UI and persistence layers:

- `CellState` represents empty, filled, and marked player cells.
- `BoardCoordinate` identifies a board position.
- `Puzzle` is an immutable snapshot of the solution and its clues.
- `PlayerGrid` owns mutable player progress and keeps row/column views synchronized.
- `GameSession` combines a puzzle with player progress and game-rule operations.
