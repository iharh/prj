task buildDocker(type: Docker) {
    push = false
    applicationName = 'cb-cps-app'
    dockerfile = file('Dockerfile')
    doFirst {
        copy {
            from('cb-cps-app/build/libs/') {
                include '*.jar'
            }
            into stageDir
        }
    }
}
