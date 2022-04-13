<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# indexing metrics collector Changelog

## [Unreleased]
### Added
- Rename to indexing stats collector
- Add more authentication options
  - [Access token authentication](https://github.com/breskeby/indexing-stats-collector/issues/3)
  - [API keys authentication](https://github.com/breskeby/indexing-stats-collector/issues/2)
- [Make elasticsearch index configurable](https://github.com/breskeby/indexing-stats-collector/issues/5)

## [0.0.1]
### Added
- Initial Release under elastic
- Rebranded from initial spike 
- Allow all authentication options
  - [Access token authentication](https://github.com/breskeby/indexing-stats-collector/issues/3)
  - [API keys authentication](https://github.com/breskeby/indexing-stats-collector/issues/2)
- [Make elasticsearch index configurable](https://github.com/breskeby/indexing-stats-collector/issues/5)
### Changed
- Do not use JDK 8 specific class javax.xml.bind.DatatypeConverter in user data anymizer
