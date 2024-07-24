package RSS.dao;

import java.io.InputStream;
import java.util.Properties;

/**
 * 此DbProperties继承自Properties   也是一个Map，键值对
 * 但增加的功能是，此ObProperties必须是单例
 */
public class ProjectProperties extends Properties {

    private static ProjectProperties instance;

    private ProjectProperties(){
        //读取配置文件
        InputStream iis = ProjectProperties.class.getClassLoader().getResourceAsStream("project.properties");
        //Properties类的load方法加载
        try {
            this.load(iis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ProjectProperties getInstance() {
        if (instance==null){
            instance = new ProjectProperties();
        }
        return instance;
    }
}
