package com.youedata.util.es;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ES相关操作封装工具类
 * @author jqx
 *
 */
public class EsSearchSqlUtil {

    static Logger logger = Logger.getLogger(EsSearchSqlUtil.class);

    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建索引
     * @param index
     * @return
     * @throws IOException
     */
    public boolean createIndex(String index) throws IOException {
        if (!isIndexExist(index)) {
            logger.info("index is not exits!");
        }
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.timeout(TimeValue.timeValueMinutes(2));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));

        request.settings(Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas", 1));

        //1、同步方法
        CreateIndexResponse indexresponse = client.indices().create(request, RequestOptions.DEFAULT);

        //2、异步方法不会阻塞并立即返回。
        /*ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                //如果执行成功，则调用onResponse方法;
            }
            @Override
            public void onFailure(Exception e) {
                //如果失败，则调用onFailure方法。
            }
        };
        client.indices().createAsync(request, RequestOptions.DEFAULT, listener);*/

        logger.info("success to create index " + indexresponse.isAcknowledged());

        return indexresponse.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String index) throws IOException {
        if (!isIndexExist(index)) {
            logger.info("index is not exits!");
        }
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        request.timeout(TimeValue.timeValueMinutes(2));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));

        //设置IndicesOptions去控制不能用的索引如何解决处理，下面用lenientExpandOpen进行通用的处理
        request.indicesOptions(IndicesOptions.lenientExpandOpen());

        //1、同步方法
        AcknowledgedResponse dResponse = client.indices().delete(request,RequestOptions.DEFAULT);

        //2、异步方法不会阻塞并立即返回。
        /*ActionListener<AcknowledgedResponse> listener = new ActionListener<AcknowledgedResponse>() {

			@Override
			public void onFailure(Exception arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponse(AcknowledgedResponse arg0) {
				// TODO Auto-generated method stub
			}

        };
        client.indices().deleteAsync(request, RequestOptions.DEFAULT, listener);*/

        if (dResponse.isAcknowledged()) {
            logger.info("delete index " + index + "  successfully!");
        } else {
            logger.info("fail to delete index " + index);
        }
        return dResponse.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     * @throws IOException
     */
    public boolean isIndexExist(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        request.local(false);
        request.humanReadable(true);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        if (exists) {
            logger.info("index [" + index + "] is exist!");
        } else {
            logger.info("index [" + index + "] is not exist!");
        }
        return exists;
    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param type   类型，类似表
     * @param id     数据ID
     * @param includFields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     * @throws IOException
     */
    public Map<String, Object> searchDataById(String index, String type, String id, String includFields) throws IOException {
        GetRequest request = new GetRequest(index,type, id);

        if (StringUtils.isNotEmpty(includFields)) {
            //不展示的字段
            String[] excludeFields = new String[]{};

            //查询特定字段,包含和不包含的字段
            FetchSourceContext fetchSourceContext =new FetchSourceContext(true, includFields.split(","),excludeFields );
            request.fetchSourceContext(fetchSourceContext);

            //为特定的存储字段配置检索(要求字段在映射中单独存储)
            //request.storedFields(storeFields);
        }
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 使用分词查询
     *
     * @param index    索引名称
     * @param type     类型名称,可传入多个type逗号分隔
     * @param fields   需要显示的字段，逗号分隔（缺省为全部字段）
     * @param matchStr 过滤条件（xxx=111,aaa=222）
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> searchListData(String index, String type, String fields, String matchStr) throws IOException {
        return searchListData(index, type, 0, 0, null, fields, null, false, null, matchStr);
    }

    /**
     * 使用分词查询
     *
     * @param index       索引名称
     * @param type        类型名称,可传入多个type逗号分隔
     * @param fields      需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField   排序字段
     * @param matchPhrase true 使用，短语精准匹配
     * @param matchStr    过滤条件（xxx=111,aaa=222）
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> searchListData(String index, String type, String fields, String sortField, boolean matchPhrase, String matchStr) throws IOException {
        return searchListData(index, type, 0, 0, null, fields, sortField, matchPhrase, null, matchStr);
    }


    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightField 高亮字段
     * @param matchStr       过滤条件（xxx=111,aaa=222）
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> searchListData(String index, String type, Integer size, String fields, String sortField, boolean matchPhrase, String highlightField, String matchStr) throws IOException {
        return searchListData(index, type, 0, 0, size, fields, sortField, matchPhrase, highlightField, matchStr);
    }


    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightField 高亮字段
     * @param matchStr       过滤条件（xxx=111,aaa=222）
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> searchListData(String index, String type, long startTime, long endTime, Integer size, String fields, String sortField, boolean matchPhrase, String highlightField, String matchStr) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();
        if (StringUtils.isNotEmpty(type)) {
            request.types(type.split(","));
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("processTime")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }

        //搜索的的字段
        if (StringUtils.isNotEmpty(matchStr)) {
            for (String s : matchStr.split(",")) {
                String[] ss = s.split("=");
                if (ss.length > 1) {
                    if (matchPhrase == Boolean.TRUE) {
                        boolQuery.must(QueryBuilders.matchPhraseQuery(s.split("=")[0], s.split("=")[1]));
                    } else {
                        boolQuery.must(QueryBuilders.matchQuery(s.split("=")[0], s.split("=")[1]));
                    }
                }
            }
        }

        // 高亮（xxx=111,aaa=222）
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.query(boolQuery);
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.fetchSource(fields.split(","), null);
        }
        searchRequestBuilder.fetchSource(true);

        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.sort(sortField, SortOrder.DESC);
        }

        if (size != null && size > 0) {
            searchRequestBuilder.size(size);
        }

        request.source(searchRequestBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        System.out.println("totalHits:   "+totalHits+"    length:  "+length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }
        return null;
    }

    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param currentPage    当前页
     * @param pageSize       每页显示条数
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightField 高亮字段
     * @param matchStr       过滤条件（xxx=111,aaa=222）
     * @return
     * @throws IOException
     */
    public EsDataPage searchDataPage(String index, String type, int currentPage, int pageSize, long startTime, long endTime, String fields, String sortField, boolean matchPhrase, String highlightField, String matchStr) throws IOException {
        //SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();
        if (StringUtils.isNotEmpty(type)) {
            request.types(type.split(","));
        }
        request.searchType(SearchType.QUERY_THEN_FETCH);
        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.fetchSource(fields.split(","), null);
        }

        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.sort(sortField, SortOrder.DESC);
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("@timestamp")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }

        // 查询字段
        if (StringUtils.isNotEmpty(matchStr)) {
            for (String s : matchStr.split(",")) {
                String[] ss = s.split("=");
                if (ss.length > 1) {
                    if (matchPhrase == Boolean.TRUE) {
                        boolQuery.must(QueryBuilders.matchPhraseQuery(s.split("=")[0], s.split("=")[1]));
                    } else {
                        boolQuery.must(QueryBuilders.matchQuery(s.split("=")[0], s.split("=")[1]));
                    }
                }
            }
        }

        // 高亮（xxx=111,aaa=222）
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.query(QueryBuilders.matchAllQuery());
        searchRequestBuilder.query(boolQuery);

        // 分页应用
        searchRequestBuilder.from(currentPage).size(pageSize);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.explain(true);

        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
//        logger.info("\n{}", searchRequestBuilder);

        // 执行搜索,返回搜索响应信息
        request.source(searchRequestBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

//        logger.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);

            return new EsDataPage(currentPage, pageSize, (int) totalHits, sourceList);
        }
        return null;
    }

    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());
            if (StringUtils.isNotEmpty(highlightField)) {

                System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();

                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }
}
