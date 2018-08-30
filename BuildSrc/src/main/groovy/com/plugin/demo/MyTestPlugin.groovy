import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class MyTestPlugin implements  Plugin<Project> {

    @Override
    void apply(Project project) {
        gradleDoSomething(project)
        createTasks(project)
    }

    void gradleDoSomething(Project project){
        project.gradle.taskGraph.whenReady {
            project.getTasks().all {
                if (it.name.equals('compileDebugKotlin')){
                    /*KotlinCompile ktask = it
                    ktask.getClasspath().each { File file->
                        println('===------:'+file)
                    }*/
                    KotlinCompile ktask = it
                    ktask.getSource().each { File file->
                        println('===------:'+file)
                    }
                }else if (it.name.equals('compileDebugKotlin')){

                }
            }
        }
    }

    void createTasks(Project project){
        project.tasks.create(name:'listJars',group:'myself'){
            doFirst {
                project.repositories.each {
                    println "repository: ${it.name} ('${it.url}')"
                }
//        println 'xxxxpp:'+repositories.collect { it.name }
//        project.configurations.compile.each { File file -> println 'ooxx----'+file.name }
            }
        }

        /**************************/
        project.tasks.create(name:'scanFileModify',group:'myself'){
            doLast {
                project.rootProject.subprojects.each {
                    if(it.hasProperty('android')){
                        println(it.getBuildDir().absolutePath+'==src path='+it.android.sourceSets.main.java.srcDirs[0].absolutePath)
                        getDirectory(it.android.sourceSets.main.java.srcDirs[0])
                    }
                }
            }
        }


        /**************************/
        project.tasks.create(name:'myTest',group:'myself'){
            project.rootProject.subprojects.each { pro ->
                /*pro.afterEvaluate { pj ->
                    if (pj.plugins.hasPlugin("com.android.application")) {
                        println '777777777777---------======' + pj.name
                        def androidExt = pj.extensions.getByName("android") as AppExtension
                        androidExt.applicationVariants.each { variant ->
                            def javaCompileTask = variant.javaCompiler
                            int processorIndex = javaCompileTask.options.compilerArgs.indexOf('-processorpath')
                            def processor = javaCompileTask.options.compilerArgs.get(processorIndex + 1)
                            processor.split(File.pathSeparator).each { jarPath ->
                                println pro.name + "----------find compiler jar path: ${jarPath}"
                            }
                        }
                    }
                }*/
            }
        }


        /**************************/
        /*project.tasks.create(name:'compilesingle',type:JavaCompile,group:'myself'){
            doFirst{
                println '********oopppp*********='+pro.name
                Project pro = project
                def sdkDirr = pro.android.getBootClasspath()
                def classesList = new ArrayList()
                def androidExt = pro.extensions.getByName("android") as AppExtension

                //self
                File classDir = new File(pro.buildDir,'intermediates/classes/debug')
                if (!classDir.exists() ){
                    classDir = new File(pro.buildDir,'intermediates/classes/release')
                }
                classesList.add(classDir.absolutePath)

                //lib
                androidExt.applicationVariants.each { variant ->
                    if(variant.name.equals("release")){
                        return
                    }
                    println '*****************='+variant.name
                    def javaCompileTask = variant.javaCompiler
                    int processorIndex = javaCompileTask.options.compilerArgs.indexOf('-processorpath')
                    def processor = javaCompileTask.options.compilerArgs.get(processorIndex + 1)
                    processor.split(File.pathSeparator).each { jarPath ->
                        classesList.add(jarPath)
                        println pro.name + "----------find compiler jar path: ${jarPath}"
                    }
                }

                configure {
                    classpath = pro.files(pro.configurations.compile.files,sdkDirr,classesList)
                }
            }

            source = '/src/main/java'
            include "com/shobal/gradlepractise/MainActivity.java" //MyContants
            classpath = project.files(project.configurations.compile)
            destinationDir = project.file('./bin')
        }*/
    }

    def getDirectory(File file) {
        File[] flist = file.listFiles()
        if (flist == null || flist.length == 0) {
            return 0
        }
        for (File f : flist) {
            if (f.isDirectory()) {
                //这里将列出所有的文件夹
//            println("Dir==>" + f.getAbsolutePath())
                getDirectory(f)
            } else {
                //这里将列出所有的文件
                println("file==>" + f.lastModified()+";"+f.getAbsolutePath())
            }
        }
    }
}