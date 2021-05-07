plugins {
    `kotlin-dsl`
}

dependencies {
    // implementation(platform("com.example.platform:plugins-platform"))

    api(project(":commons"))
    implementation(project(":commons"))
}
