package com.plugin.demo

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtMethod

class MyInject {
    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "System.out.println(\"I Love ====\" ); ";

    public static void addOneLineCode(){

    }

    public static void injectDir(String path, String packageName,String modifyClassName,String androidClassPath) {
        pool.appendClassPath(path)
        pool.appendClassPath(androidClassPath)
        println '---------=======filepath='+path
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                println '---------=======filepath='+filePath+',needModifyCn='+modifyClassName
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    // 判断当前目录是否是在我们的应用包里面
                    boolean isMyPackage = true;
                    if (isMyPackage) {
                        String[] str= filePath.split('/')
                        String className = str[str.length-1].replace('.class','')
                        println '---------=======filepath='+filePath+',className='+className
                        if (!modifyClassName.equalsIgnoreCase(className))
                            return ;

                        //开始修改class文件
                        try{
                            CtClass cc = pool.getCtClass(packageName+'.'+modifyClassName)
                            if (cc.isFrozen())
                                cc.defrost()
                            CtMethod m = cc.getDeclaredMethod("onResume")
                            m.insertBefore('android.widget.Toast.makeText(this,"sdfsdfgge",android.widget.Toast.LENGTH_LONG).show();')
                            cc.writeFile(path)
                            cc.detach()
                            println '---------=======add toast===='+packageName+'.'+modifyClassName+',method='+m
                        }catch (Throwable throwable){
                            println '-----------======error='+throwable.getMessage()
                        }

                        //开始修改class文件
                        /*CtClass c = pool.getCtClass(className)

                        if (c.isFrozen()) {
                            c.defrost()
                        }

                        CtConstructor[] cts = c.getDeclaredConstructors()
                        if (cts == null || cts.length == 0) {
                            //手动创建一个构造函数
                            CtConstructor constructor = new CtConstructor(new CtClass[0], c)
                            constructor.insertBeforeBody(injectStr)
                            c.addConstructor(constructor)
                        } else {
                            cts[0].insertBeforeBody(injectStr)
                        }
                        c.writeFile(path)
                        c.detach()*/
                    }
                }
            }
        }
    }
}