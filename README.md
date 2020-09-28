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
、、、

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
    
、、、
#### local.properties中设置
、、、

bintrayKey=uyelsjfh34iosan3io8so3
bintrayUser=ooftf

、、、
