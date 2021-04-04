# 🎁 v-ms [![Build Status](https://travis-ci.org/Jian-Min-Huang/v-ms.svg?branch=develop)](https://travis-ci.org/Jian-Min-Huang/v-ms)
Vincent's Personal Message Service

# common
> v-ms (v message service) contains two type of message services, v-queue and v-topic .
> this tools contains following feature :

* v1.1.0 management of vms
* v1.2.0 communication layer management

# v-queue
> This queue is base on DelayQueue and appending following features : 

* v0.1.0 element enqueue hook
* v0.2.0 element enqueue filter
* v0.1.0 element cut in line
* v0.1.0 queue content logging
* v0.1.0 element dequeue hook
* v0.1.0 element timeout hook
* v0.2.0 element timeout checker suspending and resuming
* v0.1.0 queue multi-consumers
* v1.1.0 local process element take out
* v1.2.0 cross process element take out
* v0.1.0 queue consumers suspending and resuming
* v0.3.0 cross process communication (v1.2.0 extract to vms manager)
* v?.?,? queue persisting
* v0.5.0 queue replication
* v0.1.0 queue monitoring and controlling

# v-topic
> This tool is base on v-queue and appending following features : 

* v1.0.0 local process topic notify
* v1.2.0 cross process topic notify

# Author
Jian-Min Huang

# License
[MIT License][license-page]

Copyright (c) 2017 Jian-Min Huang

[license-page]: <https://github.com/Jian-Min-Huang/v-ms/blob/develop/LICENSE>