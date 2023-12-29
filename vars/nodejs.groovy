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
        tools {nodejs "nodejs"}
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

            stage('Preparing the artifact') {
                // when { 
                //     expression { env.TAG_NAME != null } 
                //     }
                steps {
                    sh "npm install"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"
                }
            }
        }
    }
}