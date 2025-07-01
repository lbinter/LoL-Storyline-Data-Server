package at.jku.cg.league.league.parser.api;

import org.springframework.web.bind.annotation.RestController;

import at.jku.cg.league.league.parser.python.PythonCaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ParserController {
    Logger log = LoggerFactory.getLogger(ParserController.class);

    @Autowired
    PythonCaller python;

    @GetMapping("/getMatchesFor/{puuid}")
    public int getMatchesFor(@PathVariable String puuid) {
        log.info("getMatchesFor(" + puuid + ")");
        return python.fetchMatchesFromRiot(puuid);
    }

    @GetMapping("/getMatchAndAnalyze/{matchId}")
    public ResponseEntity<FetchAndAnalyze> getMatchAndAnalyze(@PathVariable String matchId) {
        log.info("getMatchAndAnalyze(" + matchId + ")");
        FetchAndAnalyze response = new FetchAndAnalyze();
        response.fetchMatchFromRiot = python.fetchMatch(matchId);
        if (response.fetchMatchFromRiot == 0) { // TODO: Check if Match.json is correct and not an error
            response.analyze_G = python.callAnalyze_G(matchId);
            if (response.analyze_G == 0) {
                response.trajectory_G = python.callTrajectory_G(matchId); // trajectory_G needs data from analyze_G
            }
            response.battle_extractor2G = python.callBattle_extractor2G(matchId);
            response.count1 = python.callCount1(matchId);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getMatch/{matchId}")
    public int getMatch(@PathVariable String matchId) {
        log.info("getMatch(" + matchId + ")");
        return python.fetchMatch(matchId);
    }

    @GetMapping("/analyze_G/{matchId}")
    public int analyze_G(@PathVariable String matchId) {
        log.info("analyze_G(" + matchId + ")");
        return python.callAnalyze_G(matchId);
    }

    @GetMapping("/trajectory_G/{matchId}")
    public int trajectory_G(@PathVariable String matchId) {
        log.info("trajectory_G(" + matchId + ")");
        return python.callTrajectory_G(matchId);
    }

    @GetMapping("/battle_extractor2G/{matchId}")
    public int battle_extractor2G(@PathVariable String matchId) {
        log.info("battle_extractor2G(" + matchId + ")");
        return python.callBattle_extractor2G(matchId);
    }

    @GetMapping("/count1/{matchId}")
    public int count1(@PathVariable String matchId) {
        log.info("count1(" + matchId + ")");
        return python.callCount1(matchId);
    }
}
