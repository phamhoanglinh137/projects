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
