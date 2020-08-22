##定义初始变量
        #set($tableName = $tool.append($tableInfo.name, "ServiceImpl"))
        ##设置回调
        $!callback.setFileName($tool.append($tableName, ".java"))
        $!callback.setSavePath($tool.append($tableInfo.savePath, "/service/impl"))

        ##拿到主键
        #if(!$tableInfo.pkColumn.isEmpty())
        #set($pk = $tableInfo.pkColumn.get(0))
        #end

        #if($tableInfo.savePackageName)package $!{tableInfo.savePackageName}.#{end}service.impl;

import $!{tableInfo.savePackageName}.entity.$!{tableInfo.name};
import $!{tableInfo.savePackageName}.dao.$!{tableInfo.name}Dao;
import $!{tableInfo.savePackageName}.service.$!{tableInfo.name}Service;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import javax.annotation.Resource;
import java.util.List;

/**
 * $!{tableInfo.comment}($!{tableInfo.name})表服务实现类
 *
 * @author $!author
 * @since $!time.currTime()
 */
@Service("$!tool.firstLowerCase($!{tableInfo.name})Service")
public class $!{tableName} implements $!{tableInfo.name}Service {
@Resource
private $!{tableInfo.name}Dao $!tool.firstLowerCase($!{tableInfo.name})Dao;

/**
 * 通过ID查询单条数据
 *
 * @param $!pk.name 主键
 * @return 实例对象
 */
@Override
public $!{tableInfo.name} queryById($!pk.shortType $!pk.name) {
        return this.$!{tool.firstLowerCase($!{tableInfo.name})}Dao.queryById($!pk.name);
        }

/**
 * mybatis 分页查询
 *
 * @param page mybatis 分页插件 对象
 * @param $!tool.firstLowerCase($!{tableInfo.name}) 条件查询
 * @return 分页列表数据
 */
@Override
public Page<$!{tableInfo.name}> queryAllByPage(Page<$!{tableInfo.name}> page,$!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name})){
        Page<$!{tableInfo.name}> testPage = page.setRecords(this.$!{tool.firstLowerCase($!{tableInfo.name})}Dao.queryAllByPage(page,$!tool.firstLowerCase($!{tableInfo.name})));
        return page;
        }

/**
 * 新增数据
 *
 * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
 * @return 实例对象
 */
@Override
public $!{tableInfo.name} insert($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name})) {
        this.$!{tool.firstLowerCase($!{tableInfo.name})}Dao.insert($!tool.firstLowerCase($!{tableInfo.name}));
        return $!tool.firstLowerCase($!{tableInfo.name});
        }

/**
 * 修改数据
 *
 * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
 * @return 实例对象
 */
@Override
public $!{tableInfo.name} update($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name})) {
        this.$!{tool.firstLowerCase($!{tableInfo.name})}Dao.update($!tool.firstLowerCase($!{tableInfo.name}));
        return this.queryById($!{tool.firstLowerCase($!{tableInfo.name})}.get$!tool.firstUpperCase($pk.name)());
        }

/**
 * 通过主键删除数据
 *
 * @param $!pk.name 主键
 * @return 是否成功
 */
@Override
public boolean deleteById($!pk.shortType $!pk.name) {
        return this.$!{tool.firstLowerCase($!{tableInfo.name})}Dao.deleteById($!pk.name) > 0;
        }
        }