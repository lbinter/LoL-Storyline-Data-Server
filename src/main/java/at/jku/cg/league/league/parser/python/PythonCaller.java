package at.jku.cg.league.league.parser.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.jku.cg.league.league.parser.config.ParserConfiguration;

@Component
public class PythonCaller {
    @Autowired
    public ParserConfiguration config;
    Logger log = LoggerFactory.getLogger(PythonCaller.class);

    public int fetchMatchesFromRiot(String puuid) {
        return fetchMatchesFromRiot(puuid, puuid);

    }

    public int fetchMatchesFromRiot(String puuid, String folder) {
        return callPython(config.getPy_fileFolder() + "/" + config.getPy_riotMatch(), config.getApi(), puuid, folder);

    }

    public int fetchMatch(String matchId) {
        // call riotMatch.py with Riot API KEY, match id and working directory location
        return callPython(config.getPy_fileFolder() + "/" + config.getPy_riotMatch(), config.getApi(), matchId,
                config.getPy_fileFolder());
    }

    public int callAnalyze_G(String matchId) {
        // call call analyze_G.py with match id, useMode = 1 and working directory location
        return callPython(config.getPy_fileFolder() + "/" + config.getPy_analyze_G(), matchId, Integer.toString(1),
                config.getPy_fileFolder());
    }

    public int callTrajectory_G(String matchId) {
        // call call trajectory_G.py with match id, useMode = 1 and working directory location
        return callPython(config.getPy_fileFolder() + "/" + config.getPy_trajectory_G(), matchId, Integer.toString(1),
                config.getPy_fileFolder());
    }

    public int callBattle_extractor2G(String matchId) {
        // call call battle-extractor2.py with match id and working directory location
        return callPython(config.getPy_fileFolder() + "/" + config.getPy_battle_extractor2(), matchId, config.getPy_fileFolder());
    }

    public int callCount1(String matchId) {
        // call call Count1.py with match id and working directory location
        return callPython(config.getPy_fileFolder() + "/" + config.getPy_count1(), matchId, config.getPy_fileFolder());
    }

    private int callPython(String... params) {
        String[] commands = new String[1 + params.length];
        commands[0] = config.getPython(); // path to python
        // commands[1] = script; // path to script
        int i = 1;
        for (String p : params) {
            commands[i] = p;
            i++;
        }
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        try {

            Process process = processBuilder.start();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            int exitCode = process.waitFor();
            log.info("Python: " + buffer.toString());
            if (exitCode != 0) {
                log.error("Error: Python script exited with code " + exitCode);
            }
            return exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            log.error("Error while running Python script: " + e.getMessage());

        }
        return -1;
    }

}