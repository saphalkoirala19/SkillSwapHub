$libDir = "src\main\webapp\WEB-INF\lib"

# Create lib directory if it doesn't exist
if (!(Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir -Force
}

# Download servlet-api
$servletApiUrl = "https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar"
$servletApiPath = "$libDir\javax.servlet-api-4.0.1.jar"
Invoke-WebRequest -Uri $servletApiUrl -OutFile $servletApiPath

# Download jstl
$jstlUrl = "https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar"
$jstlPath = "$libDir\jstl-1.2.jar"
Invoke-WebRequest -Uri $jstlUrl -OutFile $jstlPath

# Download mysql-connector-java
$mysqlUrl = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar"
$mysqlPath = "$libDir\mysql-connector-java-8.0.28.jar"
Invoke-WebRequest -Uri $mysqlUrl -OutFile $mysqlPath

# Download javax.servlet.jsp-api
$jspApiUrl = "https://repo1.maven.org/maven2/javax/servlet/jsp/javax.servlet.jsp-api/2.3.3/javax.servlet.jsp-api-2.3.3.jar"
$jspApiPath = "$libDir\javax.servlet.jsp-api-2.3.3.jar"
Invoke-WebRequest -Uri $jspApiUrl -OutFile $jspApiPath

# Download javax.el-api
$elApiUrl = "https://repo1.maven.org/maven2/javax/el/javax.el-api/3.0.0/javax.el-api-3.0.0.jar"
$elApiPath = "$libDir\javax.el-api-3.0.0.jar"
Invoke-WebRequest -Uri $elApiUrl -OutFile $elApiPath

Write-Host "Downloaded all dependencies to $libDir"
