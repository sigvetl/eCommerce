pipeline {
    agent any

    tools {
        maven 'maven_3_6_3' 
    }

    stages {
        stage('Build') {
            steps{
                sh 'mvn clean install'
            }
        }
        stage('copy dependencies') {
            steps{
                sh 'mvn dependency:copy-dependencies'
            }
        }
        stage('Run') {
            steps{
                sh 'cd target/ java -cp auth-course-0.0.1-SNAPSHOT.jar:dependency auth-course'
            }
        }
    }
}
