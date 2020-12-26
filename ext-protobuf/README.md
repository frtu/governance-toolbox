# Extension - Protobuf

## Overview

Working with governance toolbok and extend it with **protobuf** support.

Allow to generate a base project for 

- library-proto-meta
- Proto data project

### Libraries - Metadata

Allow .proto files to be annotated.

### Generate an Protobuf data module

Generate your Protobuf sub module to manipulate **sales-proto** record with :

```
mvn archetype:generate -DarchetypeCatalog=local \
-DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=proto-project-archetype -DarchetypeVersion=1.1.0-SNAPSHOT \
-DgroupId=com.github.frtu -DartifactId=sales-proto -Dversion=0.0.1-SNAPSHOT
```

When asked a **DatamodelClassName**, type **Sales** which will be the *Class name* of the generated object.

To finish to generate all the artifacts, run : 

```
mvn compile
```
