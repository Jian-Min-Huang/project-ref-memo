# :file_folder: v-mvn-repo ![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103) ![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)
Vincent's personal maven repository hosted on GitHub repository

# Upload
suppose you have another project (ex. v-util), run these maven command for deploying artifacts
```sh
$ mvn clean deploy -DaltDeploymentRepository=release-repo::default::file:~/GitHub/v-mvn-repo/releases
$ mvn clean deploy -DaltDeploymentRepository=snapshot-repo::default::file:~/GitHub/v-mvn-repo/snapshots
$ cd ~/GitHub/v-mvn-repo
$ git commit -m "message"
$ git push
```

# Import Artifact in Other Project
add following block in your pom.xml

* maven
```xml
<repositories>
	<repository>
		<id>release-repo</id>
		<url>https://github.com/Jian-Min-Huang/v-mvn-repo/raw/master/releases</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>${groupId}</groupId>
		<artifactId>${artifactId}</artifactId>
		<version>${version}</version>
	</dependency>
</dependencies>
```

* gradle
```groovy
repositories {
    maven { url "https://github.com/Jian-Min-Huang/v-mvn-repo/raw/master/releases" }
}

dependencies {
    compile group: '${group}', name: '${name}', version: '${version}'
}
```

# Author
Jian-Min Huang

# License
[MIT License][license-page]

Copyright (c) 2017 Jian-Min Huang

[:arrow_up: back to top][top-page]

[license-page]: <https://github.com/Jian-Min-Huang/v-mvn-repo/blob/develop/LICENSE>
[top-page]: <https://github.com/Jian-Min-Huang/v-mvn-repo#file_folder-v-mvn-repo-->