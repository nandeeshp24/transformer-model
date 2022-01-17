package org.dice.qa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aksw.jena_sparql_api.util.sparql.syntax.path.PathWalker;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.path.P_Link;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathVisitorBase;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.dice.qa.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;


@Component
public class FileUtil {

    @Autowired
    private DBpediaUtil dBpediaUtil;

    public List<Map<String, Object>> test(MultipartFile file, String knowledgeBase) throws Exception {
        dBpediaUtil.setKnowledgeBase(knowledgeBase);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.reader();
        ObjectNode node = (ObjectNode) reader.readTree(file.getInputStream());
        List<Map<String, Object>> queryTokensList = new ArrayList<>();
        for (JsonNode question : node.get("questions")) {
            String queryString = AppConstants.PREFIXES + "\n" + question.get("query").get("sparql").asText();
            List<String> tokens = new ArrayList<>();
            Map<String, Object> queryTokens = new HashMap<>();
            try {
                Query query = QueryFactory.create(queryString);
                ElementWalker.walk(query.getQueryPattern(),
                        new ElementVisitorBase() {
                            public void visit(ElementPathBlock el) {
                                Iterator<TriplePath> triples = el.patternElts();
                                Map<String, String> variableMap = new HashMap<>();
                                while (triples.hasNext()) {
                                    TriplePath triple = triples.next();
                                    List<String> tripleTokens = new ArrayList<>();
                                    List<String> literals = new ArrayList<>();
                                    String subjectToken = "";
                                    String predicateToken = "";
                                    String objectToken = "";
                                    try {
                                        Node subject = triple.getSubject();
                                        Node predicate = triple.getPredicate();
                                        Node object = triple.getObject();
                                        Path paths = triple.getPath();
                                        if (subject.isURI()) {
                                            subjectToken = dBpediaUtil.getLabels(new ArrayList<>() {{
                                                add(subject.getURI());
                                            }}).get(0);
                                        } else if (subject.isLiteral()) {
                                            subjectToken = subject.getLiteral().getValue().toString();
                                        } else if (subject.isVariable()) {
                                            String variable = ((Var) subject).getVarName();
                                            if (variableMap.containsKey(variable)) {
                                                subjectToken = variableMap.get(variable);
                                            } else {
                                                subjectToken = "<var" + (variableMap.size() + 1) + ">";
                                                variableMap.put(variable, subjectToken);
                                            }
                                        }
                                        if (predicate != null) {
                                            if (predicate.isURI()) {
                                                predicateToken = dBpediaUtil.getLabels(new ArrayList<>() {{
                                                    add(predicate.getURI());
                                                }}).get(0);
                                            } else if (predicate.isLiteral()) {
                                                predicateToken = predicate.getLiteral().toString();
                                            } else if (predicate.isVariable()) {
                                                String variable = ((Var) predicate).getVarName();
                                                if (variableMap.containsKey(variable)) {
                                                    predicateToken = variableMap.get(variable);
                                                } else {
                                                    predicateToken = "<var" + (variableMap.size() + 1) + ">";
                                                    variableMap.put(variable, predicateToken);
                                                }
                                            }
                                        } else if (paths != null) {
                                            List<String> predicateTokens = new ArrayList<>();
                                            PathWalker.walk(paths, new PathVisitorBase() {
                                                public void visit(P_Link path) {
                                                    predicateTokens.add(path.getNode().getURI());
                                                }
                                            });
                                            predicateToken = String.join(" ", predicateTokens);
                                        }
                                        if (object.isURI()) {
                                            objectToken = dBpediaUtil.getLabels(new ArrayList<>() {{
                                                add(object.getURI());
                                            }}).get(0);
                                        } else if (object.isLiteral()) {
                                            objectToken = object.getLiteral().getValue().toString();
                                        } else if (object.isVariable()) {
                                            String variable = ((Var) object).getVarName();
                                            if (variableMap.containsKey(variable)) {
                                                objectToken = variableMap.get(variable);
                                            } else {
                                                objectToken = "<var" + (variableMap.size() + 1) + ">";
                                                variableMap.put(variable, objectToken);
                                            }
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(question.get("id") + ":\t\t" + queryString);
                                    }
                                    tokens.add(subjectToken + " <tok> " + predicateToken + " <tok> " + objectToken);
                                }
                            }
                        }
                );
                /*if (query.hasOrderBy()) {
                    for (SortCondition condition : query.getOrderBy()) {
                        String cond = condition.direction == -1 ? "DESC" : "ASC";
                        tokens.add(cond);
                    }
                }
                if (query.hasAggregators()) {
                    for (ExprAggregator aggregator : query.getAggregators()) {
                        tokens.add(aggregator.getAggregator().getName());
                    }
                }*/
                Iterator<JsonNode> questionIterator = question.get("question").iterator();
                String questionText = "";
                while (questionIterator.hasNext()) {
                    JsonNode questionString = questionIterator.next();
                    if ("en".equals(questionString.get("language").asText())) {
                        questionText = questionString.get("string").asText();
                    }
                }
                queryTokens.put("question", questionText);
                queryTokens.put("tokens", String.join(" <dot> ", tokens));
                queryTokensList.add(queryTokens);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return queryTokensList;
    }

    public String checkQueries(MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.reader();
        ObjectNode node = (ObjectNode) reader.readTree(file.getInputStream());
        for (JsonNode question : node.get("questions")) {
            String queryString = AppConstants.PREFIXES + "\n" + question.get("query").get("sparql").asText();
            try {
                Query query = QueryFactory.create(queryString);
                ElementWalker.walk(query.getQueryPattern(),
                        new ElementVisitorBase() {
                            public void visit(ElementPathBlock el) {
                                Iterator<TriplePath> triples = el.patternElts();
                                while (triples.hasNext()) {
                                    TriplePath triple = triples.next();
                                    try {
                                        Node subject = triple.getSubject();
                                        Node predicate = triple.getPredicate();
                                        Node object = triple.getObject();
                                        Path paths = triple.getPath();
                                        if (!subject.isVariable() && subject.isURI()) {
                                            if (!subject.getURI().startsWith("http://www.wikidata.org/entity/")) {
                                                System.out.println(subject.getURI());
                                            }
                                        }
                                        if (predicate != null && predicate.isURI()) {
                                            if (!predicate.getURI().startsWith("http://www.wikidata.org/entity/")) {
                                                System.out.println(predicate.getURI());
                                            }
                                        } else if (paths != null) {
                                            PathWalker.walk(paths, new PathVisitorBase() {
                                                public void visit(P_Link path) {
                                                    if (!path.getNode().getURI().startsWith("http://www.wikidata.org/entity/")) {
                                                        System.out.println(path.getNode().getURI());
                                                    }
                                                }
                                            });
                                        }
                                        if (!object.isVariable() && object.isURI()) {
                                            if (!object.getURI().startsWith("http://www.wikidata.org/entity/")) {
                                                System.out.println(object.getURI());
                                            }
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(question.get("id") + ":\t\t" + queryString);
                                    }
                                }
                            }
                        }
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "Done";
    }
}
