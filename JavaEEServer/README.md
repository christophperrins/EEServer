# 1. Basic Java EE server

**Contents**
<!-- TOC -->autoauto- [1. Basic Java EE server](#1-basic-java-ee-server)auto- [2. To run this applcation](#2-to-run-this-applcation)auto    - [2.1. Run a local h2 database](#21-run-a-local-h2-database)auto    - [2.2. Create a war file for the project](#22-create-a-war-file-for-the-project)auto    - [2.3. Add a new datasource to your wildfly project](#23-add-a-new-datasource-to-your-wildfly-project)auto    - [2.4. Run the project](#24-run-the-project)autoauto<!-- /TOC -->

# 2. To run this applcation

## 2.1. Run a local h2 database
To run a h2 database, download the jar file [here](http://repo2.maven.org/maven2/com/h2database/h2/1.4.199/h2-1.4.199.jar)

Open up a terminal in the same directory as the jar file and run the command

`java -jar h2-1.4.199.jar`

The server should now be viewable at [localhost:8082](http://localhost:8082)

(the username should be 'sa' and the password will either be empty '' or 'sa')

## 2.2. Create a war file for the project
Using maven, run `mvn install` on the project and take the created .war file and place it in the deployments of your wildfly server.

**(DONT RUN WILDFLY JUST YET!)**

## 2.3. Add a new datasource to your wildfly project
In your wildfly folder, go to\
standalone -> configurations -> standalone.xml

Inside this xml you should find the following:
> < datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" > use-java-context="true">\
 &nbsp; &nbsp; < connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</ connection-url>\
 &nbsp; &nbsp; < driver>h2</ driver>\
 &nbsp; &nbsp; < security>\
 &nbsp; &nbsp;  &nbsp; &nbsp; < user-name>sa</ user-name>\
 &nbsp; &nbsp;  &nbsp; &nbsp; < password>sa</ password>\
 &nbsp; &nbsp;  </ security>\
                </ datasource>


We don't want to use the ExampleDatasource, we want to add our own. Why? because this one uses in memory. The one we are creating will connect to our locally hosted h2 database jar.

So add the following piece of code underneath
> < datasource jta="true" jndi-name="java:jboss/datasources/MyApplicationDS" pool-name="MyApplicationDS" enabled="true" use-ccm="true"> \
&nbsp; &nbsp; < connection-url>jdbc:h2:tcp://localhost/~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</ connection-url> \
&nbsp; &nbsp; < driver-class>org.h2.Driver</ driver-class>\
&nbsp; &nbsp;  < driver>h2</ driver>\
&nbsp; &nbsp; < security> \
&nbsp; &nbsp; &nbsp; &nbsp; < user-name>sa</ user-name> \
&nbsp; &nbsp; &nbsp; &nbsp;  < password>sa</ password> \
&nbsp; &nbsp; </ security> \
&nbsp; &nbsp; < validation> \
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; < background-validation>false</ background-validation> \
&nbsp; &nbsp; </ validation> \
&nbsp; &nbsp; </ datasource> 


**To this datasource our persistence.xml will now connect!**

## 2.4. Run the project
Make sure the h2database is runnning

Make sure the war file is in the deployments

Make sure the standalone.xml is saved and closed.

Run the standalone.bat if using a windows computer.

