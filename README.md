# unpoly-jetbrains

![Build](https://github.com/mawngo/unpoly-jetbrains/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/23200-unpoly-support)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/23200-unpoly-support)

## Plugin Description

<!-- Plugin description -->
This plugin adds support for [Unpoly](https://unpoly.com/) tags autocompletion.

<!-- Plugin description end -->

Currently, this plugin is unable to suggest autocompletion properly [if you configured Unpoly
to automatically handle all links and forms](https://unpoly.com/handling-everything).

The list of supported tags and rules can be
found [here](src/main/kotlin/com/github/sitdownrightnow2552/unpolyjetbrains/attribute/UnpolyAttributes.kt).

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  unpoly-jetbrains"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/mawngo/unpoly-jetbrains/releases/latest) and install it
  manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
