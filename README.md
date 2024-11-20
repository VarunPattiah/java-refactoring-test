Java Refactoring Test Project
=============================

**Please, before starting this test, read through all of the instructions carefully!**

Introduction
------------

This is a test project used by SAP Team Atlas software hiring process to test your knowledge of Java/Spring best practices and refactoring.

The idea of this exercise is to evaluate your ability to identify poor coding practices and improve the code through the use of best practices.

The main project is a very basic user management application. We are not looking to add any supplementary features, instead we are verifying the following items:

* Your knowledge of REST
* Your knowledge of Gradle
* Your knowledge of Spring / SpringBoot
* Your ability to identify and refactor poor Java code
* Your ability to identify and fix bugs
* Your ability to apply proven design principles
* Your ability to write useful and effective tests

Feel free to modify whatever you want! :)

Prerequisites
-------------

* You must have a Github account. If you don't have one, please create one via the [Github website](http://github.com/).
* This repo uses Git for source control management (SCM). If you don't already have the git utility already installed on your machine you will need to install it. To do so, check out the [git downloads page](http://git-scm.com/downloads).
* To build this project you must use Gradle. If you do not have gradle already installed on your machine you will need to install it. To do so, check out the [Gradle downloads](https://gradle.org/install/).

Instructions
------------

1. Clone this repo from Github to your local machine (or fork the repository if you prefer).
2. At the project root directory, run the following command from the command-line:
   `$ ./gradlew build`
3. The tests run will fail, as some of the tests are not implemented correctly. Part of the task is to fix these tests.
4. Now perform the refactoring you deem necessary by using your knowledge of Java/Spring best practices. Remember that this includes both code and tests. Also, please feel free to innovate!
5. Please make sure that your code compiles and that all tests are green when you are done.
6. When you are finished please commit your code on your local machine and then [create a patch using git](http://git-scm.com/docs/git-format-patch) (if you have not created a fork). 
7. The final step is to send an email to your contact at SAP to inform this person that you have completed the test. Please make sure to attach a copy of the patch containing your changes (if one was created), or if you have created a fork, please supply the URL to your forked repository.!

Business Requirements
---------------------

* The user's email is a unique identifier and should be handled accordingly.
* A user should have at least one role.

Tips
----

* Unit tests != integration tests
* Spring dependency is provided, feel free to use it
* Don't be afraid to import additional dependencies if you think you need them
* Remember that you will have to handle concurrent requests
* Your final architecture should be portable, extensible and easily maintainable

Bonus
-----

If this exercise is too easy, additional points will be given for the following: - 

* Utilising Docker / Docker Compose to run a PostgreSQL Database as the persistence layer
* Implement the DB access using Hibernate, or other such ORM
* Implement DB initialisation using either Flyway or Liquibase

Good luck!


Local Development
-----------------

1. clone the repository
2. generate the jar file via gradle CMD: ./gradlew build
3. run docker compose from root directory 
   docker-compose -d up

This will create a docker image docker-postgres-java-refactoring:latest 
And 2 containers one for app and another for postres

you can verify they are running via cnd docker ps

4. Now go to http://localhost:8080/v1/users