# 1. Basic Java EE server

**Contents**

[2. To run this applcation](#2-to-run-this-applcation)
- [2.1. Run a local h2 database](#21-run-a-local-h2-database)
- [2.2. Create a war file for the project](#22-create-a-war-file-for-the-project)    
- [2.3. Add a new datasource to your wildfly project](#23-add-a-new-datasource-to-your-wildfly-project)
- [2.4. Run the project](#24-run-the-project)

[3. What is this application doing?](#3-what-is-this-application-doing)    
- [3.1. AccountDB](#31-accountdb)        
    - [3.1.1. @PersistenceContext](#311-persistencecontext)        
    - [3.1.2. @Transactional](#312-transactional)

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
A datasource is as you might have guessed, a source for where the data comes from!

We need to let our wildfly project know about the h2database.jar we are currently running.


In your wildfly folder, go to\
standalone -> configurations -> standalone.xml

Inside this xml you should find the following:
```html
<datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password>sa</password>
    </security>
</datasource>
```

We don't want to use the ExampleDatasource, we want to add our own. Why? because this one uses in memory - it will lose all data when we turn the database off. We want a datasource which willl be connecting to our locally hosted h2 database jar which can persist the information.

So add the following piece of code underneath. the ExampleDS datasource (don't delete it). 
You will need to configure the following datasource a little to point to the correct location (this one points to the test database), as well as any username and password for the database you have running.
```html
<datasource jta="true" jndi-name="java:jboss/datasources/MyApplicationDS" pool-name="MyApplicationDS" enabled="true" use-ccm="true">
    <connection-url>jdbc:h2:tcp://localhost/~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url> 
    <driver-class>org.h2.Driver</driver-class>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password>sa</password>
    </security>
    <validation>
        <background-validation>false</background-validation>
    </validation> 
</datasource> 
```

**To this datasource our persistence.xml will now connect!**

## 2.4. Run the project
Make sure the h2database is runnning

Make sure the war file is in the deployments

Make sure the standalone.xml is saved and closed.

Run the standalone.bat if using a windows computer.

# 3. What is this application doing?

## 3.1. AccountDB

### 3.1.1. @PersistenceContext
[**Previously**](https://github.com/christophperrins/JPA-with-SE) we saw that in Java SE we could run the following to create an EntityManager and transaction using:
```Java
public EntityManagerFactory  managerFactory = Persistence.createEntityManagerFactory("myPU");
public EntityManager manager = managerFactory.createEntityManager(); 
public EntityTransaction transaction = manager.getTransaction();
```
But when we use injection, we don't really want to be creating our own instances, where possible we want them being injected in and managed for us by the CDI container.

So instead of creating our own EntityManagerFactory we can inject one in using the `@PersistenceUnit` annotation. After this we can create our own EntityManager. 

**BUT** this is going to take longer, each time creating an instance of a entitymanager, which again we want abstracting from us because we want the CDIContainer to manage it for us. So instead of injecting in a EntityManagerFactory, we can directly inject in a Entity manager with  `@PersistenceContext` and then we can tell it which persistenceunit to look at inside the persistence.xml

### 3.1.2. @Transactional
Again we don't want to be creating our own instances when they can be managed by the CDIContainer. 

So instead of beginning and committing ourselves, we can inject in a transaction and it will all be handled for us

