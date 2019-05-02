# webapp-in-springboot-aws-scripting
demonstrated the concepts in aws with springboot application                                               
Technology Stack                                                           
Operating System - Linux (Ubuntu) Programming Language - Java Relational Database - MySQL Backend Framework - Spring and Hibernate

Build Instructions                               
Build the Application using Maven Build                                
Deploy Instructions                                       
Run the "NoteappApplication.java" as Java Application                                       
When the Application successfully deployes run Postman and go to "localhost:8080/register/user" with post request           
You will need to add email address and password in the Body header                     
After successful creation of the user, a token will be generated. Copy the token.                       
For logging in use "localhost:8080/" address with get request                         
When loging in use the token as Authentication to get logged in                           
Running Tests                                  
Run the "NoteappApplicationTests.java" file as a JUnit Test                                  
API Guidelines                                  
Link:- https://app.swaggerhub.com/apis-docs/csye6225/csye6225-spring2019/assignment-07                             

CI/CD                                                      
Currently using Cirle CI tools to build new artifacts on each commit on GitHub and upload new succeeded builds to Amazon S3 bucket.                                                                   
                  
Configure your webapp repository in Circle CI and follow the project.                                       

Configure below variables in Circle CI environment variables for the webapp project.                          
-- AWS_ACCESS_KEY_ID -- AWS_BUCKET_NAME
-- AWS_DEFAULT_REGION
-- AWS_SECRET_ACCESS_KEY
                                      
Create circleci user in AWS IAM.                              

Create New S3 Bucket to hold the build artifacts.                                       

Assign appropriate policies to allow circleci user to access and upload artifacts in S3 Bucket.                           

Create Code Deploy Application to deploy new revisions onto EC2 instances for each new build
