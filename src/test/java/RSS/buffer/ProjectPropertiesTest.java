package RSS.buffer;

import RSS.dao.ProjectProperties;
import junit.framework.TestCase;

public class ProjectPropertiesTest extends TestCase {
    public void testGetInstance() {
        ProjectProperties properties = ProjectProperties.getInstance();
        System.out.println(properties.get("file.path"));
        assertNotNull(properties);
    }
}
