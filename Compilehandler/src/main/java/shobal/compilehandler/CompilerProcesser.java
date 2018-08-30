package shobal.compilehandler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import shobal.annotatiom.ELModules;

@AutoService(Process.class)
public class CompilerProcesser extends AbstractProcessor{
    private Filer mFileUtils;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        // 在这里打印gradle文件传进来的参数
        Map<String, String> map = processingEnv.getOptions();
        for (String key : map.keySet()) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "Printing:"+key + "：" + map.get(key));
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        initElModules(roundEnvironment);
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion(){
        return SourceVersion.latestSupported();
    }

    private void initElModules(RoundEnvironment roundEnv) {
        if (roundEnv !=null) {
            TypeSpec manager = TypeSpec.classBuilder("ELModulesFactory")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();

            JavaFile javaFile = JavaFile.builder("com.shobal", manager)
                    .build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.WARNING, "printing ,initELModuleConfigs error" + e.getMessage());
            }

            Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(ELModules.class);
//            mMessager.printMessage(Diagnostic.Kind.WARNING, "Printing: -----------------++++++++"+set.size());
            for (Element element : set) {
//                mMessager.printMessage(Diagnostic.Kind.WARNING, "Printing: -----------------++++++++="+element.getSimpleName());
                TypeElement element1 = (TypeElement) element;
                List<? extends Element> subes = element1.getEnclosedElements();
                for (Element e : subes) {
//                    mMessager.printMessage(Diagnostic.Kind.WARNING, "Printing: ----++++++++="+e.getSimpleName());
                    if (e.getSimpleName().equals("onResume")) {
                        ExecutableElement executeableElement = (ExecutableElement) e;
                        ClassName className;
                    }
                }
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(ELModules.class.getCanonicalName());
        return types;
    }
}
