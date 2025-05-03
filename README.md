
# My Chat SSE & Flux

## API Reference

#### Get main static page
```http
  GET /chat
```
| Description                                |
| :----------------------------------------- |
| return main page with users and chat rooms |

#### Get SSE to user
```http
  GET /chat/init/{user}
```
| Parameter | Type     | Description                            |
| :-------- | :------- | :------------------------------------- |
| `user`    | `string` | **Required**. user name to fetch event |

#### Get list of available users
```http
  GET /chat/users
```

#### Get list of available chat rooms
```http
  GET /chat/chats/{user}
```
| Parameter | Type     | Description                               |
| :-------- | :------- | :---------------------------------------- |
| `user`    | `string` | Get list of available chat rooms for user |

#### Post write message to chat room
```http
  POST /chat/{user}/{chat}
```
|  Body    | Type     | Description                               |
| :------- | :------- | :---------------------------------------- |
| `mypost` | `string` | {user} write message{body} to {chat} room |

## Authors

- [@Vicktrix](https://github.com/Vicktrix)

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

## Examples 

https://github.com/Vicktrix/MyChat/blob/master/images/user1chat1_2.png

https://github.com/Vicktrix/MyChat/blob/master/images/user1chat1_first.png

https://github.com/Vicktrix/MyChat/blob/master/images/user1chat2_1.png

https://github.com/Vicktrix/MyChat/blob/master/images/user2chat1.png

https://github.com/Vicktrix/MyChat/blob/master/images/user2chat1_2.png

https://github.com/Vicktrix/MyChat/blob/master/images/user2chat2_2.png

https://github.com/Vicktrix/MyChat/blob/master/images/user3chat1.png

https://github.com/Vicktrix/MyChat/blob/master/images/user3chat1_2.png

https://github.com/Vicktrix/MyChat/blob/master/images/user3chat2_1.png

