package org.dice.qa.constants;

public interface AppConstants {
    String DBPEDIA_HOST = "http://dbpedia.org/sparql";
    String WIKIDATA_HOST = "https://query.wikidata.org/sparql";
    String DBPEDIA_KB = "dbpedia";
    String WIKIDATA_KB = "wikidata";
    String WIKIDATA_PREFIXES = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>\n" +
            "PREFIX dct: <http://purl.org/dc/terms/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
            "PREFIX schema: <http://schema.org/>\n" +
            "PREFIX cc: <http://creativecommons.org/ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "PREFIX prov: <http://www.w3.org/ns/prov#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "PREFIX wdata: <http://www.wikidata.org/wiki/Special:EntityData/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wdt: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wdtn: <http://www.wikidata.org/prop/direct-normalized/>\n" +
            "PREFIX wds: <http://www.wikidata.org/entity/statement/>\n" +
            "PREFIX p: <http://www.wikidata.org/prop/>\n" +
            "PREFIX wdref: <http://www.wikidata.org/reference/>\n" +
            "PREFIX wdv: <http://www.wikidata.org/value/>\n" +
            "PREFIX ps: <http://www.wikidata.org/prop/statement/>\n" +
            "PREFIX psv: <http://www.wikidata.org/prop/statement/value/>\n" +
            "PREFIX psn: <http://www.wikidata.org/prop/statement/value-normalized/>\n" +
            "PREFIX pq: <http://www.wikidata.org/prop/qualifier/>\n" +
            "PREFIX pqv: <http://www.wikidata.org/prop/qualifier/value/>\n" +
            "PREFIX pqn: <http://www.wikidata.org/prop/qualifier/value-normalized/>\n" +
            "PREFIX pr: <http://www.wikidata.org/prop/reference/>\n" +
            "PREFIX prv: <http://www.wikidata.org/prop/reference/value/>\n" +
            "PREFIX prn: <http://www.wikidata.org/prop/reference/value-normalized/>\n" +
            "PREFIX wdno: <http://www.wikidata.org/prop/novalue/>\n" +
            "PREFIX hint: <http://www.bigdata.com/queryHints#>";
    String PREFIXES = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>\n" +
            "PREFIX dct: <http://purl.org/dc/terms/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
            "PREFIX schema: <http://schema.org/>\n" +
            "PREFIX cc: <http://creativecommons.org/ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "PREFIX prov: <http://www.w3.org/ns/prov#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "PREFIX wdata: <http://www.wikidata.org/wiki/Special:EntityData/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wdt: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wds: <http://www.wikidata.org/entity/>\n" +
            "PREFIX p: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wdref: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wdv: <http://www.wikidata.org/entity/>\n" +
            "PREFIX ps: <http://www.wikidata.org/entity/>\n" +
            "PREFIX pq: <http://www.wikidata.org/entity/>\n" +
            "PREFIX pr: <http://www.wikidata.org/entity/>\n";
    String TOKEN_DELIMITER = "<tok>";
    String VARIABLE_PREFIX = "<var";
    String TRIPLE_DELIMITER = "<dot>";
}
