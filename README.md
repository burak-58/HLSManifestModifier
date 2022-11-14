# HLSManifestModifier
Filter to modify requested m3u8 in Ant Media Server

# Build
- build plugin

  `mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true`

- copy plugin jar file

  `sudo cp target/HLSManifestModifier.jar /usr/local/antmedia/plugins`

- copy dependent m3u8 jar file

  `sudo cp ~/.m2/repository/io/lindstrom/m3u8-parser/0.24/m3u8-parser-0.24.jar /usr/local/antmedia/plugins`

- restart server

  `sudo service antmedia restart`

# Usage
- request m3u8 by adding start and end datetime in unix timestamp like

  http://localhost:5080/LiveApp/streams/test.m3u8?start=1668454888&end=1668454999
