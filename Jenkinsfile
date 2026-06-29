pipeline {
     agent any

    tools {
          maven 'maven-3.9.9'
    }

    stages {
        stage('Checkout'){
           steps{
              git branch: 'main', url:'https://github.com/archanat97/Selenium-test-framework'
       }
     }

    stage('Build') {
      steps {
          bat 'mvn clean install'
      }
   }

   stage('Test'){
     steps {
          bat 'mvn test'
      }
   }
   stage('Reports') {
     steps {
       publishHTML(target: [
              reportDir: 'src/test/resources/ExtentReports',
              reportFiles: 'ExtentReport.html',
              reportName: 'Extent Spark Report'
            ])
        }
     }
 }
  post {
    always{
     archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint:true junit'target/surefire-reports/*.xml'
    }
  success {
    emailext(
      to: 'archanamayathiri@gmail.com',
      subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
      body: """
      <html>
      <body>
      <p> Hello Team ,</p>
      <p> The latest Jenkins build had completed. </p>
      <p> The build log is attached. </p>
      <p> <b> Extent Report.</b> <a href="http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/"> Click here </a> </p>
     )
   }
 }
}