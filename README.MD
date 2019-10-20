# Introduction
This Java desktop app is for managing media items (Books, CDs, DVDs, Movies) with MySQL database.

<h4>To load and run the project in the command line terminal:</h4>
<p>
<ul>
  <li>
    NOTE: Java, JDK, MySQL database server and Eclipse are already installed in your system <br>
    NOTE: mysql-connector-java-8.0.18.jar is included. <br>
    Database connection URL can be the following depending on version of MySQL server or mysql-connector: <br>
    jdbc:mysql://localhost:3306/media <br>
    jdbc:mysql://localhost:3306/media?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
  </li>
  <li>
    Clone this project to your computer with GIT.
  </li>
  <li>
    To run the project:<br> 
    Create database in MySQL by executing "source inventory_db.sql" or run "inventory_db.sql" in MySQLWorkBench where the "inventory_db.sql" file is located in the db folder. <br> 
    Compile and run InventoryTest.java located at com.ryanliang.inventorygui
  </li>
</ul>
</p>