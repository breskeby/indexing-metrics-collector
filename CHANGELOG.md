<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# indexing metrics collector Changelog

## [Unreleased]
### Added
- Initial Release under elastic
- Rebranded from initial spike 
- Allow all authentication options
  - [Access token authentication](https://github.com/breskeby/indexing-stats-collector/issues/3)
  - [API keys authentication](https://github.com/breskeby/indexing-stats-collector/issues/2)
- [Make elasticsearch index configurable](https://github.com/breskeby/indexing-stats-collector/issues/5)
- add runtime data about 

### Changed
- Renamed default index name to `idea-metrics-indexing`
- Improve elasticsearch connection error handling
- Do not use JDK 8 specific classes in user data anymizer

