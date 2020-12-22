package com.ooftf.publish.bintray

import com.jfrog.bintray.gradle.BintrayPlugin
import com.ooftf.publish.bintray.internal.AndroidAttachments
import com.ooftf.publish.bintray.internal.JavaAttachments
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.api.tasks.javadoc.Javadoc

class ReleasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        PublishExtension extension = project.extensions.create('publish', PublishExtension)
        project.afterEvaluate {
            project.tasks.withType(Javadoc) {
                enabled = false
                options.addStringOption('Xdoclint:none', '-quiet')
                options.addStringOption('encoding', 'UTF-8')
                options.addStringOption('charSet', 'UTF-8')
            }
            project.tasks.withType(GenerateModuleMetadata) {
                enabled = false
            }
            extension.initDefault(project)
            extension.validate()
            attachArtifacts(extension, project)
            new BintrayConfiguration(extension).configure(project)
        }
        project.apply([plugin: 'maven-publish'])
        new BintrayPlugin().apply(project)
    }

    private static void attachArtifacts(PublishExtension extension, Project project) {
        project.plugins.withId('com.android.library') {
            project.android.libraryVariants.all { variant ->
                String publicationName = variant.name
                MavenPublication publication = createPublication(publicationName, project, extension)
                new AndroidAttachments(publicationName, project, variant).attachTo(publication)
            }
        }
        project.plugins.withId('java') {
            def mavenPublication = project.publishing.publications.find { it.name == 'maven' }
            if (mavenPublication == null) {
                String publicationName = 'maven'
                MavenPublication publication = createPublication(publicationName, project, extension)
                new JavaAttachments(publicationName, project).attachTo(publication)
            }
        }
    }

    private static MavenPublication createPublication(String publicationName, Project project, PublishExtension extension) {
        PropertyFinder propertyFinder = new PropertyFinder(project, extension)
        String groupId = extension.groupId
        String artifactId = extension.artifactId
        String version = propertyFinder.publishVersion

        PublicationContainer publicationContainer = project.extensions.getByType(PublishingExtension).publications
        return publicationContainer.create(publicationName, MavenPublication) { MavenPublication publication ->
            publication.groupId = groupId
            publication.artifactId = artifactId
            publication.version = version
        } as MavenPublication
    }
}
