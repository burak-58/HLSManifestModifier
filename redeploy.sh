#!/bin/sh
AMS_DIR=~/softwares/ant-media-server
mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true
OUT=$?

if [ $OUT -ne 0 ]; then
    exit $OUT
fi

rm -r $AMS_DIR/plugins/HLSManifestModifier*
cp target/HLSManifestModifier.jar $AMS_DIR/plugins/

OUT=$?

if [ $OUT -ne 0 ]; then
    exit $OUT
fi
#./start-debug.sh
