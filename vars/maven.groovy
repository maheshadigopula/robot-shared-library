def lintChecks(component){
    sh "echo Installing Maven"
    // sh "sudo yum install maven -y"
    // sh "mvn checkstyle:checkstyle"
    sh " echo lint checks completed for ${component}.....!!!!!"
}


def sonarChecks(component){
    sh " echo Starting the quality check..."
    mvn clean compile
    sh " sonar-scanner -Dsonar.host.url=http://${sonar_URL}:9000 -Dsonar.java.binaries=target/ -Dsonar.projectKey=${component} -Dsonar.login=admin -Dsonar.password=123 "
    sh " curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gate.sh"
    sh " bash -x quality-gate.sh ${sonar_USR} ${sonar_PSW} ${sonar_URL} ${component}"
    sh " echo lint checks completed for ${component}.....!!!!!"
}


def call(component)
{
    pipeline {
        agent any

        environment {
            sonar = credentials('sonar')
            sonar_URL = "172.31.0.34"
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

