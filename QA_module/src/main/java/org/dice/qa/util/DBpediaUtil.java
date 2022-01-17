package org.dice.qa.util;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.dice.qa.constants.AppConstants;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DBpediaUtil {
    private String knowledgeBase;
    private RDFConnectionFuseki conn = null;

    public void setKnowledgeBase(String kb) {
        knowledgeBase = kb;
    }

    private void buildConnection(String host) {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination(host);
        conn = (RDFConnectionFuseki) builder.build();
    }

    public ResultSet getResultFromQuery(String queryString) {
        ResultSet resultSet = null;
        QueryExecution queryExecution = null;
        try {
            Query query = QueryFactory.create(queryString);
            queryExecution = conn.query(query);
            if (AppConstants.DBPEDIA_KB.equals(knowledgeBase)) {
                queryExecution = QueryExecutionFactory.sparqlService(AppConstants.DBPEDIA_HOST, query);
            } else if (AppConstants.WIKIDATA_KB.equals(knowledgeBase)) {
                queryExecution = QueryExecutionFactory.sparqlService(AppConstants.WIKIDATA_HOST, query);
            }
            resultSet = ResultSetFactory.copyResults(queryExecution.execSelect());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (queryExecution != null) {
                queryExecution.close();
            }
        }
        return resultSet;
    }

    private String extractToken(String url) {
        int index = url.lastIndexOf("/");
        if (url.contains("#")) {
            index = url.lastIndexOf("#");
        }
        return url.substring((index + 1));
    }

    public List<String> getLabels(List<String> uriList) {
        if (conn == null) {
            if (AppConstants.WIKIDATA_KB.equals(knowledgeBase)) {
                this.buildConnection(AppConstants.WIKIDATA_HOST);
            } else {
                this.buildConnection(AppConstants.DBPEDIA_HOST);
            }
        }
        Map<String, String> labelMap = new HashMap<>();
        for (String uri : uriList) {
            StringBuilder queryString = new StringBuilder("SELECT ?sub ?label")
                    .append(" WHERE {")
                    .append(" ?sub <http://www.w3.org/2000/01/rdf-schema#label> ?label")
                    .append(" FILTER ( ?sub = <").append(uri).append("> )")
                    .append(" FILTER ( LANG(?label) = 'en' )")
                    .append(" }");
            ResultSet labelsResultSet = getResultFromQuery(queryString.toString());
            if (labelsResultSet != null) {
                while (labelsResultSet.hasNext()) {
                    QuerySolution querySolution = labelsResultSet.next();
                    labelMap.put(querySolution.get("?sub").asResource().getURI(), querySolution.get("?label").asLiteral().getString());
                }
            }
        }
        return uriList.stream().map(i -> labelMap.getOrDefault(i, extractToken(i))).collect(Collectors.toList());
    }
}
