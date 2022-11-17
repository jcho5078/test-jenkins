node {
    stage('Prepare') {
        echo '=== Prepare ==='
        checkout scm
        sh 'chmod +x ./mvnw'
        echo '=== prepare success ==='
    }

    stage('Build') {
        echo '=== Build ==='
        echo 'Build number : $BUILD_NUMBER'
        sh './mvnw clean'
        sh './mvnw package'
        app = docker.build("asia.gcr.io/graphite-ruler-366202/test-rds:$BUILD_NUMBER")
        sh 'echo "docker push asia.gcr.io/graphite-ruler-366202/test-rds:$BUILD_NUMBER" > /JenkinsPipe'
    }

    stage('Deploy') {
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

    stage('Ending') {
        echo '== Ending'
        sh 'docker rmi asia.gcr.io/graphite-ruler-366202/test-rds:$BUILD_NUMBER'
    }
}