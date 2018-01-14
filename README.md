# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact

### How to add a Module ###

* Create a new directory named the same as the module
* Add an include line to the settings.gradle file with the module name

### Updating the API Gateway SDK ###

* Generate the SKD using the following settings
> * Service Name: PennStateUnified
> * Java Package Name: edu.pennstate.api
> * Java Build System: gradle
* Add the following the the bottom of the build.gradle file in the generated SDK
```
apply plugin: 'maven'

configurations {
    deployerJars
}

dependencies {
    deployerJars 'org.springframework.build:aws-maven:5.0.0.RELEASE'
}

uploadArchives {
    repositories.mavenDeployer {
        configuration = configurations.deployerJars
        repository(url: "s3://repo.unifiedapi.psu.edu/snapshots") {
            authentication(userName: AWS_ACCESS_KEY, password: AWS_SECRET_KEY)
        }
    }
}
```
* Create a gradle.properties file with your AWS credentials
(Found in \\Users\\\<username\>\\.aws\\credentials)
```
AWS_ACCESS_KEY=<ACESS_KEY>
AWS_SECRET_KEY=<SECRET_KEY>
```