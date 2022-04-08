<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# indexing stats-collector Changelog

## [Unreleased]

## [1.1.1]
Patch release fixing the plugin id handling addressing problems when updating the plugin within idea

## [1.1.1] 2022-04-08
### Added
- Fix plugin update handling in idea by fixing mismatching plugin id

## [1.1.0] 2022-04-08
### Added
- Collect more details about the indexing
  - was indexing interupted
  - was full indexing
  - files scanning duration
  - indexing duration

### Changed
- Make notification messages more intuitive

## [1.0.0] 2022-04-07
### Added
- Rename to indexing stats collector
- Add more authentication options
  - [Access token authentication](https://github.com/breskeby/indexing-stats-collector/issues/3)
  - [API keys authentication](https://github.com/breskeby/indexing-stats-collector/issues/2)
- [Make elasticsearch index configurable](https://github.com/breskeby/indexing-stats-collector/issues/5)

## [0.0.1] 2022-04-05
### Added
- Initial Release