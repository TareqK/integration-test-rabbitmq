# integration-test-rabbitmq
![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.kisoft/integration-test-rabbitmq/badge.png)

This Maven plugin allows you to start and stop a RabbitMQ Docker instance as part
of your build. This is especially useful if you want to do integration testing, 
instead of mocking rabbitmq

## Usage

Add this plugin definition to your pom.xml
```xml
<plugin>
    <groupId>me.kisoft</groupId>
    <artifactId>integration-test-rabbitmq</artifactId>
    <version>${LATEST_VERSION_NUMBER}</version>
    <executions>
        <execution>
            <id>start-rabbitmq</id>
            <goals>
                <goal>start-rabbitmq</goal>
            </goals>
        </execution>
        <execution>
            <id>stop-rabbitmq</id>
            <goals>
                <goal>stop-rabbitmq</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Options

| Option | Description                      | Default Value         |
|--------|----------------------------------|-----------------------|
| port   | The port to bind to              |  5672                 |
| name   | The name of the container        | Random String         |
| image  | The RabbitMQ image to use        | rabbitmq:3-management |

Adding options

```xml
<plugin>
    <groupId>me.kisoft</groupId>
    <artifactId>integration-test-rabbitmq</artifactId>
    <version>${LATEST_VERSION_NUMBER}</version>
    <configuration>
        <port>3321</port>
        <name>jon</name>
        <image>rabbitmq:3</image>
    </configuration>
    <executions>
        <execution>
            <id>start-rabbitmq</id>
            <goals>
                <goal>start-rabbitmq</goal>
            </goals>
        </execution>
        <execution>
            <id>stop-rabbitmq</id>
            <goals>
                <goal>stop-rabbitmq</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
This will start RabbitMQ bound on port ```3321```, with the container name ```jon```
and using the image ```rabbitmq:3```

### Caveats

1. You need Docker installed on your pc - if you are using windows, im not sure how RabbitMQ docker images work there
2. If a container was was started, but the build crashes for some reason, its not cleaned up, you will 
need to cleanup manually
3. You need to always have both the start and stop executions, otherwise the container never stops
4. You can only have one instance/build. This is because of a shared state object