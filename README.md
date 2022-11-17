# HLSManifestModifier
This is a Tomcat Filter to modify requested m3u8 in Ant Media Server as filtering the chunks between provided start and end date. You can use this filter for the VoD files serverd by AMS.  

# Build
- To build Plugin you should first clone and build [ant-media-server-parent](https://github.com/ant-media/ant-media-server-parent) project. You can build it with the following maven command:

  `mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true`

- After building the parent project you can build filter project with the sam3 maven command:
  
  `mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true`

- copy plugin jar file

  `sudo cp target/HLSManifestModifier.jar /usr/local/antmedia/plugins`

- copy dependent m3u8 jar file

  `sudo cp ~/.m2/repository/io/lindstrom/m3u8-parser/0.24/m3u8-parser-0.24.jar /usr/local/antmedia/plugins`
  
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
  
**Note:** m3u8 files should contain program date time. To enable the in AMS, you should add the following line into the application configuration file `settings.hlsflags=+program_date_time` Please check [this](https://antmedia.io/javadoc/io/antmedia/AppSettings.html#hlsflags) for more information.
