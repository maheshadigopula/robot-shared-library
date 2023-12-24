def lintChecks(component){
    sh "echo Installing Maven"
    // sh "sudo yum install maven -y"
    sh "mvn checkstyle:checkstyle"
    sh " echo lint checks completed for ${component}.....!!!!!"
}

def call(component)
{
    pipeline {
        agent any
        stages{
            stage('Lint checks'){
                steps {
                    script
                    {
                        lintChecks(component)
                    }
                }
            }
        }
    }
}

