package at.jku.cg.league.league.parser.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("parser")
public class ParserConfiguration {
    private String api;
    private String python;
    private String py_fileFolder;
    private String py_main;
    private String py_riotMatch;
    private String py_analyze_G;
    private String py_trajectory_G;
    private String py_battle_extractor2;
    private String py_count1;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getPython() {
        return python;
    }

    public void setPython(String pythonPath) {
        this.python = pythonPath;
    }

    public String getPy_fileFolder() {
        return py_fileFolder;
    }

    public void setPy_fileFolder(String py_fileFolder) {
        this.py_fileFolder = py_fileFolder;
    }

    public String getPy_main() {
        return py_main;
    }

    public void setPy_main(String py_main) {
        this.py_main = py_main;
    }

    public String getPy_riotMatch() {
        return py_riotMatch;
    }

    public void setPy_riotMatch(String riotMatch) {
        this.py_riotMatch = riotMatch;
    }

    public String getPy_analyze_G() {
        return py_analyze_G;
    }

    public void setPy_analyze_G(String py_analyze_G) {
        this.py_analyze_G = py_analyze_G;
    }

    public String getPy_trajectory_G() {
        return py_trajectory_G;
    }

    public void setPy_trajectory_G(String py_trajectory_G) {
        this.py_trajectory_G = py_trajectory_G;
    }

    public String getPy_battle_extractor2() {
        return py_battle_extractor2;
    }

    public void setPy_battle_extractor2(String py_battle_extractor2) {
        this.py_battle_extractor2 = py_battle_extractor2;
    }

    public String getPy_count1() {
        return py_count1;
    }

    public void setPy_count1(String py_count1) {
        this.py_count1 = py_count1;
    }
}