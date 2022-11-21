# Start Jenkins with docker

Using github, jenkins.

app: spring-boot

# CI/CD strategy

need: allow ssh to jenkins-server and service-server, write shell file in service-server for run DockerFile

github push -> jenkins-server -> build image -> push to private image server -> publish source file over ssh -> run shell file to run docker app   
-> remove image in service-server for prevent duplicate image
