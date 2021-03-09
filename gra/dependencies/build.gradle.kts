import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("io.spring.dependency-management").version("1.0.11.RELEASE").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply {
        plugin("io.spring.dependency-management")
    }

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.dom4j:dom4j:2.1.3") {
                exclude("jaxen:jaxen")
                exclude("javax.xml.stream:stax-api")
                exclude("net.java.dev.msv:xsdlib")
                exclude("pull-parser:pull-parser")
                exclude("relaxngDatatype:relaxngDatatype")
                exclude("javax.xml.bind:jaxb-api")
                exclude("xpp3:xpp3")
            }
        }
    }
}
