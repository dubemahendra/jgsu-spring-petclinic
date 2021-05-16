pipeline {
    agent any
    triggers {pollSCM('* * * * *')}
    stages {
        stage('Checkout') {
            steps {
               git branch: 'main', url: 'https://github.com/dubemahendra/jgsu-spring-petclinic.git'
                   }
            }
			stage('build'){
			steps	{
                sh './mvnw clean package' 
					}
			post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
				}	
                changed
                emailtext subject: 'Job \'${JOB_NAME}\' (${BUILD_NUMBER} is waiting for input',
                body: 'Please do to ${BUILD_URL} and verify the build',
                attachLog: ture,
                compressLog: ture,
                to:"dubemahendra@gmail.com",
                recipientProviders: [upstreamDevelopers(), requester()]

           }
        }
    }
}
