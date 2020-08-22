##引入宏定义
        $!define

        ##使用宏定义设置回调（保存位置与文件后缀）
        #save("/entity", ".java")

        ##使用宏定义设置包后缀
        #setPackageSuffix("entity")

        ##使用全局变量实现默认包导入
        $!autoImport
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;
##使用宏定义实现类注释信息
        #tableComment("实体类")
@SuppressWarnings("serial")
public class $!{tableInfo.name} extends Model<$!{tableInfo.name}> {
        #foreach($column in $tableInfo.fullColumn)
        #if(${column.comment})//${column.comment}#end

private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
        #end

        #foreach($column in $tableInfo.fullColumn)
        #getSetMethod($column)
        #end

        #foreach($column in $tableInfo.pkColumn)
/**
 * 获取主键值
 *
 * @return 主键值
 */
@Override
protected Serializable pkVal() {
        return this.$!column.name;
        }
        #break
        #end
        }