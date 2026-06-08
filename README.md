# Jixiao Adventure (Super Mario Tutorial)

Java step-by-step tutorial project for building a Super Mario-style game.

## Project layout

```
Starfall_adventure/
├── SuperMario/          # Complete game (lesson 23) — open this in IntelliJ to run
├── lessons/             # 23 tutorial steps (01–23), each with code for that stage
├── assets/
│   ├── images/        # Shared sprites (used by all lessons via links)
│   └── music/         # Background music
├── lib/
│   └── jlayer-1.0.1.jar
└── README.md
```

## How to run

1. Open the project root in IntelliJ IDEA.
2. Use the **SuperMario** module.
3. Run `com.sxt.MyFrame` (main class).
4. Use the main menu: **START**, **SETTINGS** (music on/off), **TUTORIALS** (controls & tips).
5. In-game controls: arrow keys to move/jump, **Space** to shoot fireballs.

## Tutorial lessons

Each folder under `lessons/` is one chapter. Open `lesson-guide.txt` in a lesson folder for the video link.

Work through lessons in order (`01-create-window` → `23-fireball-shooting`), or run the finished game from `SuperMario/`.

## Notes

- Image and music files live once in `assets/` and are linked into each lesson’s `SuperMario/src/` to avoid duplication.
- Java classes use English names (e.g. `Fireball`, `PowerUp`) instead of pinyin (`HuoQiu`, `DaoJu`).
