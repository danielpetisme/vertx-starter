# Vertx.x Starter


![Travis (.org)](https://img.shields.io/travis/vert-x3/vertx-starter.svg)
![GitHub](https://img.shields.io/github/license/vert-x3/vertx-starter.svg)

**Disclaimer**: This is a *WIP* project. Any comment, test or help is welcome!

[Vert.x Starter](http://start.vertx.io) is an open-source web application for creating [Vert.x](https://vertx.io/) applications. 

## Quickstart

Simply click on "Generate Project" on the web interface to download a project archive.

It your a CLI adept, you can use any http client (curl, [httpie](https://httpie.org/)) to invoke the API.

`$ curl -X GET http://start.vertx.io/starter.zip -d groupId=com.acme -d language=java -d vertxVersion=3.5.3 -o starter.zip`

## API

## Generating a Vert.x application

`http://start.vertx.io/starter.{archiveFormat}`

*Note*: `{archiveFromat}` can be `zip`, `tgz`, `tar.gz`, etc. if the project generator can handle the format, it will use the appropriate compression tool.  

You can provide the following query parameters to customize the project
* `type`: The type of project (`core`, `openapi`, etc.).
* Basic information for the generated project `groupId`, `artifactId`
* `language`: `java` or `kotlin`
* `buildTool`: `maven` or `gradle` build tool
* `vertxVersion`: the Vert.x version
* `vertxDependencies`: a comma separated list of artifactIds  of the vert.x modules

Full example:
```
curl -X GET \
  'http://start.vertx.io/starter.zip?artifactId=starter&buildTool=maven&groupId=io.vertx&language=java&type=core&vertxDependencies=&vertxVersion=3.5.3' \
  -o starter.zip
```

The HTTPie equivalent:
```
$ http http://start.vertx.io/starter.zip \
type==core \
groupId==io.vertx \
artitfactId==starter \
language==java \
buildTool==maven \
vertxVersion==3.5.3 \
vertxDependencies==vertx-web,vertx-web-client \
-o starter.zip
```
## Vert.x Starter metadata

The vert.x starter metadata lists all the capabilities proposed by the API. The metadata is used to build the Web UI is exposed to ease the creation of third-party clients (IDE integration, CLI, etc).

`http://start.vertx.io/metadata`


## Running your own starter

## Build from sources

For now, the vertx-starter project is not available on Maven-Central, so you need to build it from source.

In order to build it, you will need Java 1.8.

### Building fat jar

`$ ./gradlew shadowJar`

### Running the app locally

`$ ./gradlew vertxRun`

### Configuration

Vert.x starter relies on the [`vertx-boot`](https://github.com/jponge/vertx-boot) launcher.
The application is configured by [`src/main/resources/application.conf`](./src/main/resources/application.conf).
Please see the according documentation to know how to override the configuration. 

### Vert.x Project Generator
The project generation is delegated to the [vertx-project-generator](https://github.com/vert-x3/vertx-project-generator) project.
You should clone the project and configure the starter to have a complete stack locally.
 
## Docker

**WIP**

## License

Vert.x Starter is Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
