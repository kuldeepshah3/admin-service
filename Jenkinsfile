pipeline {
    agent any

    environment {
        // To run batch commands
        PATH = "C:\\Program Files\\Java\\jdk1.8.0_301\\bin;C:\\Program Files\\apache-maven-3.8.2\\bin;C:\\WINDOWS\\SYSTEM32;C:\\Program Files\\Docker\\Docker\\resources\\bin"
    }
    stages {

        stage ('Checkout new changes periodically') {
            steps {
                script {
                    properties([pipelineTriggers([pollSCM('*/20 * * * *')])])
                }
                //define scm connection for polling
                git branch: 'main', credentialsId: 'GitHub-SSH', url: 'git@github.com:kuldeepshah3/admin-service.git'
            }
        }

        stage ('Print useful values') {
            steps {
                script {
                    echo ${env.BUILD_ID}
                }
            }
        }

        stage('Code compilation)') {
           // Run the maven build
            steps {
                script {
                    // Get the Maven tool from global configuration.
                    def mvnHome = tool 'Maven 3.8.2'
                    bat(/mvn clean compile/)
                    echo 'Code Compilation successful'

                }
            }
        }

        stage('Quality Gate (Integration Tests and Code Scan)') {
           // Run the maven build
            steps {
                script {
                    // Get the Maven tool from global configuration.
                    def mvnHome = tool 'Maven 3.8.2'
                    bat(/mvn verify findbugs:findbugs cobertura:cobertura pmd:pmd site:site -Dmaven.test.failure.ignore=true package/)
                    echo 'Code Analysis successful'
                }
            }
        }

        /* stage('Prepare Docker Image') {
            steps {
                echo 'Building docker image....'
                bat(/docker build -t kuldeepshah3\/admin-service ./)
                echo 'Docker build successful'
            }
        }

        stage('Upload image to docker hub') {
            steps {
                echo 'Push image to Docker Hub'
                bat(/docker push kuldeepshah3\/admin-service:latest/)
                echo 'Docker push successful'
            }
        } */

    }

    post {
        // Always runs. And it runs before any of the other post conditions.
        always {
            // Let's wipe out the workspace before we finish!
            // deleteDir()
        }
    }

// The options directive is for configuration that applies to the whole job.
    options {
        // For example, we'd like to make sure we only keep 10 builds at a time, so
        // we don't fill up our storage!
        buildDiscarder(logRotator(numToKeepStr: '5'))

        // And we'd really like to be sure that this build doesn't hang forever, so
        // let's time it out after an hour.
        timeout(time: 25, unit: 'MINUTES')
    }
}