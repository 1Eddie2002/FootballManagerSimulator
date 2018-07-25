package com.elliot.footballmanager.match.model;

import com.elliot.footballmanager.match.FootballTeamMatchStats;
import com.elliot.footballmanager.match.RandomNumberGenerator;
import com.elliot.footballmanager.player.Player;

/**
 * Used within the MatchEngine to determine whether a Player
 * decides to shoot and calculates whether he scores.
 * @author Elliot
 */
public class Shot {

    private Player playerTakingShot;
    private Integer numberOfTilesAwayFromGoal;

    public Shot() {

    }

    public Shot(Player playerTakingShot) {
        this.playerTakingShot = playerTakingShot;
        numberOfTilesAwayFromGoal = getPlayersDistanceFromGoal();
    }

    private Integer getPlayersDistanceFromGoal() {
        return Math.abs(getPlayerTakingShot().getCurrentXCoordinate() - getPlayerTakingShot().getOpposingTeamsGoal()[0]);
    }

    public boolean doesPlayerDecideToShoot() {
        return RandomNumberGenerator.getRandomNumberBetweenZeroAndOneHundred() < playersProbabilityOfScoringAGoal();
    }

    public void attemptShot(Football football, Player opposingTeamsGoalkeeper, FootballTeamMatchStats matchStats) {
        Integer playersChanceOfScoring = playersProbabilityOfScoringAGoal();
        Integer randomChanceOfScoring = RandomNumberGenerator.getRandomNumberBetweenZeroAndOneHundred();

        if (randomChanceOfScoring < (playersChanceOfScoring / 2)) {
            updateMatchStatsGoalScored(matchStats);
            giveGoalKeeperTheFootball(football, opposingTeamsGoalkeeper);
        } else if (randomChanceOfScoring >= (playersChanceOfScoring / 2)
                && randomChanceOfScoring <= playersChanceOfScoring) {
            updateMatchStatsShotSaved(matchStats);
            giveGoalKeeperTheFootball(football, opposingTeamsGoalkeeper);
        } else {
            updateMatchStatsShotMissed(matchStats);
            giveGoalKeeperTheFootball(football, opposingTeamsGoalkeeper);
        }
    }

    private void updateMatchStatsGoalScored(FootballTeamMatchStats matchStats) {
        matchStats.incrementGoalsScoredByOne();
    }

    private void updateMatchStatsShotSaved(FootballTeamMatchStats matchStats) {
        matchStats.incrementShotsOnTargetByOne();
    }

    private void updateMatchStatsShotMissed(FootballTeamMatchStats matchStats) {
        matchStats.incrementShotsByOne();
    }

    private void giveGoalKeeperTheFootball(Football football, Player goalkeeper) {
        football.setPlayerInPossession(goalkeeper);
    }

    //TODO: Continue to flesh this out, E,g use additional attributes determining whether they're off balance etc.
    private Integer playersProbabilityOfScoringAGoal() {
        Integer total = 0;
        total += getPlayerTakingShot().getTechnicalAttributes().getFinishing();
        total += getPlayerTakingShot().getTechnicalAttributes().getShotPower();
        total += getPlayerTakingShot().getTechnicalAttributes().getCurve();
        total += getPlayerTakingShot().getTechnicalAttributes().getCurve();
        total += getPlayerTakingShot().getTechnicalAttributes().getBallControl();

        Integer numberOfAttributesUsedForCalculation = 5;
        total = total / numberOfAttributesUsedForCalculation;

        for (int i = 0; i < getNumberOfTilesAwayFromGoal(); i++) {
            total -= 10;
        }

        return total;
    }

    public Player getPlayerTakingShot() {
        return playerTakingShot;
    }

    public void setPlayerTakingShot(Player playerTakingShot) {
        this.playerTakingShot = playerTakingShot;
    }

    public Integer getNumberOfTilesAwayFromGoal() {
        return numberOfTilesAwayFromGoal;
    }

    public void setNumberOfTilesAwayFromGoal(Integer numberOfTilesAwayFromGoal) {
        this.numberOfTilesAwayFromGoal = numberOfTilesAwayFromGoal;
    }
}