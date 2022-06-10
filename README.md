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
![C2 model](https://i.imgur.com/34Nvkd4.jpg)

## Objects

### JWT Object

| **Field**         | **Description**              |
| ----------------- | ---------------------------- |
| `sub` uuid string | The image's id                |
| `iat` timestamp   | Issue time of the token      |
| `exp` timestamp   | Expiration time of the token |
| `jti` uuid string | The token's id               |

<br />

## Request Mappings

### `GET /api/v1/images/{id}`

Returns a JPEG image upon specifying a valid image `id`.

#### URL parameters:

|       **Parameter** | **Description**           | **Required** |
| ------------------: | ------------------------- | :----------: |
|   `id` string       | Valid image id            |     true     |

#### Request body parameters:

> _none_

<br />

### `POST /api/v1/images/{id}`

Returns a URL after supplying proper image base64 data upon specifying a valid image `id`.

#### URL parameters:

|       **Parameter** | **Description**           | **Required** |
| ------------------: | ------------------------- | :----------: |
|   `id` uuid string       | Valid image id            |     true     |

#### Request body parameters:

Base64 image data.

#### Return example:

```
https://images-service-rently.herokuapp.com/api/v1/images/9f4de707-28fb-4511-b519-3684b848ed35
```

### `PUT /api/v1/images/{id}`

Returns a URL after supplying proper image base64 data upon specifying a valid exsiting image `id`.

#### URL parameters:

|       **Parameter** | **Description**           | **Required** |
| ------------------: | ------------------------- | :----------: |
|   `id` uuid string       | Valid image id            |     true     |

#### Request body parameters:

Base64 image data.

#### Return example:

```
https://images-service-rently.herokuapp.com/api/v1/images/9f4de707-28fb-4511-b519-3684b848ed35
```

### `DELETE /api/v1/images/{id}`

Deletes an image id a valid existing image `id`.

#### URL parameters:

|       **Parameter** | **Description**           | **Required** |
| ------------------: | ------------------------- | :----------: |
|   `id` uuid string       | Valid image id            |     true     |

#### Request body parameters:

> _none_
