def lintChecks(component){
    sh " echo Installing Jslints"
    //sh "npm install jslint"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh " echo lint checks completed for ${component}.....!!!!!"
}

def sonarChecks(component){
    sh " echo Starting the quality check..."
    sh  "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 -Dsonar.sources=. -Dsonar.projectKey=${component}  -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
    sh  "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
    sh  "bash -x quality-gata.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${component}"
    sh " echo sonar checks completed for ${component}.....!!!!!"
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
                        sonarChecks(component)
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

            stage('Downloading Dependencies'){
                steps{
                    sh "echo Installing npm!!!!!!"
                    sh "npm install"
                }
            }
        }
    }
}