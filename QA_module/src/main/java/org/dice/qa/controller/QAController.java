package org.dice.qa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.dice.qa.util.ElasticUtil;
import org.dice.qa.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class QAController {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ElasticUtil elasticUtil;

    @RequestMapping(path = "/create-training-data", method = RequestMethod.POST)
    public ResponseEntity<Object> createTrainingData(@RequestParam("file") MultipartFile file, @RequestParam("host") String knowledgeBase) throws Exception {
        try {
            List<Map<String, Object>> trainingData = new ArrayList<>(fileUtil.test(file, knowledgeBase));
            File outFile = new File("/data/QA/QA/" + file.getOriginalFilename());
            FileWriter fileWriter = new FileWriter(outFile);
            ObjectMapper mapper = new ObjectMapper();
            for (Map<String, Object> entry : trainingData) {
                try {
                    Map<String, String> line = new TreeMap<>();
                    line.put("question", (String) entry.get("question"));
                    line.put("tokens", (String) entry.get("tokens"));
                    fileWriter.append(mapper.writeValueAsString(line));
                    fileWriter.append("\n");
                } catch (Exception ex) {
                    System.out.println("Here");
                }
            }
            fileWriter.close();
            /*InputStreamResource resource = new InputStreamResource(new FileInputStream(outFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "training_data.json"));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");*/

            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(outFile.length())
//                    .contentType(MediaType.parseMediaType("application/json"))
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Done!");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<h4>Invalid File</h4>");
        }
    }

    @RequestMapping("/")
    public ResponseEntity<Object> home() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("templates/index.html").getPath());
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/html"))
                .body(content);
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test(@RequestParam("label") String label) throws Exception {
        List<String> list = elasticUtil.searchEntities(label);
        return "Done";
    }

}
