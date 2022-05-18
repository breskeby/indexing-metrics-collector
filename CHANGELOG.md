<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Idea Indexing Metrics Collector Changelog

## [Unreleased]

## [0.9.2]
### Changed
- Add support for elasticsearch 7.x

## [0.9.1]
### Added
- [Check connection details in preference page](https://github.com/elastic/idea-indexing-metrics-collector/issues/5) 
- [Option to allow plain http to connect to elasticsearch](https://github.com/breskeby/indexing-stats-collector/issues/12)

## [0.9.0]
### Added
- Initial Release under elastic
- Rebranded from initial spike 
- Allow all authentication options
  - [Access token authentication](https://github.com/breskeby/indexing-stats-collector/issues/3)
  - [API keys authentication](https://github.com/breskeby/indexing-stats-collector/issues/2)
- [Make elasticsearch index configurable](https://github.com/breskeby/indexing-stats-collector/issues/5)
- Add runtime data



### Changed
- Renamed default index name to `idea-metrics-indexing`
- Improve elasticsearch connection error handling
- Do not use JDK 8 specific classes in user data anymizer