# Create Custom TeamCity Agent

### Running a TeamCity Agent
* docker-compose up -d
* docker-compose exec agent bash

### (Option 1) Install Google Cloud SDK 
```
$ curl -O https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-264.0.0-linux-x86_64.tar.gz
$ tar -zxf google-cloud-sdk-264.0.0-linux-x86_64.tar.gz
--- skip tar rename error, sometimes happen!? just remove and do it again ---
$ cd google-cloud-sdk && sh ./install.sh
$ cd ~
$ /google-cloud-sdk/bin/gcloud components update
$ /google-cloud-sdk/bin/gcloud components install kubectl
$ history -c
$ history -w
```

### (Option 2) Install Python 3 Pip
```
$ apt-get update
$ apt-get install -y python3-pip
$ apt-get clean
$ history -c
$ history -w
```

### (Option 3) Install Firefox
```
$ apt-get update
$ apt-get install -y firefox
$ apt-get clean
$ history -c
$ history -w
```

### Commit It and Push
* docker commit -m "customize teamcity agent" $(docker-compose ps -q agent) jianminhuang/teamcity-agent:$(cat .env | grep VERSION | sed "s/VERSION=//g")
* docker login -ujianminhuang
* docker push jianminhuang/teamcity-agent:$(cat .env | grep VERSION | sed "s/VERSION=//g")

### Debug
* Directory renamed before its status could be extracted, Sometimes, Doesn't matter ?
