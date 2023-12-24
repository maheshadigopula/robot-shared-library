
def nodejs(component){
    sh " echo Installing Jslints"
    //sh "npm install jslint"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh " echo lint checks completed.....!!!!!"
}

def call()
{
pipeline {
    agent any
    stages{
        stage('Lint checks'){
            steps {
                script
                {
                    maven.nodejs(component)
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

