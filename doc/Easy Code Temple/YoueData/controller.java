##定义初始变量
        #set($tableName = $tool.append($tableInfo.name, "Controller"))
        ##设置回调
        $!callback.setFileName($tool.append($tableName, ".java"))
        $!callback.setSavePath($tool.append($tableInfo.savePath, "/controller"))
        ##拿到主键
        #if(!$tableInfo.pkColumn.isEmpty())
        #set($pk = $tableInfo.pkColumn.get(0))
        #end

        #if($tableInfo.savePackageName)package $!{tableInfo.savePackageName}.#{end}controller;

import $!{tableInfo.savePackageName}.entity.$!{tableInfo.name};
import $!{tableInfo.savePackageName}.service.$!{tableInfo.name}Service;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.youedata.common.tips.Tip;
import com.youedata.common.tips.ErrorTip;
import com.youedata.common.tips.SuccessTip;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.http.HttpMessage;
import io.swagger.annotations.*;
/**
 * $!{tableInfo.comment}($!{tableInfo.name})表控制层
 *
 * @author $!author
 * @since $!time.currTime()
 */
@Api(value = "$!tool.firstLowerCase($tableInfo.name)", description = "$!tool.firstLowerCase($tableInfo.name)服务", position = 100, protocols = "http")
@RestController
@RequestMapping("/api/v1/$!tool.firstLowerCase($tableInfo.name)")
public class $!{tableName} {
/**
 * 服务对象
 */
@Resource
private $!{tableInfo.name}Service $!tool.firstLowerCase($tableInfo.name)Service;

/**
 * 通过主键查询单条数据
 *
 * @param id 主键
 * @return 单条数据
 */
@ApiOperation(value="查询单条数据", notes="查询单条数据")
@ApiResponses({
        @ApiResponse(code = 200, message = "查询单条数据")
})
@GetMapping("/selectOne")
public Tip selectOne($!pk.shortType $!pk.name) {
        try {
        return SuccessTip.success(this.$!{tool.firstLowerCase($tableInfo.name)}Service.queryById($!pk.name));
        }catch (Exception e){
        e.printStackTrace();
        return ErrorTip.sqlError(e.getMessage());
        }
        }

@ApiOperation(value = "任务分页列表", notes = "任务分页列表", response = HttpMessage.class)
@GetMapping("/listPage")
public Tip listPage(
@ApiParam(value = "当前页码", defaultValue = "1") @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
@ApiParam(value = "每页条数", defaultValue = "10") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
        $!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name})){

        Page page=new Page(pageNum,pageSize);
        try {
        page =this.$!{tool.firstLowerCase($tableInfo.name)}Service.queryAllByPage(page,$!tool.firstLowerCase($!{tableInfo.name})) ;
        return SuccessTip.success(page);
        }catch (Exception e){
        e.printStackTrace();
        return ErrorTip.sqlError(e.getMessage());
        }
        }

@ApiOperation(value="$!tool.firstLowerCase($!{tableInfo.name}) 更改", notes="$!tool.firstLowerCase($!{tableInfo.name}) 更改", httpMethod = "POST")
@PostMapping(value = "/update")
public Tip update(@RequestBody $!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name})){
        try {
        return SuccessTip.success(this.$!{tool.firstLowerCase($tableInfo.name)}Service.update($!tool.firstLowerCase($!{tableInfo.name})));
        }catch (Exception e){
        e.printStackTrace();
        return ErrorTip.sqlError(e.getMessage());
        }
        }

@ApiOperation(value="保存$!tool.firstLowerCase($!{tableInfo.name})", notes="保存", httpMethod = "POST")
@PostMapping(value = "/save")
public Tip save(@RequestBody $!{tableInfo.name} $!tool.firstLowerCase($!{tableInfo.name})){
        try {
        return SuccessTip.success(this.$!{tool.firstLowerCase($tableInfo.name)}Service.insert($!tool.firstLowerCase($!{tableInfo.name})));
        }catch (Exception e){
        e.printStackTrace();
        return ErrorTip.sqlError(e.getMessage());
        }
        }


@ApiOperation(value="delete $!tool.firstLowerCase($!{tableInfo.name})", notes="删除", httpMethod = "POST")
@PostMapping(value = "/delete")
public Tip delete(@RequestBody $!pk.shortType $!pk.name){
        try {
        Boolean bool = this.$!{tool.firstLowerCase($tableInfo.name)}Service.deleteById($!pk.name);
        if(bool){
        return SuccessTip.success();
        }else{
        return ErrorTip.error();
        }

        }catch (Exception e){
        e.printStackTrace();
        return ErrorTip.sqlError(e.getMessage());
        }
        }

        }