package RSS.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonInformationItem implements Serializable {
    private String title;
    private String txtDate; //xml中得到的String日期
    private Date date;  //转为Date日期
    private String link;//item的link
    private StringBuffer description;   //item的描述
    private String id;
    private String source;//RSS源的名称

    public void addDescription(String txt)
    {
        if(this.description == null)
        {
            this.description = new StringBuffer();
        }
        this.description.append(txt);
    }

    /**
     * 生成此条item在磁盘上的文件名
     * @return  source_描述的hashcode.xml
     */
    public String getFileName()
    {
        StringWriter writer = new StringWriter();
        writer.append(source);
        writer.append("_");
        writer.append(String.valueOf((date.toString()+description).hashCode()));
        writer.append(".xml");
        return writer.toString();
    }
}
