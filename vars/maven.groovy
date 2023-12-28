def lintChecks(component){
    sh "echo Installing Maven"
    // sh "sudo yum install maven -y"
    // sh "mvn checkstyle:checkstyle"
    sh " echo lint checks completed for ${component}.....!!!!!"
}


def sonarChecks(component){
    sh " echo Starting the quality check..."
    sh  "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000  -Dsonar.projectKey=${component}  -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
    sh  "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
    sh  "bash -x quality-gata.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${component}"
    sh " echo lint checks completed for ${component}.....!!!!!"
}


def call(component)
{
    pipeline {
        agent any

        environment {
            SONAR = credentials('sonar')
            SONAR_URL = "172.31.0.34"
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
        }
    }
}

