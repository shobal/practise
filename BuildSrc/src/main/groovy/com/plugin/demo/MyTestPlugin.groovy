import com.android.build.gradle.tasks.factory.AndroidJavaCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
//import org.jetbrains.kotlin.gradle.internal.KaptWithKotlincTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinTasksProvider

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

            /*KaptWithKotlincTask kaptDebugKotlin = project.getTasks().getByName('kaptDebugKotlin')

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
            }*/

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
        project.tasks.create(name: 'compileKotlinsingle', group: 'myself') {
            doFirst {
                def classePathList = new ArrayList()
                project.getTasks().all {
                    if (it.name.equals('compileDebugKotlin')) {
                        KotlinCompile ktask = it
                        ktask.getClasspath().each { File file ->
                            classePathList.add(file.absolutePath)
                        }
                    }
                }
                File classDir = new File(project.buildDir, 'intermediates/classes/debug')
                File oldKotlinClassDir = new File(project.getBuildDir(), 'tmp/kotlin-classes/debug')
                classePathList.add(oldKotlinClassDir)
                classePathList.add(classDir)

                //1.2.50
                KotlinTasksProvider kotlinTasksProvider = Class.forName(KotlinTasksProvider.getName()).newInstance()
                KotlinCompile ktc = kotlinTasksProvider.createKotlinJVMTask(project,'ktComile','oox')

                //1.2.71
                /*Class cls = Class.forName(KotlinTasksProvider.getName())
                Constructor ct  = cls.getDeclaredConstructor(String.class)
                KotlinTasksProvider kotlinTasksProvider = ct.newInstance('mytestow')
                KotlinCompilation kotlinCompilation = null
                KotlinCompile ktc = kotlinTasksProvider.createKotlinJVMTask(project,'ktComile',kotlinCompilation)*/

                ktc.kotlinOptions.noJdk = true
                ktc.kotlinOptions.noReflect = true

                List<File> javaSourceRoots = new ArrayList<>()
                javaSourceRoots.add(new File(project.getProjectDir(), '/src/main/java'))
                ktc.source(javaSourceRoots)
                /* K2JVMCompilerArguments args = ktc.createCompilerArgs()
                 ktc.setupCompilerArgs(args,true)
                 ktc.setupPlugins(args)*/
                //ktc.setupCompilerArgs()
                //ktc.setupPlugins()
                //kapt KaptGenerateStubsTask ,
/*                List<String> freeCompilerArgs = new ArrayList<>()
                freeCompilerArgs.addAll(ktc.kotlinOptions.freeCompilerArgs)
                freeCompilerArgs.add('-Xplugin=D:\\devTools\\gradle-2.14.1-all\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-annotation-processing-gradle\\1.2.50\\807f7d867857a69a010455c9f67767055f54351\\kotlin-annotation-processing-gradle-1.2.50.jar,D:\\devTools\\gradle-2.14.1-all\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-android-extensions\\1.2.50\\a5309d96fd097320a75947d2e9673a86c948f605\\kotlin-android-extensions-1.2.50.jar')
                freeCompilerArgs.add('-P')
                freeCompilerArgs.add('plugin:org.jetbrains.kotlin.kapt3:configuration=rO0ABXoAAAQAAAAACwAHYXB0TW9kZQAAAAEABXN0dWJzAAdzb3VyY2VzAAAAAQBMRDpcZ2l0X3Byb2plY3Rcc2hvYmFsdHV0b3JpYWxccHJhY3Rpc2VcYXBwXGJ1aWxkXGdlbmVyYXRlZFxzb3VyY2Vca2FwdFxkZWJ1ZwAHY2xhc3NlcwAAAAEASEQ6XGdpdF9wcm9qZWN0XHNob2JhbHR1dG9yaWFsXHByYWN0aXNlXGFwcFxidWlsZFx0bXBca2FwdDNcY2xhc3Nlc1xkZWJ1ZwAPaW5jcmVtZW50YWxEYXRhAAAAAQBQRDpcZ2l0X3Byb2plY3Rcc2hvYmFsdHV0b3JpYWxccHJhY3Rpc2VcYXBwXGJ1aWxkXHRtcFxrYXB0M1xpbmNyZW1lbnRhbERhdGFcZGVidWcACWFwb3B0aW9ucwAAAAEAvHJPMEFCWGVHQUFBQUF3QURhMlY1QUFWMllXeDFaUUFDZEhBQUJXUm1jMlJtQUJWcllYQjBMbXR2ZEd4cGJpNW5aVzVsY21GMFpXUUFVa1E2WEdkcGRGOXdjbTlxWldOMFhITm9iMkpoYkhSMWRHOXlhV0ZzWEhCeVlXTjBhWE5sWEdGd2NGeGlkV2xzWkZ4blpXNWxjbUYwWldSY2MyOTFjbU5sWEd0aGNIUkxiM1JzYVc1Y1pHVmlkV2M9AA5qYXZhY0FyZ3VtZW50cwAAAAEAEHJPMEFCWGNFQUFBQUFBPT0AEHVzZUxpZ2h0QW5hbHlzaXMAAAABAAR0cnVlABFjb3JyZWN0RXJyb3JUeXBlcwAAAAEABWZhbHNlABZtYXBEaWFnbm9zdGljTG9jYXRpb25zAAAAAQAFZmFsc2UABXN0dWJzAAAAAQBGRDpcZ2l0X3Byb2plY3Rcc2hvYmFsdHV0b3JpYWxccHJhY3Rpc2VcYXBwXGJ1aWxkXHRtcFxrYXB0M1xzdHVic1xkZWJ1ZwALYXBjbGFzc3BhdGgAAAAMAM9EOlxkZXZUb29sc1xncmFkbGUtMi4xNC4xLWFsbFxjYWNoZXNcbW9kdWxlcy0yXGZpbGVzLTIuMVxvcmcuamV0YnJhaW5zLmtvdGxpblxrb3RsaW4tYW5ub3RhdGlvbi1wcm9jZXNzaW5nLWdyYWRsZVwxLjIuNTBcODA3ZjdkODY3ODU3YTY5YTAxMDQ1NWM5ZjY3NzY3MDU1ZjU0MzUxXGtvdGxpbi1hbm5vdGF0aW9uLXByb2Nlc3NpbmctZ3JhZGxlLTEuMi41MC5qYXIAU0Q6XGdpdF9wcm9qZWN0XHNob2JhbHR1dG9yaWFsXHByYWN0aXNlXENvbXBpbGVoYW5kbGVyXGJ1aWxkXGxpYnNcQ29tcGlsZXoAAAQAaGFuZGxlci5qYXIAvkQ6XGRldlRvb2xzXGdyYWRsZS0yLjE0LjEtYWxsXGNhY2hlc1xtb2R1bGVzLTJcZmlsZXMtMi4xXG9yZy5qZXRicmFpbnMua290bGluXGtvdGxpbi1jb21waWxlci1lbWJlZGRhYmxlXDEuMi41MFw2YmYwOTZkN2EyMDFiODczNDU4M2IyNWQ5MGRjM2E3MGZhNzY4NjI4XGtvdGxpbi1jb21waWxlci1lbWJlZGRhYmxlLTEuMi41MC5qYXIApEQ6XGRldlRvb2xzXGdyYWRsZS0yLjE0LjEtYWxsXGNhY2hlc1xtb2R1bGVzLTJcZmlsZXMtMi4xXG9yZy5qZXRicmFpbnMua290bGluXGtvdGxpbi1zdGRsaWJcMS4yLjUwXDY2ZDQ3YjAwNGM1YjhhMWQyZDFkZjllNDYzMTg3MzkwZWQ3NDEzMTZca290bGluLXN0ZGxpYi0xLjIuNTAuamFyAKdEOlxkZXZUb29sc1xncmFkbGUtMi4xNC4xLWFsbFxjYWNoZXNcbW9kdWxlcy0yXGZpbGVzLTIuMVxjb20uZ29vZ2xlLmF1dG8uc2VydmljZVxhdXRvLXNlcnZpY2VcMS4wLXJjMlw1MTAzM2E1YjhmY2Y3MDM5MTU5ZTM1YjY4NzhmMTA2Y2NkNWZiMzVmXGF1dG8tc2VydmljZS0xLjAtcmMyLmphcgCSRDpcZGV2VG9vbHNcZ3JhZGxlLTIuMTQuMS1hbGxcY2FjaGVzXG1vZHVsZXMtMlxmaWxlcy0yLjFcY29tLnNxdWFyZXVwXGphdmFwb2V0XDEuMTAuMFw3MTJjMTc4ZDM1MTg1ZDgyNjEyOTU5MTNjOWYyYTdkNjg2N2E2MDA3XGphdmFwb2V0LTEuMTAuMC5qYXIAS0Q6XGdpdF9wcm9qZWN0XHNob2JhbHR1dG9yaWFsXHByYWN0aXNlXGFubm90YXRpb21cYnVpbGRcbGlic1xhbm5vdGF0aW9tLmphcgCyRDpcZGV2VG9vbHNcZ3JhZGxlLTIuMTQuMS1hbGxcY2FjaGVzXG1vZHVsZXMtMlxmaWxlcy0yLjFcb3JnLmpldGJyYWlucy5rb3RsaW5ca290bGluLXN0ZGxpYi1jb21tb25cMS4yLjUwXDZiMTlhMmZjYzI5ZDM0ODc4YjNhYWIzM2ZkNWZjZjcwNDU4YTczZGZca290bGluLXN0ZGxpYi1jb21tb24tMS4yLjUwLmphcgBvQzpcUHJvZ3JhbSBGaWxlc1xBbmRyb2lkXEFuZHJvaWQgU3R1ZGlvXGdyYWRsZVxtMnJlcG9zaXRvcnlcb3JnXGpldGJyYWluc1xhbm5vdHoAAAHVYXRpb25zXDEzLjBcYW5ub3RhdGlvbnMtMTMuMC5qYXIAtEQ6XGRldlRvb2xzXGdyYWRsZS0yLjE0LjEtYWxsXGNhY2hlc1xtb2R1bGVzLTJcZmlsZXMtMi4xXG9yZy5qZXRicmFpbnMua290bGluXGtvdGxpbi1zY3JpcHQtcnVudGltZVwxLjIuNTBcZjdmNWY5YWUwMzQ0YzJkMTRjYTQxY2E3NDBkMTcxNDNiYTJlZDNiY1xrb3RsaW4tc2NyaXB0LXJ1bnRpbWUtMS4yLjUwLmphcgCVRDpcZGV2VG9vbHNcZ3JhZGxlLTIuMTQuMS1hbGxcY2FjaGVzXG1vZHVsZXMtMlxmaWxlcy0yLjFcY29tLmdvb2dsZS5hdXRvXGF1dG8tY29tbW9uXDAuM1w0MDczYWIxNmFiNGFjZWI5YTIxNzI3M2RhNjQ0MjE2NmJmNTFhZTE2XGF1dG8tY29tbW9uLTAuMy5qYXIAZkM6XFByb2dyYW0gRmlsZXNcQW5kcm9pZFxBbmRyb2lkIFN0dWRpb1xncmFkbGVcbTJyZXBvc2l0b3J5XGNvbVxnb29nbGVcZ3VhdmFcZ3VhdmFcMTguMFxndWF2YS0xOC4wLmphcg==,plugin:org.jetbrains.kotlin.android:configuration=rO0ABXeMAAAAAwAIZmVhdHVyZXMAAAABAAV2aWV3cwAHcGFja2FnZQAAAAEAGWNvbS5zaG9iYWwuZ3JhZGxlcHJhY3Rpc2UAB3ZhcmlhbnQAAAABADxtYWluO0Q6XGdpdF9wcm9qZWN0XHNob2JhbHR1dG9yaWFsXHByYWN0aXNlXGFwcFxzcmNcbWFpblxyZXM=')
                ktc.kotlinOptions.freeCompilerArgs = freeCompilerArgs*/

                ktc.setDestinationDir(project.file('./bin'))
//        ktc.setClasspath(project.files())
                ktc.setClasspath(project.files(classePathList))
                List<String> includes = new ArrayList<>()
                includes.add("com/shobal/gradlepractise/KtTestt.kt")
//        includes.add("com/shobal/gradlepractise/MyTest.kt")
                includes.add("com/shobal/gradlepractise/MyContants.java")

                ktc.include(includes)
                ktc.execute()
            }
        }


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