# testcontainers with Docker Compose sandbox #

## How? ##

Linux host with cgroups v1 enabled, system-wide docker, user-wide docker-compose, portainer

```shell
mvn clean package
```

```shell
cd src/test/resources/a
docker-compose -f docker-compose-debug.yml up
```

## Troubleshooting ##

```shell
docker network prune -f
```

## License ##

Perl "Artistic License"
