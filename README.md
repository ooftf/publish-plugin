# publish-bintray [ ![Download](https://api.bintray.com/packages/ooftf/maven/publish-plugin/images/download.svg) ](https://bintray.com/ooftf/maven/publish-plugin/_latestVersion)
## 区别
    针对我自己，添加了大量的默认值  
    具体默认值可参考 PublishExtension.initDefault方法  
    groupId = com.${用户名}  
    artifactId = project.name  
    website = "https://github.com/${用户名}/${project.rootProject.name}"  
    userOrg = bintrayUser  
    bintrayKey和bintrayUser可以在local.properties中直接设置  
    通过转换的方式简化了bintrayKey，只要记住自己的常用密码就可以了（只对我自己管用）
## 使用方式
#### gradle


    buildscript {
        repositories {
            maven { url 'https://dl.bintray.com/ooftf/maven' }
        }
        dependencies {
            classpath 'com.ooftf:publish-bintray:0.0.1'
        }
    }


    apply plugin: 'ooftf-publish'
    publish {
        publishVersion = '0.0.1'
    }
    
#### 默认参数
```groovy

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
        boolean override = false
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
        }
    
    }

```
  
#### local.properties中设置

    bintrayKey=uyelsjfh34iosan3io8so3
    bintrayUser=ooftf


