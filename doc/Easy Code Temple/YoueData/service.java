##定义初始变量
        #set($tableName = $tool.append($tableInfo.name, "Service"))
        ##设置回调
        $!callback.setFileName($tool.append($tableName, ".java"))
        $!callback.setSavePath($tool.append($tableInfo.savePath, "/service"))

        ##拿到主键
        #if(!$tableInfo.pkColumn.isEmpty())
        #set($pk = $tableInfo.pkColumn.get(0))
        #end

        #if($tableInfo.savePackageName)package $!{tableInfo.savePackageName}.#{end}service;

import $!{tableInfo.savePackageName}.entity.$!{tableInfo.name};
import java.util.List;
import com.baomidou.mybatisplus.plugins.Page;
/**
 * $!{tableInfo.comment}($!{tableInfo.name})表服务接口
 *
 * @author $!author
 * @since $!time.currTime()
 */
public interface $!{tableName} {

        /**
         * 通过ID查询单条数据
         *
         * @param $!pk.name 主键
         * @return 实例对象
         */
        $!{tableInfo.name} queryById($!pk.shortType $!pk.name);

        /**
         * mybatis 分页查询
         *
         * @param page mybatis 分页插件 对象
         * @param $!tool.firstLowerCase($!{tableInfo.name}) 条件查询
         * @return 分页列表数据
         */
        Page<$!{tableInfo.name}> queryAllByPage(Page<$!{tableInfo.name}> page,$!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 新增数据
         *
         * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
         * @return 实例对象
         */
        $!{tableInfo.name} insert($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 修改数据
         *
         * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
         * @return 实例对象
         */
        $!{tableInfo.name} update($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 通过主键删除数据
         *
         * @param $!pk.name 主键
         * @return 是否成功
         */
        boolean deleteById($!pk.shortType $!pk.name);

        }