<p>
  <img src="https://github.com/rently-io/image-service/actions/workflows/ci.yml/badge.svg" />
  <img src="https://github.com/rently-io/image-service/actions/workflows/cd.yml/badge.svg" />
</p>

# Image Service

This Spring Boot project is one among other RESTful APIs used in the larger Rently project. This service handles the conversion of image base64 data into usable URLs. The images are saved on a MongoDB database. Possible requests include `GET`, `POST`, `PUT`, `DELETE`.

Additionally, upon fetching images, a *Rently.io* watermark is stamped on the bottom left corner of images like so: 

<p align="center">
  <img height="200px" src="https://i.imgur.com/SmS5WmQ.png" />
  <img height="200px" src="https://i.imgur.com/scIquXW.jpg" />
  <img height="200px" src="https://i.imgur.com/HXTqtA0.png" />
</p>

It may seem redundant to stamp a watermark on `GET` request rather than `PUT` or `POST` requests as it adds avoidable processing time, though, in the event that the text needs to be changed, no irreversible changes to images would have been done. Also, adding the watermark adds processing on the order of a few milliseconds, completely acceptable in the context of this project. 

A middleware was added that verifies the json web tokens' validity upon every requests. JWTs must have the [following shape](#jwt-object]) and must be encrypted using the right server secret and its corresponding hashing algorithm.

After each subsequent additions and changes to the codebase of the service, tests are ran and, if passed, the service is automatically deployed on to a Heroku instance [here](https://image-service-rently.herokuapp.com/) and dockerized [here](https://hub.docker.com/repository/docker/dockeroo80/rently-image-service).

> ⚠️ Please note that the service is currently deployed on a free Heroku instance and needs a few seconds to warm up on first request!

### C2 model
![C2 model](https://i.imgur.com/CqQbDQA.png)

## Objects

### JWT Object

| **Field**         | **Description**              |
| ----------------- | ---------------------------- |
| `sub` uuid string | The user's id                |
| `iat` timestamp   | Issue time of the token      |
| `exp` timestamp   | Expiration time of the token |
| `jti` uuid string | The token's id               |

<br />

## Request Mappings

### `GET /api/v1/images/{id}`

Returns a json [response](#response-object) object containing one [user](#user-object) object. Permits fetching user data using a `provider` and a `provider account id`.

#### URL parameters:

|       **Parameter** | **Description**           | **Required** |
| ------------------: | ------------------------- | :----------: |
|   `provider` string | Valid provider name       |     true     |
| `providerId` string | Valid provider account id |     true     |

#### Request body parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "content": {
    "id": "3bdb141f-deb6-4260-a8ac-999e6ab9c89d",
    "name": "fmullett4",
    "provider": "facebook",
    "providerId": "123123abcabc",
    "email": "fmullett4@51.la",
    "createdAt": "1617868768000",
    "updatedAt": "1635152066000"
  }
}
```
