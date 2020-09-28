package com.ooftf.publish.bintray.internal

import com.ooftf.publish.bintray.AndroidSoftwareComponent
import com.ooftf.publish.bintray.MavenPublicationAttachments
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.tasks.javadoc.Javadoc

class AndroidAttachments extends MavenPublicationAttachments {

    AndroidAttachments(String publicationName, Project project, def variant) {
        super(androidComponentFrom(project),
                androidSourcesJarTask(project, publicationName, variant),
                androidJavadocsJarTask(project, publicationName, variant),
                androidArchivePath(variant))
    }

    private static SoftwareComponent androidComponentFrom(Project project) {
        return  project.objects.newInstance(AndroidSoftwareComponent.class) as SoftwareComponent
    }

    private static Task androidSourcesJarTask(Project project, String publicationName, def variant) {
        def sourcePaths = variant.sourceSets.collect { it.javaDirectories }.flatten()
        return sourcesJarTask(project, publicationName, sourcePaths)
    }

    private static Task androidJavadocsJarTask(Project project, String publicationName, def variant) {
        Javadoc javadoc = project.task("javadoc${publicationName.capitalize()}", type: Javadoc) { Javadoc javadoc ->
            javadoc.source = variant.javaCompiler.source
            javadoc.classpath = variant.javaCompiler.classpath
        } as Javadoc
        return javadocsJarTask(project, publicationName, javadoc)
    }

    private static def androidArchivePath(def variant) {
        return variant.outputs[0].packageLibrary
    }
}
