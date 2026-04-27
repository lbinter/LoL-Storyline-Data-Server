package at.jku.cg.league.league.parser.api;

import org.springframework.web.bind.annotation.RestController;

import at.jku.cg.league.league.parser.config.ParserConfiguration;
import at.jku.cg.league.league.parser.python.PythonCaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ParserController {
    Logger log = LoggerFactory.getLogger(ParserController.class);

    @Autowired
    public ParserConfiguration config;
    @Autowired
    PythonCaller python;

    @CrossOrigin
    @GetMapping("/getMatchAndAnalyze/{regionId}/{matchId}")
    public ResponseEntity<FetchAndAnalyze> getMatchAndAnalyze(@PathVariable Integer regionId,
            @PathVariable String matchId) {
        log.info("getMatchAndAnalyze(" + regionId + "," + matchId + ")");
        FetchAndAnalyze response = new FetchAndAnalyze();

        if (!isValidRegionId(regionId)) {
            return ResponseEntity.ok(response);
        }
        response.regionId = regionId;
        if (!isValidMatchId(matchId)) {
            return ResponseEntity.ok(response);
        }
        response.matchId = matchId;

        response.fetchMatchFromRiot = python.fetchMatch(regionId, matchId);
        if (response.fetchMatchFromRiot == 0) { // TODO: Check if Match.json is correct and not an error
            response.analyze_G = python.callAnalyze_G(regionId, matchId);
            if (response.analyze_G == 0) {
                response.trajectory_G = python.callTrajectory_G(regionId, matchId); // trajectory_G needs data from
                                                                                    // analyze_G
            }
            response.battle_extractor2G = python.callBattle_extractor2G(regionId, matchId);
            response.count1 = python.callCount1(regionId, matchId);
        }
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping("/deleteMatch/{regionId}/{matchId}")
    public int deleteMatch(@PathVariable Integer regionId, @PathVariable String matchId) {
        if (!config.isAllow_delete()) {
            log.error("deleteMatch is disabled, cannot delete regionId[" + regionId + "], matchId[" + matchId + "]");
            return -2;
        }
        log.info("deleteMatch(" + regionId + "," + matchId + ")");
        if (!isValidMatchId(matchId)) {
            return -1;
        }
        return python.deleteMatch(regionId, matchId);
    }

    @CrossOrigin
    @GetMapping("/getMatch/{regionId}/{matchId}")
    public int getMatch(@PathVariable Integer regionId, @PathVariable String matchId) {
        log.info("getMatch(" + regionId + "," + matchId + ")");
        if (!isValidMatchId(matchId)) {
            return -1;
        }
        return python.fetchMatch(regionId, matchId);
    }

    @CrossOrigin
    @GetMapping("/analyze_G/{regionId}/{matchId}")
    public int analyze_G(@PathVariable Integer regionId, @PathVariable String matchId) {
        log.info("analyze_G(" + regionId + "," + matchId + ")");
        if (!isValidMatchId(matchId)) {
            return -1;
        }
        return python.callAnalyze_G(regionId, matchId);
    }

    @CrossOrigin
    @GetMapping("/trajectory_G/{regionId}/{matchId}")
    public int trajectory_G(@PathVariable Integer regionId, @PathVariable String matchId) {
        log.info("trajectory_G(" + regionId + "," + matchId + ")");
        if (!isValidMatchId(matchId)) {
            return -1;
        }
        return python.callTrajectory_G(regionId, matchId);
    }

    @CrossOrigin
    @GetMapping("/battle_extractor2G/{regionId}/{matchId}")
    public int battle_extractor2G(@PathVariable Integer regionId, @PathVariable String matchId) {
        log.info("battle_extractor2G(" + regionId + "," + matchId + ")");
        if (!isValidMatchId(matchId)) {
            return -1;
        }
        return python.callBattle_extractor2G(regionId, matchId);
    }

    @GetMapping("/count1/{regionId}/{matchId}")
    public int count1(@PathVariable Integer regionId, @PathVariable String matchId) {
        log.info("count1(" + regionId + "," + matchId + ")");
        if (!isValidMatchId(matchId)) {
            return -1;
        }
        return python.callCount1(regionId, matchId);
    }

    private boolean isValidMatchId(String matchId) {
        if (matchId == null) {
            log.error("empty matchId detected");
            return false;
        }
        if (StringUtils.containsWhitespace(matchId)) {
            log.error("invalid matchId " + matchId);
            return false;
        }
        return true;
    }

    private boolean isValidRegionId(Integer regionId) {
        if (regionId == null) {
            log.error("empty regionId detected");
            return false;
        }
        return 1 <= regionId && regionId <= 4;
    }
}
