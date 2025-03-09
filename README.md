# About the repository POC

Create EC2 INSTANCE t2.meduim.
1. We are trying to install java 17, maven, git, mysql using ec2 user data script.
2. After we are cloning this spring boot application (changed jdbc url public vp4 in application.properties before pushing) and building the jar using "mvn clean package -Dmaven.test.skip=true"
3. Then running the jar in the target folder using "java -jar *.jar"
4. Use postman and try to insert the records, the records will be inserted in the mysql which we installed in ec2 instance.
