Published pivy-1.1-SNAPSHOT.jar (mycompany:pivy:1.1-SNAPSHOT) to
    http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.jar
Published ivy-1.1-SNAPSHOT.xml (mycompany:pivy:1.1-SNAPSHOT) to
    http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/ivy-1.1-SNAPSHOT.xml
Published pivy-1.1-SNAPSHOT.module (mycompany:pivy:1.1-SNAPSHOT) to
    http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.module

curl -XPOST -u admin:admin123 -T pivy/build/libs/pivy-1.1-SNAPSHOT.jar http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.jar
