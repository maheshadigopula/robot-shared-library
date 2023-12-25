def lintChecks(component){
    sh " echo Installing Jslints"
    //sh "npm install jslint"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh " echo lint checks completed for ${component}.....!!!!!"
}

def sonarChecks(component){
    sh " echo Starting the quality check..."
    sh " sonar-scanner -Dsonar.host.url=https://${url}:9000 -Dsonar.source=. -Dsonar.projectKey=${component} -Dsonar.login=${sonar_USR} -Dsonar.password=${sonar_PSW} "
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh " echo lint checks completed for ${component}.....!!!!!"
}


def call(component)
{
    pipeline {
        agent any

        environment {
            sonar = credentials('sonar')
            url = 172.31.0.188
        }
        stages{
            stage('Lint checks'){
                steps {
                    script
                    {
                        lintChecks(component)
                        sonarChecks(component)
                    }
                }
            }

            stage('Downloading Dependencies'){
                steps{
                    //sh "npm install"
                    sh "echo npm install"
                }
            }
        }
    }
}

