package ch.uzh.ifi.hase.soprafs24.rest.mapper;


import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "password", target = "password")
  @Mapping(source = "username", target = "username")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  User convertUserGetDTOtoEntity(UserGetDTO userGetDTO);

  @Mapping(source = "id", target = "id") // smailalijagic: id needed? Once set it never changes
  @Mapping(source = "status", target = "status")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "usericon", target = "usericon")
  @Mapping(source = "userfriendlist", target = "userfriendlist")
  @Mapping(source = "usergamelobbylist", target = "usergamelobbylist")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "totalplayed", target = "totalplayed")
  @Mapping(source = "totalwins", target = "totalwins")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "id", target = "id") // smailalijagic: id needed? Once set it never changes
  @Mapping(source = "status", target = "status")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "usericon", target = "usericon")
  @Mapping(source = "userfriendlist", target = "userfriendlist")
  @Mapping(source = "usergamelobbylist", target = "usergamelobbylist")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "totalplayed", target = "totalplayed")
  @Mapping(source = "totalwins", target = "totalwins")
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO); // smailalijagic: all updatable data


  @Mapping(source = "id", target = "id")
  @Mapping(source = "token", target = "token")
  AuthenticationResponseDTO convertEntityToAuthenticationResponseDTO(User user);


  //@Mapping(source = "id", target = "id")
  @Mapping(source = "message", target = "lastmessage")
  Chat convertMessagePostDTOtoEntity(MessagePostDTO messagePostDTO); // smailalijagic: added, issue #58
  // source = MessagePostDTO, target = Chat

  //@Mapping(source = "id", target = "id")
  @Mapping(source = "messages", target = "message")
  MessageGetDTO convertEntityToMessageGetDTO(Chat chat); // smailalijagic: added, issue #58
  // source = Chat, target = MessageGetDTO

  @Mapping(source = "id", target = "lobbyid")
  Lobby convertEntityToLobbyDeleteDTO(LobbyDeleteDTO lobbyDeleteDTO); // smailalijagic: added, issue #54
  // source = LobbyDeleteDTO, target = Lobby

  @Mapping(source = "lobbyid", target = "id")
  @Mapping(source = "creator_userid", target = "creator_userid")
  @Mapping(source = "invited_userid", target = "invited_userid")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);
  // source = Lobby, target = LobbyGetDTO

  @Mapping(source = "lobbyid", target = "id")
  @Mapping(source = "creator_userid", target = "creator_userid")
  @Mapping(source = "invited_userid", target = "invited_userid")
  @Mapping(source = "lobbyToken", target = "lobbyToken")
  LobbyPutDTO convertEntityToLobbyPutDTO(Lobby lobby);

  @Mapping(source = "lobbyid", target = "id")
  @Mapping(source = "creator_userid", target = "creator_userid")
  LobbyPostDTO covertEntityToLobbyPostDTO(Lobby lobby);
  // source = Lobby, target = LobbyGetDTO

//  @Mapping(source = "id", target = "lobbyid")
//  @Mapping(source = "creator_userid", target = "creator_userid")
//  @Mapping(source = "invited_userid", target = "invited_userid")
//  @Mapping(source = "lobbyToken", target = "lobbyToken")
//  Lobby convertLobbyPutDTOtoEntity(LobbyPutDTO lobbyPutDTO);

  @Mapping(source = "gameid", target = "gameId")
  @Mapping(source = "playerid", target = "playerId")
  @Mapping(source = "imageid", target = "imageId")
  Guess convertGuessPostDTOtoEntity(GuessPostDTO guessPostDTO);

  @Mapping(source = "creatorid", target = "creatorId")
  @Mapping(source = "invitedplayerid", target = "invitedPlayerId")
  Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

  @Mapping(source = "playerid", target = "playerId")
  @Mapping(source = "imageid", target = "imageId")
  Guess convertGuessPutDTOtoEmtity(GuessPostDTO guessPostDTO);
}

