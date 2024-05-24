package ch.uzh.ifi.hase.soprafs24.rest.mapper;


import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "friendsList", target = "friendsList")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "totalplayed", target = "totalplayed")
    @Mapping(source = "totalwins", target = "totalwins")
    @Mapping(source = "lobbyInvitations", target = "lobbyinvitations")
    @Mapping(source = "friendRequests", target = "friendRequests")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "totalplayed", target = "totalplayed")
    @Mapping(source = "totalwins", target = "totalwins")
    UserStatsGetDTO convertEntityToUserStatsGetDTO(User user);

    @Mapping(source = "id", target = "id") // smailalijagic: id needed? Once set it never changes
    @Mapping(source = "status", target = "status")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "friendsList", target = "friendsList")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "totalplayed", target = "totalplayed")
    @Mapping(source = "totalwins", target = "totalwins")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO); // smailalijagic: all updatable data


    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    AuthenticationDTO convertEntityToAuthenticationDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    User convertAuthenticationDTOtoUser(AuthenticationDTO authenticationDTO);


    //@Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "lastmessage")
    Chat convertMessagePostDTOtoEntity(MessagePostDTO messagePostDTO); // smailalijagic: added, issue #58
    // source = MessagePostDTO, target = Chat


    @Mapping(source = "lastmessage", target = "message")
    MessageGetDTO convertEntityToMessageGetDTO(Chat chat); // smailalijagic: added, issue #58
    // source = Chat, target = MessageGetDTO

    @Mapping(source = "id", target = "lobbyid")
    Lobby convertEntityToLobbyDeleteDTO(LobbyDeleteDTO lobbyDeleteDTO); // smailalijagic: added, issue #54
    // source = LobbyDeleteDTO, target = Lobby

    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    ImageDTO convertEntityToImageDTO(Image image); // dario added for Image

    @Mapping(source = "lobbyid", target = "id")
    @Mapping(source = "creatorUserId", target = "creatorUserId")
    @Mapping(source = "invitedUserId", target = "invitedUserId")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);
    // source = Lobby, target = LobbyGetDTO

    @Mapping(source = "lobbyid", target = "id")
    @Mapping(source = "creatorUserId", target = "creatorUserId")
    @Mapping(source = "invitedUserId", target = "invitedUserId")
    @Mapping(source = "lobbyToken", target = "lobbyToken")
    LobbyPutDTO convertEntityToLobbyPutDTO(Lobby lobby);

    @Mapping(source = "lobbyid", target = "id")
    @Mapping(source = "creatorUserId", target = "creatorUserId")
    LobbyPostDTO convertEntityToLobbyPostDTO(Lobby lobby);
    // source = Lobby, target = LobbyGetDTO

    @Mapping(source = "gameid", target = "gameId")
    @Mapping(source = "playerid", target = "playerId")
    @Mapping(source = "imageid", target = "imageId")
    Guess convertGuessPostDTOtoEntity(GuessPostDTO guessPostDTO);


    @Mapping(source = "guess", target = "guess")
    @Mapping(source = "strikes", target = "strikes")
    ResponsePostDTO convertEntitytoReponsePostDTO(Response response);


    @Mapping(source = "creatorUserId", target = "creatorPlayerId")
    @Mapping(source = "invitedUserId", target = "invitedPlayerId")
    @Mapping(source = "maxStrikes", target = "maxStrikes")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);
    // source = GamePostDTO, target = Game

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "creatorPlayerId", target = "creatorPlayerId")
    @Mapping(source = "invitedPlayerId", target = "invitedPlayerId")
    @Mapping(source = "maxStrikes", target = "maxStrikes")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "id")
    User convertUserDeleteDTOtoEntity(UserDeleteDTO userDeleteDTO);
    // smailalijagic: source = UserDeleteDTO, target = User

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "receiverUserName", target = "receiverUserName")
    FriendRequest convertFriendRequestPostDTOtoEntity(FriendRequestPostDTO friendRequestPostDTO);

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "receiverId", target = "receiverId")
    FriendRequest convertFriendRequestPutDTOtoEntity(FriendRequestPutDTO friendRequestPutDTO);

    @Mapping(source = "id", target = "friendId")
    @Mapping(source = "username", target = "friendUsername")
    @Mapping(source = "profilePicture", target = "friendIcon")
    FriendGetDTO convertEntityToFriendGetDTO(User friend);
}