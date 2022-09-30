package com.classic.imteller.api.service;

import com.classic.imteller.api.dto.room.*;
import com.classic.imteller.api.repository.Room;
import com.classic.imteller.api.repository.RoomRepository;
import com.classic.imteller.exception.CustomException;
import com.classic.imteller.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    @Transactional
    public Room joinRoom(String userSessionId, long sessionId, JoinReqDto joinReqDto) {
        // 게임이 시작되었다면 방에 들어갈 수 없어야 함
        if (roomRepository.getRoom(sessionId) == null) return null;
        if (roomRepository.getRoom(sessionId).getStarted()) return null;

        // 인원수가 초과됐어도 방에 들어갈 수 없어야 함
        if (roomRepository.getRoom(sessionId).getPlayers().size() >= roomRepository.getRoom(sessionId).getMaxNum()) return null;

        boolean isGood = roomRepository.joinRoom(userSessionId, sessionId, joinReqDto);
        if (isGood) {
            Room room = roomRepository.getRoom(sessionId);
            return room;
        }
        return null;
    }

    @Transactional
    public Room exitRoom(long sessionId, ExitReqDto exitReqDto) {
        String res = roomRepository.exitRoom(sessionId, exitReqDto);
        if (res == "ok"){
            Room room = roomRepository.getRoom(sessionId);
            return room;
        }
        return null;
    }

    @Transactional
    public HashMap<String, Boolean> ready(long sessionId, ReadyReqDto readyReqDto) {
        return roomRepository.ready(sessionId, readyReqDto);
    }

    @Transactional(readOnly = true)
    public Room getRoom(long sessionId) {
        return roomRepository.getRoom(sessionId);
    }

    @Transactional
    public boolean start(String userSessionId, long sessionId) {
        Room room = roomRepository.getRoom(sessionId);
        if (room == null) return false;

        // 모두가 레디했는지 확인
        HashMap<String, Boolean> isReady = room.getReady();
        List<String> players = room.getPlayers();
        boolean chk = true;
        for (String player : players) {
            if(!isReady.get(player)) {
                chk = false;
                break;
            }
        }
        if (!chk) return false;

        // 3명 이상인지 확인
        if(players.size() < 3) return false;

        // 방장의 sessionId와 시작요청을 보낸 사람이 같은지 확인
        HashMap<String, String> usids = room.getUserSessionIds();
        String leader = room.getLeader();
        if (usids.get(leader).equals(userSessionId)) {
            roomRepository.start(sessionId);
            return true;
        }
        else return false;
    }

    @Transactional
    public HashMap<String, List<GameCardDto>> selectCards(long sessionId, SelectReqDto selectReqDto) {
        boolean chk = roomRepository.selectCards(sessionId, selectReqDto);
        if (chk && roomRepository.getRoom(sessionId).getCards().size() == roomRepository.getRoom(sessionId).getPlayers().size()) {
            HashMap<String, List<GameCardDto>> firstHands = roomRepository.draw(sessionId);
            return firstHands;
        }
        else return null;
    }

    public void setPhase (long sessionId, int phase){
        roomRepository.setPhase(sessionId, phase);
    }

    public void startTimer (long sessionId, TimerTask task) {
        roomRepository.startTimer(sessionId, task);
    }

    public void stopTimer (long sessionId) {
        roomRepository.stopTimer(sessionId);
    }

    public void saveTellerInfo(long sessionId, TellerDto tellerDto){
        roomRepository.saveTellerInfo(sessionId, tellerDto);
    }

    public boolean endCheck(long sessionId) {
        String endType = roomRepository.getRoom(sessionId).getType();
        if (endType.equals("score")) {
            HashMap<String, Integer> totalScore = roomRepository.getTotalScore(sessionId);
            for (String player : totalScore.keySet()) {
                if (totalScore.get(player) >= roomRepository.getTypeNum(sessionId)) {
                    return true;
                }
            }
            return false;
        } else {
            int laps = roomRepository.getRoom(sessionId).getLaps();
            int typeNum = roomRepository.getRoom(sessionId).getTypeNum();
            List<String> players = roomRepository.getRoom(sessionId).getPlayers();
            String teller = roomRepository.getRoom(sessionId).getTeller();

            return (laps == typeNum) && (players.indexOf(teller) == players.size() - 1);
        }
    }

    public void setNextTeller(long sessionId) {
        roomRepository.setNextTeller(sessionId);
    }

    public void forcedCard(long sessionId){
        roomRepository.forcedCard(sessionId);
    }

    public boolean getUserCard(long sessionId, UserCardDto userCardDto){
        return roomRepository.getUserCard(sessionId, userCardDto);
    }

    public HashMap<String, Boolean> getUserStatus(long sessionId) {
        return roomRepository.getUserStatus(sessionId);
    }

    public boolean choice(long sessionId, ChoiceCardDto choiceCardDto) {
        return roomRepository.choice(sessionId, choiceCardDto);
    }

    public void statusReset(long sessionId) {
        roomRepository.statusReset(sessionId);
    }

    public void updateTellerStatus(long sessionId) {
        roomRepository.updateTellerStatus(sessionId);
    }

    public void randomSelect(long sessionId) {
        roomRepository.randomSelect(sessionId);
    }

    public HashMap<String, Integer> scoreCalc(long sessionId) {
        return roomRepository.scoreCalc(sessionId);
    }

    public HashMap<String, Integer> getTotalScore(long sessionId) {
        return roomRepository.getTotalScore(sessionId);
    }

    public int getTurn(long sessionId) {
        return roomRepository.getTurn(sessionId);
    }

    public boolean checkStatus(long sessionId) {
        // 모든 status를 받아서 true인지 확인
        HashMap<String, Boolean> status = roomRepository.getUserStatus(sessionId);
        boolean chk = true;
        for (String player : status.keySet()) {
            if (status.get(player) == false) {
                chk = false;
                break;
            }
        }
        return chk;
    }

    public void oneCardDraw(long sessionId) {
        roomRepository.oneCardDraw(sessionId);
    }

    public HashMap<String, List<GameCardDto>> getHand(long sessionId) {
        return roomRepository.getHand(sessionId);
    }

    public void useItem (long sessionId, UseItemDto useItemDto) {
        roomRepository.useItem(sessionId, useItemDto);
    }

    public List<EffectDto> getActivated (long sessionId) {
        return roomRepository.getActivated(sessionId);
    }
}
