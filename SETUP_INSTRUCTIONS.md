# SkillSwapHub Setup Instructions

## Fix for "package javax.servlet does not exist" Error

This error occurs because the servlet API dependencies are missing in your project. Follow these steps to fix it:

### Option 1: Using Eclipse IDE

1. Right-click on your project in the Project Explorer
2. Select "Properties"
3. Go to "Java Build Path" > "Libraries" tab
4. Click "Add External JARs..."
5. Navigate to your Tomcat installation directory (e.g., `C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib`)
6. Add the following JAR files:
   - `servlet-api.jar`
   - `jsp-api.jar`
   - `el-api.jar`
   - `jstl-api.jar`
7. Click "Apply and Close"

### Option 2: Using IntelliJ IDEA

1. Go to "File" > "Project Structure..."
2. Select "Libraries" in the left panel
3. Click the "+" button and select "Java"
4. Navigate to your Tomcat installation directory (e.g., `C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib`)
5. Add the following JAR files:
   - `servlet-api.jar`
   - `jsp-api.jar`
   - `el-api.jar`
   - `jstl-api.jar`
6. Click "Apply" and "OK"

### Option 3: Using Maven

1. Create a `pom.xml` file in your project root with the following content:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.skillswaphub</groupId>
    <artifactId>skillswaphub</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Servlet API -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSP API -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!-- MySQL Connector -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
    </dependencies>
</project>
```

2. Right-click on the `pom.xml` file and select "Add as Maven Project"
3. Maven will download the dependencies automatically

### Option 4: Manual JAR Download

1. Create a `lib` directory in your project's `WEB-INF` directory:
   ```
   mkdir -p src/main/webapp/WEB-INF/lib
   ```

2. Download the following JAR files and place them in the `lib` directory:
   - [javax.servlet-api-4.0.1.jar](https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar)
   - [javax.servlet.jsp-api-2.3.3.jar](https://repo1.maven.org/maven2/javax/servlet/jsp/javax.servlet.jsp-api/2.3.3/javax.servlet.jsp-api-2.3.3.jar)
   - [jstl-1.2.jar](https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar)
   - [mysql-connector-java-8.0.28.jar](https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar)

3. Add these JARs to your project's classpath

## Additional Setup

1. Make sure you have a Java Development Kit (JDK) installed (version 8 or higher)
2. Make sure you have a servlet container installed (e.g., Apache Tomcat)
3. Configure your IDE to use the installed JDK and servlet container

## Database Setup

1. Make sure you have MySQL installed and running
2. Create a database named `skillswaphub`
3. Run the SQL scripts in `src/main/resources/db/init.sql` to create the necessary tables
4. Update the database connection parameters in `src/main/java/com/skillswaphub/dao/DBConnection.java` if needed
