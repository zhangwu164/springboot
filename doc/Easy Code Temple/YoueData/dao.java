##定义初始变量
        #set($tableName = $tool.append($tableInfo.name, "Dao"))
        ##设置回调
        $!callback.setFileName($tool.append($tableName, ".java"))
        $!callback.setSavePath($tool.append($tableInfo.savePath, "/dao"))

        ##拿到主键
        #if(!$tableInfo.pkColumn.isEmpty())
        #set($pk = $tableInfo.pkColumn.get(0))
        #end

        #if($tableInfo.savePackageName)package $!{tableInfo.savePackageName}.#{end}dao;

import $!{tableInfo.savePackageName}.entity.$!{tableInfo.name};
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
/**
 * $!{tableInfo.comment}($!{tableInfo.name})表数据库访问层
 *
 * @author $!author
 * @since $!time.currTime()
 */
@Mapper
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
        List<$!{tableInfo.name}> queryAllByPage(Pagination page, $!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 通过实体作为筛选条件查询
         *
         * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
         * @return 对象列表
         */
        List<$!{tableInfo.name}> queryAll($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 新增数据
         *
         * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
         * @return 影响行数
         */
        int insert($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 修改数据
         *
         * @param $!tool.firstLowerCase($!{tableInfo.name}) 实例对象
         * @return 影响行数
         */
        int update($!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name}));

        /**
         * 通过主键删除数据
         *
         * @param $!pk.name 主键
         * @return 影响行数
         */
        int deleteById($!pk.shortType $!pk.name);

        }