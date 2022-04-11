# indexing-stats-collector

[//]: # ()
[//]: # (![Build]&#40;https://github.com/breskeby/idea-index-tracker/workflows/Build/badge.svg&#41;)

[//]: # ([![Version]&#40;https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg&#41;]&#40;https://plugins.jetbrains.com/plugin/PLUGIN_ID&#41;)

[//]: # ([![Downloads]&#40;https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg&#41;]&#40;https://plugins.jetbrains.com/plugin/PLUGIN_ID&#41;)

[//]: # (## ToDo list)

[//]: # (- [x] Create a new [IntelliJ Platform Plugin Template][template] project.)

[//]: # (- [x] Get familiar with the [template documentation][template].)

[//]: # (- [x] Verify the [pluginGroup]&#40;/gradle.properties&#41;, [plugin ID]&#40;/src/main/resources/META-INF/plugin.xml&#41; and [sources package]&#40;/src/main/kotlin&#41;.)

[//]: # (- [x] Review the [Legal Agreements]&#40;https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html&#41;.)

[//]: # (- [ ] [Publish a plugin manually]&#40;https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate&#41; for the first time.)

[//]: # (- [ ] Set the Plugin ID in the above README badges.)

[//]: # (- [ ] Set the [Deployment Token]&#40;https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html&#41;.)

[//]: # (- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.)

<!-- Plugin description -->
Allows gathering project scanning & indexing statistics for further analysis by ingesting them into an elasticsearch cluster.

* [Project Home](https://github.com/breskeby/indexing-stats-collector)
* [Issue Tracker](https://github.com/breskeby/indexing-stats-collector/issues)

<!-- Plugin description end -->

This idea plugin gives developer teams the chance to analyse how much time IDEA users spent on indexing their projects.
The plugin captures indexing events occured in IDEA and pushes them into an elasticsearch cluster for further analyzise. 

The captured data per indexing event includes

- total indexing time
- indexing reason
- duration of file scanning
- indexing duration
- update start
- update end
- was full indexing (true / false)
- was indexing interrupted (true / false)
- environment
  - user name (can be optionally anonymized)
  - host name (can be optionally anonymized)
  - os name
  - os arch
  - os version

The elasticsearch mapping template can be found here: [elasticsearch index mapping](https://github.com/breskeby/indexing-stats-collector/blob/main/src/main/resources/idea-indexing-mapping.json)

The idea for this came up when I heard first time about [shared project index](https://www.jetbrains.com/help/idea/shared-indexes.html#project-shared-indexes) For idea.
If you want to know the impact of such a plugin you need to have hard numbers first. In a distributed team gathering these numbers are tricky so the idea
for this plugin was born.

## Installation

[//]: # (- Using IDE built-in plugin system:)

[//]: # (  )
[//]: # (  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "idea-index-tracker"</kbd> >)

[//]: # (  <kbd>Install Plugin</kbd>)

- From plugin repository:

  - In <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Manage plugin repositories...</kbd> 
  add [https://raw.githubusercontent.com/breskeby/indexing-stats-collector/main/updatePlugins-213.xml](https://raw.githubusercontent.com/breskeby/indexing-stats-collector/main/updatePlugins-213.xml)
  - Install <b>indexing-stats-collector</b> plugin

- Manually:

  Download the [latest release](https://github.com/breskeby/indexing-stats-collector/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
