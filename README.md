# COM504
Parking Meter System 

![use-cases](https://user-images.githubusercontent.com/33484962/51384351-faf1f400-1b13-11e9-9307-9cdc961fa997.png)

![robustness](https://user-images.githubusercontent.com/33484962/51384421-32f93700-1b14-11e9-916d-caf45c0e1bfd.png)


![official class diagram](https://user-images.githubusercontent.com/33484962/51384464-558b5000-1b14-11e9-9c69-e49547f99d1d.png)

Schedule is hardcoded and cannot be modified when compiled.

To run project, you need to have apache tomcat installed. If installed, go to Netbeans IDE, click Tools, Servers, set Server to Apache Tomcat and set the port number to '1234'. Build the projects and run. 

The main class of the client project is 'ParkingSystemFrame', if you run the parking project, it should clearly state it as main class.

Most classes are complemented with JavaDoc as to make reading the code easier.

Junit tests are available in the web app module which is responsible for downloading schedule, verifying payments, parking ticket issuance, etc.




