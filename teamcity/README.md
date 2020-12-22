# teamcity
* Administration -> Server Administration -> Authenication -> Allow login as guest user

### Run Agent at Local
```
$ vi TeamCity/buildAgent/bin/agent.sh
>>> TEAMCITY_AGENT_MEM_OPTS_ACTUAL="-Xmx4096m -XX:ReservedCodeCacheSize=1024m"
```
```
$ vi TeamCity/buildAgent/conf/buildAgent.properties
```
```
$ sh agent.sh start
```
