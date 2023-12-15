package space.amolchavan.entities;

import space.amolchavan.entities.app.*;
import com.creditdatamw.zerocell.Reader;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter @Setter
public class DTO {
    String testDataPath,configPath;

    List<User> users =null;

    public DTO(){
        testDataPath = System.getProperty("user.dir") + "/src/test/resources/test-data/";
        configPath = System.getProperty("user.dir") + "/src/test/resources/test-config/";
    }

    /**
     * This method accepts two parameters and create static DTO object that can be used anywhere in the test
     * @param: env - example test/staging/prod
     * @param: parameter it could be moduleName or appName eg. eic/mediaplugin/sdp
     * @param : tenant 
     * @author: Amol Chavan*/
    public void createTestData(String env, String parameter,String tenant) {
        parameter = parameter.toLowerCase();
        env = env.toLowerCase();
        tenant = tenant.toLowerCase();
       if(parameter.equals("app")){
            this.users = Reader.of(User.class).from(new File(testDataPath + parameter + "-" + env + "-" + tenant +".xlsx")).sheet("users").skipHeaderRow(true).list();
        }
    }
}
