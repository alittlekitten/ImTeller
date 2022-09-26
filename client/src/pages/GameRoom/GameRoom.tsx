/** @jsxImportSource @emotion/react */

import React from 'react'
import { useParams } from 'react-router-dom'
import { css } from '@emotion/react'
import { useState, useEffect } from 'react'

import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

import game from 'actions/api/game'

import RoomInfo from 'components/roomInfo'
import GameProfile from 'components/gameProfile'
import Setting from '../../components/setting'
import Chat from 'components/chat'
import CardSelectModal from './cardSelectModal'
// 여기서 이제 socket 연결이 시작되는거지
export default function GameRoom() {
  const roomId: any = useParams().gameId
  // api 연결되면 더미 데이터 이걸로 바꾸기
  const [roomDetail, setRoomDetail] = useState('')
  const [modalOpen, setModalOpen] = useState(false)

  const openModal = () => {
    setModalOpen(!modalOpen)
  }

  useEffect(() => {
    game.roomDetail(roomId).then((result) => {
      setRoomDetail(result.data)
    })
  }, [roomId])

  return (
    <div css={roomBg}>
      <div>{roomId} 대기실입니다</div>
      <div css={head}>
        <RoomInfo
          room={roomsDummy.roomId}
          name={roomsDummy.roomName}
          pw={roomsDummy.isLocked}
          people={roomsDummy.peopleNum}
          max={roomsDummy.maxPeopleNum}
          method={roomsDummy.type}
          typeNum={roomsDummy.typeNum}
        />
        <Setting />
      </div>
      <div css={main}>
        <div css={players}>
          {playersDummy.map((player: any) => (
            <div key={player.id} css={playerOne}>
              <GameProfile name={player.name} profile={player.profileUrl} />
            </div>
          ))}
        </div>
        <Chat />
      </div>
      <button css={button} onClick={openModal}>
        카드 선택
      </button>
      <button css={button}>취소</button>
      <CardSelectModal open={modalOpen} close={openModal} />
    </div>
  )
}
const roomsDummy: any = {
  roomId: 1,
  roomName: '내가 최고다',
  isLocked: true,
  peopleNum: 3,
  maxPeopleNum: 5,
  type: '라운드',
  typeNum: 3,
}

const playersDummy: any = [
  {
    id: 1,
    name: '민지',
    profileUrl:
      'https://w.namu.la/s/cf689cab0bb0801a249f535dc7c008c0c054b82954d8f9cdca47b81363e68489a3c24c6dd3adf6664a9c260499bd6dcd132354c253673082c05310d00787cd1627999da116030d7f09cfeabb07d2bbbd2c4581baef341d6e49fd2581525c5e2d',
  },
  {
    id: 2,
    name: '도현',
    profileUrl:
      'https://img.danawa.com/prod_img/500000/147/615/img/14615147_1.jpg?shrink=330:330&_v=20220426173016',
  },
  {
    id: 3,
    name: '보경',
    profileUrl:
      'https://img.danawa.com/prod_img/500000/147/615/img/14615147_1.jpg?shrink=330:330&_v=20220426173016',
  },
  {
    id: 4,
    name: '현영',
    profileUrl:
      'https://img.danawa.com/prod_img/500000/147/615/img/14615147_1.jpg?shrink=330:330&_v=20220426173016',
  },
  {
    id: 5,
    name: '석호',
    profileUrl:
      'https://w.namu.la/s/cf689cab0bb0801a249f535dc7c008c0c054b82954d8f9cdca47b81363e68489a3c24c6dd3adf6664a9c260499bd6dcd132354c253673082c05310d00787cd1627999da116030d7f09cfeabb07d2bbbd2c4581baef341d6e49fd2581525c5e2d',
  },
  {
    id: 6,
    name: '수민',
    profileUrl:
      'https://w.namu.la/s/cf689cab0bb0801a249f535dc7c008c0c054b82954d8f9cdca47b81363e68489a3c24c6dd3adf6664a9c260499bd6dcd132354c253673082c05310d00787cd1627999da116030d7f09cfeabb07d2bbbd2c4581baef341d6e49fd2581525c5e2d',
  },
]

const players = css({
  display: 'flex',
  justifyContent: 'space-evenly',
  flexWrap: 'wrap',
  width: '65%',
})

const playerOne = css({
  margin: '10px',
  flexGrow: 1,
  flexShrink: 1,
  flexBasis: '20%',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
})
const head = css({
  display: 'flex',
  justifyContent: 'space-between',
})
const main = css({
  display: 'flex',
  justifyContent: 'space-evenly',
})
const button = css({
  width: '10em',
  borderRadius: 15,
  margin: '1em',
  cursor: 'pointer',
})
const roomBg = css({
  position: 'fixed',
  top: 0,
  right: 0,
  left: 0,
  bottom: 0,
  backgroundImage: 'linear-gradient(-225deg, #5271C4 0%, #B19FFF 48%, #ECA1FE 100%)',
  backgroundSize: 'cover',
})
