# Ember Client

A client-side Fabric utility mod for Minecraft 26.2 with a dark red, category-column ClickGUI.
It only contains quality-of-life modules that use vanilla options and information you already
have, so it is safe to use on any server.

## Layout

Flat: every `.java` file sits in the repo root (packages are still declared inside the files, e.g.
`com.ember.client.module`). `build.gradle` points Gradle at the root folder, so you can upload the files
straight to GitHub without creating folders. Only `.github/workflows/build.yml` needs its folder.

## Get the jar (no local setup)

1. Create a GitHub repo and upload everything in this zip (including the `.github` folder).
2. Open the **Actions** tab. The "Build Ember Client" workflow runs on every push.
3. When it finishes, download the `ember-client-mod` artifact. Put the jar in your `mods` folder next to
   Fabric API 0.150.1+26.2.

To build locally instead, install JDK 25 and Gradle 9.3+, then run `gradle build`.

## Controls

| Key | Action |
|---|---|
| Right Shift | Open the menu |
| C (hold) | Zoom (when the Zoom module is on) |
| V | Send the selected chat macro (when Chat Macro is on) |

In the menu: left click toggles a module, right click expands its settings, drag a slider to change a number.
Settings are saved to `config/ember_client.json`.

## Modules

| Category | Modules |
|---|---|
| Render | Fullbright (vanilla max brightness), FOV Changer |
| HUD | FPS Counter, Coordinates, Ping Display, Clock, Armor HUD |
| Movement | Toggle Sprint, Toggle Sneak, Auto Walk |
| Utility | Zoom, Weather Notifier (local chat message only), Chat Macro (V sends one saved line from `config/ember_macros.txt`) |

## Adding a module

1. Add a new `.java` file in the root with `package com.ember.client.module.modules;`, extending `Module`.
2. Add settings with `add(new BoolSetting(...))` or `add(new NumberSetting(...))`.
3. Register it in `ModuleManager.java`.

## Notes on Minecraft 26.2 API names

Minecraft 26.x is unobfuscated and renamed a few things (`GuiGraphics` is now `GuiGraphicsExtractor`,
`Screen.render` is now `extractRenderState`, `ResourceLocation` is now `Identifier`). This project was
written against those names but has not been compiled against the 26.2 jars. If the Actions build reports a
missing method, send the error and it can be fixed.
