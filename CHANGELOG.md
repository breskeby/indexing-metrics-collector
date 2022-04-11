<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# indexing stats-collector Changelog

## [Unreleased]

## [1.2.2]
### Fixed
- Fix providing shipped index mapping on non-existing renamed indexes

## [1.2.1]
### Changed
- Do not use JDK 8 specific class javax.xml.bind.DatatypeConverter in user data anymizer

## [1.2.0]
### Added
- Publish indexing collector plugin version

## [1.1.1]
### Added
- Fix plugin update handling in idea by fixing mismatching plugin id

## [1.1.0]
### Added
- Collect more details about the indexing
  - was indexing interupted
  - was full indexing
  - files scanning duration
  - indexing duration

### Changed
- Make notification messages more intuitive

## [1.0.0]
### Added
- Rename to indexing stats collector
- Add more authentication options
  - [Access token authentication](https://github.com/breskeby/indexing-stats-collector/issues/3)
  - [API keys authentication](https://github.com/breskeby/indexing-stats-collector/issues/2)
- [Make elasticsearch index configurable](https://github.com/breskeby/indexing-stats-collector/issues/5)

## [0.0.1]
### Added
- Initial Release