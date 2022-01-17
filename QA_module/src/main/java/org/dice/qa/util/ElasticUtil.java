package org.dice.qa.util;

import org.apache.http.HttpHost;
import org.dice.qa.constants.AppConstants;
import org.dice.qa.models.Triple;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Component
public class ElasticUtil {
    @Value("${elastic.hostname}")
    private String elasticHost;

    @Value("${elastic.port}")
    private int elasticPort;

    @Value("${elastic.scheme}")
    private String elasticScheme;

    @Value("${elastic.resourceindex}")
    private String entityIndex;

    @Value("${elastic.propertyindex}")
    private String propertyIndex;

    @Value("${elastic.classindex}")
    private String classIndex;

    public List<String> searchEntities(String query) throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticHost, elasticPort, elasticScheme)));
        String question = "What is the birth name of Adele?";
//        String text = "Adele <tok> birth name <tok> <var1>";
        String text = "area";
        List<Triple> triples = new ArrayList<>();
        for (String tripleString : text.split(AppConstants.TRIPLE_DELIMITER)) {
//            Triple triple = new Triple(tripleString.split(AppConstants.TOKEN_DELIMITER)[0], tripleString.split(AppConstants.TOKEN_DELIMITER)[1], tripleString.split(AppConstants.TOKEN_DELIMITER)[2]);
            Triple triple = new Triple(tripleString.split(AppConstants.TOKEN_DELIMITER)[0], "", "");
            if (!triple.isSubjectVariable()) {
                BoolQueryBuilder booleanQueryBuilder = new BoolQueryBuilder();
                QueryBuilder q = matchQuery("label", triple.getSubject());
                booleanQueryBuilder.must(q);
                SearchRequest searchRequest = new SearchRequest();
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(booleanQueryBuilder);
                searchSourceBuilder.size(10);
                searchRequest.source(searchSourceBuilder);
                searchRequest.indices(propertyIndex);
                SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                SearchHit[] hits = searchResponse.getHits().getHits();
                for (SearchHit hit : hits) {
                    Map<String, Object> sources = hit.getSourceAsMap();
                    System.out.println("result");
                }
            }
        }
        return new ArrayList<>();
    }
}
