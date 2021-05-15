import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy

import org.gradle.api.artifacts.repositories.PasswordCredentials

import org.gradle.internal.authentication.AllSchemesAuthentication
import org.gradle.internal.credentials.DefaultPasswordCredentials
import org.gradle.internal.resource.ExternalResourceName
import org.gradle.internal.resource.transfer.MyAccessorBackedExternalResource
import org.gradle.internal.resource.transport.http.DefaultHttpSettings
import org.gradle.internal.resource.transport.http.DefaultSslContextFactory
import org.gradle.internal.resource.transport.http.HttpSettings
import org.gradle.internal.resource.transport.http.MyHttpClientHelper
import org.gradle.internal.resource.transport.http.MyHttpResourceUploader
import org.gradle.internal.resource.transport.http.SslContextFactory
import org.gradle.internal.verifier.HttpRedirectVerifier

import java.net.URI

import org.gradle.kotlin.dsl.*

class MyCustomPlugin : Plugin<Project> {

    class MyNoopHttpRedirectVerifier : HttpRedirectVerifier {
        override fun validateRedirects(redirectLocations : MutableCollection<URI>) {
            // Noop
        }
    }

    override fun apply(project: Project): Unit = project.run {
        // apply(plugin = "base")

        val ivyPublishDir = "$buildDir/ivy"

        val nexusUsername: String by project
        val nexusPassword: String by project

        val pubCfg by configurations.creating

        dependencies {
            //pubCfg("org.eclipse.jgit:org.eclipse.jgit:4.9.2.201712150930-r")
            pubCfg(project(":p1"))
            pubCfg(project(":p2"))
        }

        tasks {
            register<Copy>("ivyCopyJars") {
                from(pubCfg)
                into(ivyPublishDir)
                include("*.jar")
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
            create("ivyDescr") {
                doLast {
                    val ivyXmlWriter = MyIvyXmlWriter()

                    val resolvedArtifacts: Set<ResolvedArtifact> = pubCfg.getResolvedConfiguration().getResolvedArtifacts()
                    resolvedArtifacts.forEach { result: ResolvedArtifact ->
                        ivyXmlWriter.addArtifact(result)
                    }

                    val ivyDescriptorFileName = "$ivyPublishDir/ivy.xml" 
                    ivyXmlWriter.writeTo(file(ivyDescriptorFileName))
                    logger.quiet("generated: $ivyDescriptorFileName")
                }
            }
            create("upload") {
                doLast {
                    val credentials: PasswordCredentials = DefaultPasswordCredentials(nexusUsername, nexusPassword)
                    val sslContextFactory: SslContextFactory = DefaultSslContextFactory()
                    val redirectVerifier: HttpRedirectVerifier = MyNoopHttpRedirectVerifier()

                    // Collections.singleton
                    val authentications = listOf(AllSchemesAuthentication(credentials))

                    val httpSettings: HttpSettings = DefaultHttpSettings.builder()
                        .withAuthenticationSettings(authentications)
                        .withSslContextFactory(sslContextFactory)
                        .withRedirectVerifier(redirectVerifier)
                        .build()
                    
                    val httpClientHelper: MyHttpClientHelper = MyHttpClientHelper(httpSettings)
                    /*
                    val uploader: MyHttpResourceUploader = MyHttpResourceUploader(httpClientHelper);

                    val externalResourceName = ExternalResourceName(URI("http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/a.txt"))

                    val externalResource = MyAccessorBackedExternalResource(externalResourceName, uploader)
                    */
                    logger.quiet("done upload")
                }
            }
        }
    }
}
