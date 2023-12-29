def lintChecks(component){
    sh " echo Installing Jslints"
    //sh "npm install jslint"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh " echo lint checks completed for ${component}.....!!!!!"
}

def call(component)
{
    pipeline {
        agent any

        environment {
            sonar = credentials('sonar')
            sonar_URL = "172.31.0.179"
        }
        stages{
            stage('Lint checks'){
                steps {
                    script
                    {
                        lintChecks(component)
                    }
                }
            }

            stage('Sonar checks'){
                steps {
                    script
                    {
                        env.ARGS="-Dsonar.sources=."
                        common.sonarChecks(component)
                    }
                }
            }

            stage('Test Cases') {
                    parallel {
                        stage('Unit Tests'){
                            steps {
                                sh "echo Unit Testing ......."
                            }
                        }

                        stage('Integration Tests'){
                            steps {
                                sh "echo Integration Testing ......."
                            }
                        }

                        stage('Functional Tests'){
                            steps {
                                sh "echo Functional Testing ......."
                            }
                        }
                    }
            }

            stage('Downloading Dependencies'){
                steps{
                    sh "echo Installing npm!!!!!!"
                    sh "npm install"
                }
            }
        }
    }
}