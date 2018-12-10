import com.android.build.gradle.tasks.factory.AndroidJavaCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.jetbrains.kotlin.gradle.internal.KaptWithKotlincTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class MyTestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
//        gradleDoSomething(project)
//        gradleDoSomething2(project)
//        checkKaptDebugDir(project)
        createTasks(project)
    }

    void gradleDoSomething2(Project project) {
        project.gradle.taskGraph.whenReady {
            project.getTasks().all {
                println('task Name=' + it.name + ',class name==' + it.getClass())
            }
        }
    }

    void checkKaptDebugDir(Project project) {
        project.gradle.taskGraph.whenReady {
            project.getTasks().all { Task tk ->
                /*tk.doFirst {
                    File kapt =  new File(project.getBuildDir(),'generated/source/kapt/debug/com')
                    println('-----kapt-----start '+tk.name+', '+kapt.exists())
                }
                tk.doLast {
                    File kapt =  new File(project.getBuildDir(),'generated/source/kapt/debug/com')
                    println('-----kapt-----end '+tk.name +', '+kapt.exists())
                }*/
                println('task Name=' + tk.name + ',class name==' + tk.getClass())
            }
        }
    }

    void gradleDoSomething(Project project) {
        project.gradle.taskGraph.whenReady {
            def getFileList
            getFileList = { file, filelist ->
                file.listFiles().each { x ->
                    x.isDirectory() ? getFileList(x, filelist) : filelist.add(x)
                }
            }
            File incrementdirtmp = new File(project.getBuildDir(), 'incrementdir')
            if (incrementdirtmp.exists()) {
                incrementdirtmp.deleteDir()
            }

            KaptWithKotlincTask kaptDebugKotlin = project.getTasks().getByName('kaptDebugKotlin')

            kaptDebugKotlin.doLast {
                //
                KotlinCompile androidKotlinCompile = project.getTasks().getByName('compileDebugKotlin')
                File incrementdir = new File(project.getBuildDir(), 'incrementdir/kotlin')
                if (!incrementdir.exists()) {
                    incrementdir.mkdirs()
                }
                File oldDes = androidKotlinCompile.getDestinationDir()
                androidKotlinCompile.setDestinationDir(incrementdir)

                //
                File oldClassDir = new File(project.getBuildDir(), 'intermediates/classes/debug')
                File oldKotlinClassDir = new File(project.getBuildDir(), 'tmp/kotlin-classes/debug')
                FileCollection oldClassPath = androidKotlinCompile.getClasspath()
                FileCollection fc = project.files(oldClassPath, oldClassDir, oldKotlinClassDir)
                androidKotlinCompile.setClasspath(fc)

                //
                ArrayList<File> javaFileTree = new ArrayList<>();
                androidKotlinCompile.getSource().each { File file ->
                    if (file.name.contains('KtTestt')) {
                        javaFileTree.add(file)
                    }
                }

                androidKotlinCompile.setSource(project.files(javaFileTree))
                androidKotlinCompile.getSource().each { File file ->
                    println('androidKotlinCompile srouce===------:' + file.absolutePath)
                }

                androidKotlinCompile.doLast {
                    project.copy {
                        from incrementdir
                        into oldDes
                    }
                    setDestinationDir(oldDes)
                    setClasspath(oldClassPath)
                }

                //
                AndroidJavaCompile androidJavaCompile = project.getTasks().getByName('compileDebugJavaWithJavac')
                File javaincrementdir = new File(project.getBuildDir(), 'incrementdir/java')
                if (!javaincrementdir.exists()) {
                    javaincrementdir.mkdirs()
                }
                File javaoldDes = androidJavaCompile.getDestinationDir()
                androidJavaCompile.setDestinationDir(javaincrementdir)

                //
                FileCollection javaoldClassPath = androidJavaCompile.getClasspath()
                FileCollection fc1 = project.files(javaoldClassPath, oldClassDir, oldKotlinClassDir)
                androidJavaCompile.setClasspath(fc1)
                fc1.each {
                    println('00oo======>' + it.absolutePath)
                }

                //
                ArrayList<File> javaFileTree2 = new ArrayList<>()
                androidJavaCompile.getSource().each { File file ->
                    if (file.name.contains('MainActivity')) {
                        javaFileTree2.add(file)
                    }
                }
                File javakaptDir = new File(project.getBuildDir(), 'generated/source/kapt/debug')
                getFileList(javakaptDir, javaFileTree2)
                androidJavaCompile.setSource(project.files(javaFileTree2))
                androidJavaCompile.getSource().each { File file ->
                    println('srouce===------:' + file.absolutePath)
                }

                androidJavaCompile.doLast {
                    project.copy {
                        from javaincrementdir
                        into javaoldDes
                    }
                    setDestinationDir(javaoldDes)
                    setClasspath(javaoldClassPath)
                }
            }

/*            project.getTasks().all {
//                println('task Name='+it.name+',class name=='+it.getClass())
                if (it.name.equals('kaptDebugKotlin')){
                    it.doLast {
                        project.getTasks().all{

                        }
                    }
                }

                if (it.name.equals('compileDebugKotlin')){
                    //
                    KotlinCompile androidKotlinCompile = it
                    File incrementdir =  new File(project.getBuildDir(),'incrementdir/kotlin')
                    if (!incrementdir.exists()){
                        incrementdir.mkdirs()
                    }
                    File oldDes = androidKotlinCompile.getDestinationDir()
                    androidKotlinCompile.setDestinationDir(incrementdir)

                    //
                    File oldClassDir = new File(project.getBuildDir(),'intermediates/classes/debug')
                    File oldKotlinClassDir = new File(project.getBuildDir(),'tmp/kotlin-classes/debug')
                    FileCollection oldClassPath = androidKotlinCompile.getClasspath()
                    FileCollection fc = project.files(oldClassPath,oldClassDir,oldKotlinClassDir)
                    androidKotlinCompile.setClasspath(fc)

                    //
                    ArrayList<File> javaFileTree = new ArrayList<>();
                    androidKotlinCompile.getSource().each { File file->
                        if (file.name.contains('KtTestt')){
                            javaFileTree.add(file)
                        }
                    }
                    File kaptDir = new File(project.getBuildDir(),'generated/source/kapt/debug')
                    getFileList(kaptDir,javaFileTree)
                    androidKotlinCompile.setSource(project.files(javaFileTree))
                    androidKotlinCompile.getSource().each { File file->
                        println('androidKotlinCompile srouce===------:'+file.absolutePath)
                    }

                    androidKotlinCompile.doLast {
                        project.copy {
                            from incrementdir
                            into oldDes
                        }
                        setDestinationDir(oldDes)
                        setClasspath(oldClassPath)
                    }

                }else if (it.name.equals('compileDebugJavaWithJavac')){

                    //
                    AndroidJavaCompile androidJavaCompile = it
                    File incrementdir =  new File(project.getBuildDir(),'incrementdir/java')
                    if (!incrementdir.exists()){
                        incrementdir.mkdirs()
                    }
                    File oldDes = androidJavaCompile.getDestinationDir()
                    androidJavaCompile.setDestinationDir(incrementdir)

                    //
                    File oldClassDir = new File(project.getBuildDir(),'intermediates/classes/debug')
                    File oldKotlinClassDir = new File(project.getBuildDir(),'tmp/kotlin-classes/debug')
                    FileCollection oldClassPath = androidJavaCompile.getClasspath()
                    FileCollection fc = project.files(oldClassPath,oldClassDir,oldKotlinClassDir)
                    androidJavaCompile.setClasspath(fc)

                    //
                    ArrayList<File> javaFileTree = new ArrayList<>()
                    androidJavaCompile.getSource().each { File file->
                        if (file.name.contains('MainActivity')){
                            javaFileTree.add(file)
                        }
                    }
                    File kaptDir = new File(project.getBuildDir(),'generated/source/kapt/debug')
                    getFileList(kaptDir,javaFileTree)
                    androidJavaCompile.setSource(project.files(javaFileTree))
                    androidJavaCompile.getSource().each { File file->
                        println('srouce===------:'+file.absolutePath)
                    }

                    androidJavaCompile.doLast {
                        project.copy {
                            from incrementdir
                            into oldDes
                        }
                        setDestinationDir(oldDes)
                        setClasspath(oldClassPath)
                    }
                }
            }*/
        }
    }

    void createTasks(Project project) {
        project.tasks.create(name: 'listJars', group: 'myself') {
            doFirst {
                project.repositories.each {
                    println "repository: ${it.name} ('${it.url}')"
                }
//        println 'xxxxpp:'+repositories.collect { it.name }
//        project.configurations.compile.each { File file -> println 'ooxx----'+file.name }
            }
        }

        /** ************************/
        project.tasks.create(name: 'scanFileModify', group: 'myself') {
            doLast {
                project.rootProject.subprojects.each {
                    if (it.hasProperty('android')) {
                        println(it.getBuildDir().absolutePath + '==src path=' + it.android.sourceSets.main.java.srcDirs[0].absolutePath)
                        getDirectory(it.android.sourceSets.main.java.srcDirs[0])
                    }
                }
            }
        }

        /** ************************/
        project.tasks.create(name: 'myTest', group: 'myself') {
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

        /** ************************/
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
                println("file==>" + f.lastModified() + ";" + f.getAbsolutePath())
            }
        }
    }
}