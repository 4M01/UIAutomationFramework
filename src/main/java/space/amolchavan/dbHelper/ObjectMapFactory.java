package space.amolchavan.dbHelper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapFactory {
    private static Map<String, Object> objectMap = null;
    private static ApplicationContext context = null;
    private static ObjectMapFactory instance;

    private  ObjectMapFactory(String[] contextPaths){
        context = new FileSystemXmlApplicationContext(contextPaths);
        objectMap = new HashMap<String, Object>();
        objectMap.putAll(context.getBeansOfType(Object.class));
    }

    private static Map<String, Object> getObjectMap(){
        return objectMap;
    }

    public static Map<String, Object> getAppContextObjectMap(){
        if(instance == null){
            File testDir = new File(System.getProperty("user.dir")+"/src/main");
            System.out.println("Absolute Path: " +testDir.getAbsolutePath() +" exist ? " + testDir.exists());

            if(testDir.exists())
                instance = new ObjectMapFactory(new String[] {"/src/main/resources/app-context.xml"});
//            else if(System.getProperty("user.dir").contains("target"))
//                instance = new ObjectMapFactory(new String[] {"test-classes/spring-config/app-context.xml"});
//            else
//                instance = new ObjectMapFactory(new String[] {"target/test-classes/spring-config/app-context.xml"});
        }
        return ObjectMapFactory.getObjectMap();
    }
}
