manual ivy publish samples:
curl -XPOST -u admin:admin123 -T pivy/build/libs/pivy-1.1-SNAPSHOT.jar http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.jar
curl -XPOST -u admin:admin123 -T a.txt http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/a.txt
