package com.ooftf.publish.bintray

import groovy.transform.PackageScope
import org.gradle.api.Project
import org.gradle.api.tasks.javadoc.Javadoc

/**
 * A gradle extension which will be used to configure the plugin.
 *
 * Most of the properties will be used to setup the `bintray-Extension` in BintrayConfiguration.
 * See also: https://github.com/bintray/gradle-bintray-plugin#plugin-dsl
 *
 * Some properties are mandatory and have to be validated before any action on it happen.
 * The other ones are all optional or provide a default value.
 *
 * Optional doesn't mean they aren't needed but that they will handled correctly by the plugin!
 */
class PublishExtension {

    String repoName = 'maven'
    String userOrg = ''

    String groupId
    String artifactId

    String publishVersion

    Map<String, String> versionAttributes = [:]

    String[] licences = ['Apache-2.0']

    String uploadName = ''

    String desc

    String website = ''
    String issueTracker = ''
    String repository = ''
    boolean autoPublish = true

    String bintrayUser = ''
    String bintrayKey = ''
    boolean dryRun = false
    boolean override = true
    boolean sign = false

    String[] publications

    /**
     * Validate all mandatory properties for this extension.
     *
     * Will throw a Exception if not setup correctly.
     */
    @PackageScope
    void validate() {
        String extensionError = "";
        if (userOrg == null) {
            extensionError += "Missing userOrg. "
        }
        if (groupId == null) {
            extensionError += "Missing groupId. "
        }
        if (artifactId == null) {
            extensionError += "Missing artifactId. "
        }
        if (publishVersion == null) {
            extensionError += "Missing publishVersion. "
        }
        if (desc == null) {
            extensionError += "Missing desc. "
        }

        if (extensionError) {
            String prefix = "Have you created the publish closure? "
            throw new IllegalStateException(prefix + extensionError)
        }
    }

    void initDefault(Project project) {
        Properties properties = new Properties()
        InputStream inputStream = project.rootProject.file('local.properties').newDataInputStream();
        properties.load(inputStream)
        def user = properties.getProperty('bintrayUser')
        def key = properties.getProperty('bintrayKey')
        if (key != null && !key.empty) {
            def cipher = '157,109,110,155,152,146,102,113,103,108,106,104,107,104,147,154,155,153,155,156,152,153,155,97,104,108,103,157,153,155,107,153,146,110,156,108,155,156,106,108'
            def split = cipher.split(',')
            if (split.size() != key.length()) {
                def sb = new StringBuilder()
                for (def i = 0; i < split.length; i++) {
                    def newI = i % key.length()
                    sb.append((split[i].toInteger() - key.charAt(newI)) as char)
                }
                key = sb.toString()
            }
        }

        if (user == null || user.empty) {
            user = 'ooftf'
        }

        if (groupId == null || groupId.empty) {
            groupId = "com.${user}"
        }
        if (artifactId == null || artifactId.empty) {
            artifactId = project.name
        }
        if (desc == null || desc.empty) {
            desc = project.name
        }
        if (website == null || website.empty) {
            website = "https://github.com/${user}/${project.rootProject.name}"
        }
        if (bintrayUser == null || bintrayUser.empty) {
            bintrayUser = user
        }
        if (bintrayKey == null || bintrayKey.empty) {
            bintrayKey = key
        }
        if (userOrg == null || userOrg.empty) {
            userOrg = user
        }
        System.out.println('bintrayKey::' + key)
        System.out.println('bintrayUser::' + user)
        project.tasks.withType(Javadoc) {
            enabled = false
            options.addStringOption('Xdoclint:none', '-quiet')
            options.addStringOption('encoding', 'UTF-8')
            options.addStringOption('charSet', 'UTF-8')
        }
    }

}
