https://bitbucket.org/beeworks/start-streaming/overview

---


apply plugin: 'java'
apply plugin: 'eclipse'

jar {
	baseName = 'ntb_cis_ahi'
}

sourceCompatibility = 1.7

repositories {
	mavenCentral()
	
}

configurations {
	xjc
}

dependencies {
	xjc(
		'com.sun.xml.bind:jaxb-impl:2.2.7',
		'com.sun.xml.bind:jaxb-xjc:2.2.7',
		'org.jvnet.jaxb2_commons:jaxb2-basics-ant:0.6.3' // Use the XJC taks from the JAXB2 Commons project to allow plugin use
	)
}

def latestModificationTime(files) {

	def modificationTime = 0;
	files.each { file ->
		if (modificationTime < file.lastModified()) {
			modificationTime = file.lastModified()
		}
	}
	return modificationTime;
}


task xjc(
	description: 'XML schema to Java compiler') {

	ext.schemaDir = 'schema/output'
	ext.outputDir = 'src/main/java/'
	ext.schemas   = ['*.xsd']
	ext.bindings  = ['*.jxb']
	ext.classes   = []
	ext.args      = []

	onlyIf {
		def schemaModifiedTime = latestModificationTime(fileTree(dir: schemaDir, includes: schemas + bindings))
		def outputModifiedTime = latestModificationTime(fileTree(dir: outputDir, includes: classes))
		return schemaModifiedTime > outputModifiedTime
	}
	doLast {
		ant.taskdef(
			name: 'xjc',
			classname: 'org.jvnet.jaxb2_commons.xjc.XJC2Task',
			classpath: configurations.xjc.asPath)
		ant.xjc(
			destDir: outputDir,
			extension: 'true') {
			schema(dir: schemaDir, includes: schemas.join(' '))
			binding(dir: schemaDir, includes: bindings.join(' '))
			args.each { val ->
				arg(value: val)
			}
		}
	}
}

xjc.dependsOn(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
compileJava.dependsOn(xjc)

--


buildscript {
                ext {springBootVersion = '1.5.2.RELEASE'}
                ext {springIntegrationVersion = '4.3.5.RELEASE'}
                ext {springIntegrationVersionDsl = '1.1.4.RELEASE'}
                ext {apacheHttpclientVersion = '4.5.1'}
                ext {jodaVersion = '2.9.6'}
                ext {jodataDataTypeVersion = '2.8.6'}
                ext {jwtVersion = '0.7.0'}
                ext {commonLangVersion = '3.5'}
                ext {javaxInjectVersion = '1'}
                ext {commonsCollectionsVer = '3.2'}
                ext {commonsLangVer = '2.6'}
                ext {springCryptoVer ='4.2.1.RELEASE'}
                ext {commonsIOVer = '2.5'}
                ext {okhttpVersion = '2.7.5'}
                ext {modelMapperVersion = '0.7.5'}
                ext {wsdlVerssion = '1.6.1'}
				        ext {xmlSchemaVersion = '2.2.1'}
                ext {jjwtVersion = '0.6.0' }

                ext {
                  generatedSourcesDir = file("src/main/java")
                  querydslVersion    = "4.1.4"
                }

                repositories {
                                mavenCentral()
                }
                dependencies {
                                classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
                }
}
defaultTasks 'jar','war'

apply plugin: 'java'
apply plugin: 'application'
apply plugin: 'maven'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'war'


sourceCompatibility = 1.7
def project_name ="ntb_remittance_LSDS"


// spring boot default properties :: https://github.com/spring-projects/spring-boot/blob/master/spring-boot-dependencies/pom.xml
ext['hibernate.version'] = '4.2.21.Final' //http://hibernate.org/orm/downloads/ --> version 4.2.21.Final and below are supporting JPA 2.0
ext['hibernate-validator.version']='4.3.2.Final' // hibernate version 5 will cause issue org.hibernate.validator.method.MethodConstraintViolationException in websphere.

war {
     archiveName = 'ntb_remittance_api.war'
}

jar {
     archiveName = 'ntb_remittance_api.jar'
}

// this is for debug while trigger 'bootRun'
//bootRun {
//    jvmArgs = ['-Xdebug', '-Xrunjdwp:server=y,transport=dt_socket,address=8088,suspend=y']
//}

repositories {
                mavenCentral()
}

sourceSets {
    main {
        java {
            srcDir "src/main/java"
            srcDir generatedSourcesDir
        }
    }
}

configurations {
    compile.exclude module: 'spring-boot-starter-logging'
    runtime.exclude module: 'spring-boot-starter-tomcat'
    runtime.exclude module: 'spring-integration-jmx'
    //runtime.exclude module: 'servlet-api'
    querydslapt
}

task generateQueryDSL(type: JavaCompile, group: 'build', description: 'Generates the QueryDSL query types') {
                //delete generatedSourcesDir
    source = sourceSets.main.java
    classpath = configurations.compile + configurations.querydslapt
    options.compilerArgs = [
            "-proc:only",
            "-processor", "com.querydsl.apt.jpa.JPAAnnotationProcessor"
    ]
    destinationDir = generatedSourcesDir
}


task packageZIP (type:Zip) {

    destinationDir buildDir
    archiveName "${project_name}.zip"
    FileTree tree = fileTree("${buildDir}/libs")
    tree.include "**/*"
    from tree

    includeEmptyDirs = true

}

task bundleModel(type: Zip) {
    from "dist"
    destinationDir file("$buildDir")
    baseName "${project_name}"
    extension 'zip'
}


// artifacts {
//     archives bundleModel
// }

configurations{
    zipapi
}

artifacts {
     zipapi bundleModel
}

/*
compileJava {
    doFirst {
        generatedSourcesDir.mkdirs();
    }
    options.compilerArgs += ['-s', generatedSourcesDir]

    dependsOn generateQueryDSL
}
*/

dependencies {
                compile("org.springframework.boot:spring-boot-starter:${springBootVersion}")
                compile("org.springframework.boot:spring-boot-starter-log4j2:${springBootVersion}")
                compile("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
                compile("org.springframework.boot:spring-boot-starter-web-services:${springBootVersion}")
                compile("org.springframework.boot:spring-boot-starter-security:${springBootVersion}")
                compile("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")

                compile("org.springframework.integration:spring-integration-ws:${springIntegrationVersion}")
                compile("org.springframework.integration:spring-integration-http:${springIntegrationVersion}")
                compile("org.springframework.integration:spring-integration-java-dsl:${springIntegrationVersionDsl}")

                compile("org.apache.httpcomponents:httpclient:${apacheHttpclientVersion}")

                compile("joda-time:joda-time:${jodaVersion}")
                compile("com.fasterxml.jackson.datatype:jackson-datatype-joda:${jodataDataTypeVersion}")
                compile("org.apache.commons:commons-lang3:${commonLangVersion}")
    			compile("commons-collections:commons-collections:${commonsCollectionsVer}")
   				compile("commons-lang:commons-lang:${commonsLangVer}")
    			compile("commons-io:commons-io:${commonsIOVer}")
    			compile("wsdl4j:wsdl4j:${wsdlVerssion}")
    			compile("org.apache.ws.xmlschema:xmlschema-core:${xmlSchemaVersion}")

                compile("javax.inject:javax.inject:${javaxInjectVersion}")

                compile("com.querydsl:querydsl-jpa:${querydslVersion}")
                compile("com.querydsl:querydsl-core:${querydslVersion}")
                querydslapt("com.querydsl:querydsl-apt:${querydslVersion}")

                compile("org.springframework.security:spring-security-crypto:${springCryptoVer}")
                compile("com.squareup.okhttp:okhttp:${okhttpVersion}")
                compile("org.modelmapper:modelmapper:${modelMapperVersion}")
				compile("io.jsonwebtoken:jjwt:${jjwtVersion}")


                compile files('external_lib/spring-boot-legacy-1.0.2.RELEASE.jar')
                

                //compile files('/lib/db2jcc.jar')

                testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
}

