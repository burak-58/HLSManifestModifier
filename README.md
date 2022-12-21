# HLSManifestModifier
This is a Tomcat Filter to modify requested m3u8 in Ant Media Server as filtering the chunks between provided start and end date. You can use this filter for the VoD files serverd by AMS.  

# Build (optional)
- To build Plugin you should first clone and build [ant-media-server-parent](https://github.com/ant-media/ant-media-server-parent) project. You can build it with the following maven command:

  `mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true`

- After building the parent project you can build filter project with the same maven command:
  
  `mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true`

- copy plugin jar file

  `sudo cp target/HLSManifestModifier.jar /usr/local/antmedia/plugins`

- copy dependent m3u8 jar file

  `sudo cp target/lib/m3u8-parser-0.24.jar /usr/local/antmedia/plugins`

# Use ready jars
- Copy the HLSManifestModifier.jar and m3u8-parser-0.24.jar from out folder in this repo into /usr/local/antmedia/plugins

# Setup Server
- register the filter to Tomcat by adding the following lines into /usr/local/antmedia/webapps/{APP_NAME}/WEB-INF/web.xml

  ```
  <filter>
    <filter-name>HlsManifestModifierFilter</filter-name>
    <filter-class>io.antmedia.filter.HlsManifestModifierFilter</filter-class>
    <async-supported>true</async-supported>
  </filter>
  <filter-mapping>
    <filter-name>HlsManifestModifierFilter</filter-name>
    <url-pattern>/streams/*</url-pattern>
  </filter-mapping>
  ```

- restart server

  `sudo service antmedia restart`

# Usage
- request m3u8 by adding start and end datetime in unix timestamp like

  http://localhost:5080/LiveApp/streams/test.m3u8?start=1668454888&end=1668454999
  
**Note:** 
You should also make the following configurations in Ant Media Server configuration file in `/usr/local/antmedia/webapps/{APP NAME}/WEB-INF/red5-web.properties`:
- set `settings.hlsflags=+program_date_time` to add program date time in m3u8 file.
- set `settings.hlsListSize=0` to keep all ts files refernces in m3u8 file.
- set `settings.deleteHLSFilesOnEnded=false` tokeep all ts files in the disk after stream finishes.
