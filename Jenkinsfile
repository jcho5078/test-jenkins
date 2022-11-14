node {
    stage('Prepare') {
        steps {
            echo '=== Prepare ==='
            checkout scm
            sh 'chmod +x ./mvnw'
            echo '=== prepare success ==='
            echo './mvnw -version'
        }
    }

    stage('Build') {
        steps {
            echo '=== Build ==='
            echo 'Build number : $BUILD_NUMBER'
            sh './mvnw clean'
            app = docker.build("asia.gcr.io/graphite-ruler-366202/test-rds:$BUILD_NUMBER")
            sh 'docker push asia.gcr.io/graphite-ruler-366202/test-rds:$BUILD_NUMBER'
        }
    }

    stage('Deploy') {
        steps {
            echo '=== Deploy ==='
            sshPublisher(publishers: [
                sshPublisherDesc(configName: 'service-deploy'
                , transfers: [sshTransfer(cleanRemote: false, excludes: ''
                , execCommand: 'docker-compose up'
                , execTimeout: 120000, flatten: false, makeEmptyDirs: false
                , noDefaultExcludes: false
                , patternSeparator: '[, ]+', remoteDirectory: 'deploy/'
                , remoteDirectorySDF: false, removePrefix: 'build/libs'
                , sourceFiles: 'build/libs/*.jar')]
                , usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)
            ])
        }
    }

    stage('Ending') {
        steps {
            echo '== Ending'
            sh 'docker rmi asia.gcr.io/graphite-ruler-366202/test-rds:$BUILD_NUMBER'
        }
    }
}